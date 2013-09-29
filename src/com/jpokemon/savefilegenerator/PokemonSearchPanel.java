package com.jpokemon.savefilegenerator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jpokemon.pokemon.PokemonInfo;
import org.jpokemon.pokemon.Type;

import com.jpokemon.util.ui.selector.TypeSelector;

public class PokemonSearchPanel extends JPanel {
  private static final String nameFieldDefault = "[Search by name]";

  public PokemonSearchPanel(PartyEditorPanel pep) {
    super(new BorderLayout());

    parent = pep;
    setPreferredSize(new Dimension(250, 300));
    setFocusable(true);

    searchFieldPanel.setFocusable(true);
    searchFieldPanel.setLayout(new BorderLayout());
    add(searchFieldPanel, BorderLayout.NORTH);

    nameField.setPreferredSize(new Dimension(100, 16));
    nameField.addFocusListener(new FieldFocuser(nameField, nameFieldDefault));
    nameField.getDocument().addDocumentListener(new DocumentListener() {
      public void changedUpdate(DocumentEvent arg0) {
        nameFieldSearch();
      }

      public void insertUpdate(DocumentEvent arg0) {
        nameFieldSearch();
      }

      public void removeUpdate(DocumentEvent arg0) {
        nameFieldSearch();
      }
    });
    searchFieldPanel.add(nameField, BorderLayout.WEST);

    typeSelector.reload();
    typeSelector.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        typeSelectorSearch();
      }
    });
    searchFieldPanel.add(typeSelector, BorderLayout.EAST);

    searchResultsPanel.setLayout(new BoxLayout(searchResultsPanel, BoxLayout.Y_AXIS));
    JScrollPane scrollPane = new JScrollPane(searchResultsPanel);
    add(scrollPane, BorderLayout.CENTER);
  }

  public void selectPokemon(PokemonInfo pi) {
    parent.addPokemon(pi);
  }

  private void nameFieldSearch() {
    String nameFieldText = nameField.getText().toLowerCase();

    if (nameFieldText.isEmpty() || nameFieldText.equals(nameFieldDefault.toLowerCase())) {
      return;
    }

    searchResultsPanel.removeAll();
    typeSelector.setSelectedItem(null);

    PokemonInfo pokemonInfo;
    for (int i = 1; (pokemonInfo = PokemonInfo.get(i)) != null; i++) {
      if (pokemonInfo.getName().toLowerCase().contains(nameFieldText)) {
        searchResultsPanel.add(new SelectablePokemonPanel(this, pokemonInfo));
      }
    }

    parent.validate();
    parent.repaint();
  }

  private void typeSelectorSearch() {
    Type selectedType = typeSelector.getCurrentElement();

    if (selectedType == null) {
      return;
    }

    searchResultsPanel.removeAll();
    nameField.setText(nameFieldDefault);

    Type t1, t2;
    PokemonInfo pokemonInfo;

    for (int i = 1; (pokemonInfo = PokemonInfo.get(i)) != null; i++) {
      t1 = Type.valueOf(pokemonInfo.getType1());
      t2 = Type.valueOf(pokemonInfo.getType2());

      if (selectedType == t1 || selectedType == t2) {
        searchResultsPanel.add(new SelectablePokemonPanel(this, pokemonInfo));
      }
    }

    parent.validate();
    parent.repaint();
  }

  private PartyEditorPanel parent;
  private JPanel searchFieldPanel = new JPanel();
  private JPanel searchResultsPanel = new JPanel();
  private JTextField nameField = new JTextField(nameFieldDefault);
  private TypeSelector typeSelector = new TypeSelector();

  private static class FieldFocuser implements FocusListener {
    public FieldFocuser(JTextField jtf, String s) {
      field = jtf;
      blankText = s;
    }

    @Override
    public void focusGained(FocusEvent arg0) {
      if (field.getText().equals(blankText)) {
        field.setText("");
      }
    }

    @Override
    public void focusLost(FocusEvent arg0) {
      if (field.getText().isEmpty()) {
        field.setText(blankText);
      }
    }

    private String blankText;
    private JTextField field;
  }

  public static final long serialVersionUID = 1L;
}