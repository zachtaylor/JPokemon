package com.jpokemon.util.mapeditor;

import java.awt.Dimension;

import javax.swing.JPanel;

public interface MapEditComponent {
  public JPanel getEditor();
  
  public Dimension getSize();
}