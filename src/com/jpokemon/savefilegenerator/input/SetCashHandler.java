package com.jpokemon.savefilegenerator.input;

import javax.swing.JTextField;

import com.jpokemon.savefilegenerator.SaveGeneratorWindow;

public class SetCashHandler extends AbstractInputHandler {
  public SetCashHandler(SaveGeneratorWindow window, JTextField textField) {
    super(window, textField);
  }

  @Override
  public void updateParent() {
    String cashText = this.boundTextField.getText();
    Integer cashVal = Integer.parseInt(cashText);

    this.boundWindow.getPlayer().setCash(cashVal);
    this.boundWindow.refresh();
  }
}