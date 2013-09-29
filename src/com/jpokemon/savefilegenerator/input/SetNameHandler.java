package com.jpokemon.savefilegenerator.input;

import javax.swing.JTextField;

import com.jpokemon.savefilegenerator.SaveGeneratorWindow;

public class SetNameHandler extends AbstractInputHandler {
  public SetNameHandler(SaveGeneratorWindow window, JTextField textField) {
    super(window, textField);
  }

  @Override
  public void updateParent() {
    this.boundWindow.loadOrSetName(this.boundTextField.getText());
    this.boundWindow.refresh();
  }
}