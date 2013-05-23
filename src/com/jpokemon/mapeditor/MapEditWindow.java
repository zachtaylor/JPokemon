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
    components.put(NPCEditor.BUTTON_NAME, new NPCEditor());
    components.put(NPCTypeEditor.BUTTON_NAME, new NPCTypeEditor());
    components.put(AreaEditor.BUTTON_NAME, new AreaEditor());
    components.put(WildPokemonEditor.BUTTON_NAME, new WildPokemonEditor());

    setLayout(new BorderLayout());

    JPanel sectionPanel = new JPanel();
    for (String component : components.keySet()) {
      sectionPanel.add(new ComponentButton(component));
    }

    add(sectionPanel, BorderLayout.NORTH);

    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(400, 300);
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
      d.setSize(d.width, d.height + 80);
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

  private static final long serialVersionUID = 1L;
}