package com.jpokemon.mapeditor.widget.selector;

import org.jpokemon.action.RequirementType;

import com.jpokemon.ui.JPokemonSelector;

public class RequirementTypeSelector extends JPokemonSelector<RequirementType> {
  @Override
  protected void reloadItems() {
    removeAllItems();

    for (RequirementType requirementType : RequirementType.values()) {
      addElementToModel(requirementType);
    }
  }

  private static final long serialVersionUID = 1L;
}