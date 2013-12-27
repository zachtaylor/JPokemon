package com.jpokemon.util;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class FeedbackInputField extends JTextField implements DocumentListener {
  private String savedValue;
  private static final long serialVersionUID = 1L;

  public FeedbackInputField() {
    getDocument().addDocumentListener(this);
  }

  public String getSavedValue() {
    return savedValue;
  }

  public void setSavedValue(String s) {
    savedValue = s;
    colorBorder();
  }

  public void colorBorder() {
    String currentValue = getText();

    if (savedValue != null && savedValue.equals(currentValue)) {
      setBorder(BorderFactory.createLineBorder(Color.black));
    }
    else {
      setBorder(BorderFactory.createLineBorder(Color.red));
    }
  }

  // DocumentListener methods

  @Override
  public void changedUpdate(DocumentEvent e) { // Doesn't do anything
  }

  @Override
  public void insertUpdate(DocumentEvent e) {
    colorBorder();
  }

  @Override
  public void removeUpdate(DocumentEvent e) {
    colorBorder();
  }
}