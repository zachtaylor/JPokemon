package com.jpokemon.util;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.jpokemon.interactioneditor.InteractionEditor;
import com.jpokemon.mapeditor.MapEditWindow;
import com.jpokemon.savefilegenerator.SaveGeneratorWindow;
import com.jpokemon.storeeditor.StoreEditor;

public class UtilWindow extends JFrame {
  public UtilWindow() {
    super("JPokemon Server Utilities");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new FlowLayout());

    JButton mapEditor = new JButton("Open Map Editor");
    mapEditor.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        new MapEditWindow();
      }
    });
    add(mapEditor);

    JButton storeEditor = new JButton("Open Store Editor");
    storeEditor.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        new StoreEditor();
      }
    });
    add(storeEditor);

    JButton interactionEditor = new JButton("Open Interaction Editor");
    interactionEditor.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        new InteractionEditor();
      }
    });
    add(interactionEditor);

    JButton saveFileEditor = new JButton("Open Save File Editor");
    saveFileEditor.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        new SaveGeneratorWindow();
      }
    });
    add(saveFileEditor);

    pack();
    setVisible(true);
  }

  private static final long serialVersionUID = 1L;
}