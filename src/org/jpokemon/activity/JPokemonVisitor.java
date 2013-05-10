package org.jpokemon.activity;

import org.jpokemon.item.Bag;
import org.jpokemon.item.Item;
import org.jpokemon.pokedex.Pokedex;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.pokemon.storage.PokemonStorageBlock;
import org.jpokemon.pokemon.storage.PokemonStorageUnit;
import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.Record;

public interface JPokemonVisitor {
  public Object data();

  public void visit_player(Player player);

  public void visit_record(Record record);

  public void visit_pokedex(Pokedex pokedex);

  public void visit_bag(Bag bag);

  public void visit_item(Item item);

  public void visit_storage_block(PokemonStorageBlock block);

  public void visit_party(PokemonStorageUnit unit);

  public void visit_storage_unit(PokemonStorageUnit unit);

  public void visit_party_leader(Pokemon pokemon);

  public void visit_pokemon(Pokemon pokemon);
}