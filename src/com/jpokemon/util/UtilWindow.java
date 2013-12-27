package com.jpokemon.util;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.jpokemon.mapeditor.MapEditWindow;
import com.jpokemon.savefilegenerator.SaveGeneratorWindow;
import com.jpokemon.storeeditor.StoreEditor;

public class UtilWindow extends JFrame {
  public UtilWindow() {
    super("JPokemon Server Utilities");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new FlowLayout());

    JButton mapEditor = new JButton("Open Map Editor");
    mapEditor.addActionListener(new OpenMapEditorHandler());
    add(mapEditor);

    JButton storeEditor = new JButton("Open Store Editor");
    storeEditor.addActionListener(new OpenStoreEditorHandler());
    add(storeEditor);

    JButton saveFileEditor = new JButton("Open Save File Editor");
    saveFileEditor.addActionListener(new OpenSaveFileHandler());
    add(saveFileEditor);

    pack();
    setVisible(true);
  }

  private class OpenMapEditorHandler implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent arg0) {
      new MapEditWindow();
    }
  }

  private class OpenStoreEditorHandler implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent arg0) {
      new StoreEditor();
    }
  }

  private class OpenSaveFileHandler implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent arg0) {
      new SaveGeneratorWindow();
    }
  }

  private static final long serialVersionUID = 1L;
}