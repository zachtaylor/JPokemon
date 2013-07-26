package org.jpokemon;

import org.jpokemon.item.Bag;
import org.jpokemon.item.Item;
import org.jpokemon.pokedex.Pokedex;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.pokemon.move.Move;
import org.jpokemon.pokemon.stat.Stat;
import org.jpokemon.pokemon.storage.PokemonStorageUnit;
import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.Record;

public interface JPokemonVisitor {
  public void visit(Player player);

  public void visit(Record record);

  public void visit(Pokedex pokedex);

  public void visit(Bag bag);

  public void visit(Item item);

  public void visit_party(PokemonStorageUnit unit);

  public void visit(PokemonStorageUnit unit);

  public void visit_party_leader(Pokemon pokemon);

  public void visit(Pokemon pokemon);

  public void visit(Stat stat);

  public void visit(Move move);
}