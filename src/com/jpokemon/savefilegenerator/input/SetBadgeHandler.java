package com.jpokemon.savefilegenerator.input;

import javax.swing.JTextField;

import com.jpokemon.savefilegenerator.SaveGeneratorWindow;

public class SetBadgeHandler extends AbstractInputHandler {
  public SetBadgeHandler(SaveGeneratorWindow window, JTextField textField) {
    super(window, textField);
  }

  @Override
  public void updateParent() {
    String badgeText = this.boundTextField.getText();
    Integer badgeVal = Integer.parseInt(badgeText);

    this.boundWindow.getPlayer().setBadgeCount(badgeVal);
    this.boundWindow.refresh();
  }
}