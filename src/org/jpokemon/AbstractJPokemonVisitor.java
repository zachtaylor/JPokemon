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

public abstract class AbstractJPokemonVisitor implements JPokemonVisitor {
  @Override
  public void visit(Player player) {
    visit(player.record());

    visit(player.pokedex());

    visit(player.bag());

    visit(player.getAllPokemon());
  }

  @Override
  public void visit(Record record) {
  }

  @Override
  public void visit(Pokedex pokedex) {
  }

  @Override
  public void visit(Bag bag) {
    for (Item item : bag) {
      visit(lastItem = item);
    }
  }

  @Override
  public void visit(Item item) {
  }

  protected Item last_visited_item() {
    return lastItem;
  }

  @Override
  public void visit(PokemonStorageBlock block) {
    PokemonStorageUnit unit;

    for (int i = 0; i < JPokemonConstants.PLAYER_STORAGE_UNIT_COUNT; i++) {
      unit = lastUnit = block.get(i);

      if (i < 1) {
        visit_party(unit);
      }
      else {
        visit(unit);
      }
    }
  }

  @Override
  public void visit_party(PokemonStorageUnit unit) {
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

  @Override
  public void visit(PokemonStorageUnit unit) {
    for (Pokemon pokemon : unit) {
      visit(lastPokemon = pokemon);
    }
  }

  protected PokemonStorageUnit last_storage_unit() {
    return lastUnit;
  }

  @Override
  public void visit_party_leader(Pokemon pokemon) {
    visit(pokemon);
  }

  @Override
  public void visit(Pokemon pokemon) {
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

  @Override
  public void visit(Stat stat) {
  }

  protected StatType last_stat_type() {
    return lastStatType;
  }

  @Override
  public void visit(Move move) {
  }

  private Item lastItem;
  private Pokemon lastPokemon;
  private StatType lastStatType;
  private PokemonStorageUnit lastUnit;
}