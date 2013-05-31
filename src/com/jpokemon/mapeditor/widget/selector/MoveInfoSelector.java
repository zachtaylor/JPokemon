package com.jpokemon.mapeditor.widget.selector;

import org.jpokemon.pokemon.move.MoveInfo;

public class MoveInfoSelector extends JPokemonSelector<MoveInfo> {
  @Override
  protected void reloadItems() {
    removeAllItems();

    MoveInfo moveInfo;
    for (int i = 1; (moveInfo = MoveInfo.get(i)) != null; i++) {
      addElementToModel(moveInfo);
    }
  }

  private static final long serialVersionUID = 1L;
}