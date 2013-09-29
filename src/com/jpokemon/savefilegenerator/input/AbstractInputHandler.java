package com.jpokemon.savefilegenerator.input;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.jpokemon.savefilegenerator.SaveGeneratorWindow;

public abstract class AbstractInputHandler implements DocumentListener {
  protected JTextField boundTextField;
  protected SaveGeneratorWindow boundWindow;

  public AbstractInputHandler(SaveGeneratorWindow window, JTextField textField) {
    this.boundWindow = window;
    this.boundTextField = textField;
  }

  @Override
  public void insertUpdate(DocumentEvent e) {
    updateParent();
  }

  @Override
  public void removeUpdate(DocumentEvent e) {
    updateParent();
  }

  @Override
  public void changedUpdate(DocumentEvent e) { }

  protected abstract void updateParent();
}
