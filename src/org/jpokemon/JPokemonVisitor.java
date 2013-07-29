package org.jpokemon;

import org.jpokemon.item.Bag;
import org.jpokemon.item.Item;
import org.jpokemon.pokedex.Pokedex;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.pokemon.move.Move;
import org.jpokemon.pokemon.stat.Stat;
import org.jpokemon.pokemon.stat.StatType;
import org.jpokemon.pokemon.storage.PokemonStorageBlock;
import org.jpokemon.pokemon.storage.PokemonStorageUnit;
import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.Record;

public abstract class JPokemonVisitor {
  public void visit(Player player) throws Exception {
    visit(player.record());

    visit(player.pokedex());

    visit(player.getBag());

    for (int i = 0; i < PokemonStorageBlock.boxcount; i++) {
      lastUnit = player.box(i);

      if (i < 1) {
        visit_party(lastUnit);
      }
      else {
        visit(lastUnit);
      }
    }
  }

  public void visit(Record record) throws Exception {
  }

  public void visit(Pokedex pokedex) throws Exception {
  }

  public void visit(Bag bag) throws Exception {
    for (Item item : bag) {
      visit(lastItem = item);
    }
  }

  public void visit(Item item) throws Exception {
  }

  protected Item last_visited_item() {
    return lastItem;
  }

  public void visit_party(PokemonStorageUnit unit) throws Exception {
    Pokemon pokemon;

    for (int i = 0; i < unit.size(); i++) {
      pokemon = lastPokemon = unit.get(i);

      if (i < 1) {
        visit_party_leader(pokemon);
      }
      else {
        visit(pokemon);
      }
    }
  }

  public void visit(PokemonStorageUnit unit) throws Exception {
    for (Pokemon pokemon : unit) {
      visit(lastPokemon = pokemon);
    }
  }

  protected PokemonStorageUnit last_storage_unit() {
    return lastUnit;
  }

  public void visit_party_leader(Pokemon pokemon) throws Exception {
    visit(pokemon);
  }

  public void visit(Pokemon pokemon) throws Exception {
    for (StatType st : StatType.values()) {
      visit(pokemon.getStat(lastStatType = st));
    }

    for (int moveIndex = 0; moveIndex < pokemon.moveCount(); moveIndex++) {
      visit(pokemon.move(moveIndex));
    }
  }

  protected Pokemon last_pokemon() {
    return lastPokemon;
  }

  public void visit(Stat stat) throws Exception {
  }

  protected StatType last_stat_type() {
    return lastStatType;
  }

  public void visit(Move move) throws Exception {
  }

  private Item lastItem;
  private Pokemon lastPokemon;
  private StatType lastStatType;
  private PokemonStorageUnit lastUnit;
}