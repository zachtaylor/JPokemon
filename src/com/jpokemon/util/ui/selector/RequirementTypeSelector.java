package com.jpokemon.util.ui.selector;

import org.jpokemon.action.RequirementType;

import com.jpokemon.util.ui.JPokemonSelector;

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