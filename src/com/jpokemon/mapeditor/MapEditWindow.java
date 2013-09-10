package com.jpokemon.mapeditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MapEditWindow extends JFrame {
  public MapEditWindow() {
    components.put(WildPokemonEditor.BUTTON_NAME, new WildPokemonEditor());
    components.put(EventEditor.BUTTON_NAME, new EventEditor());
    components.put(StoreEditor.BUTTON_NAME, new StoreEditor());

    setLayout(new BorderLayout());

    JPanel sectionPanel = new JPanel();
    for (String component : components.keySet()) {
      sectionPanel.add(new ComponentButton(component));
    }

    add(sectionPanel, BorderLayout.NORTH);

    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setSize(MINIMUM_WIDTH, 80);
    setVisible(true);
  }

  private void onClickComponentButton(String name) {
    if (currentView != null) {
      remove(currentView);
    }

    MapEditComponent mec = components.get(name);

    if (mec != null) {
      currentView = mec.getEditor();
      add(currentView, BorderLayout.CENTER);

      Dimension d = mec.getSize();
      d.setSize(Math.max(d.width, MINIMUM_WIDTH), d.height + 80);
      setSize(d);
      repaint();
    }
  }

  private class ComponentButton extends JButton implements ActionListener {
    public ComponentButton(String name) {
      super(name);
      addActionListener(this);
    }

    public void actionPerformed(ActionEvent arg0) {
      onClickComponentButton(getText());
    }

    private static final long serialVersionUID = 1L;
  }

  private JPanel currentView = null;
  private Map<String, MapEditComponent> components = new HashMap<String, MapEditComponent>();

  private static final int MINIMUM_WIDTH = 1140;
  private static final long serialVersionUID = 1L;
}