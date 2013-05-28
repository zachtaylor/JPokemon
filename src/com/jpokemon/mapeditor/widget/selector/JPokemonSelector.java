package com.jpokemon.mapeditor.widget.selector;

import java.awt.Component;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;

@SuppressWarnings("unchecked")
public abstract class JPokemonSelector<E> extends JComboBox<E> {
  public JPokemonSelector() {
    setModel(model);
    setRenderer(new JPokemonCellRenderer());
  }

  public E getCurrentElement() {
    return (E) model.getSelectedItem();
  }

  public void reload() {
    int selectedIndex = getSelectedIndex();

    if (selectedIndex < 0) {
      selectedIndex = 0;
    }

    reloadItems();

    if (model.getSize() > selectedIndex) {
      setSelectedIndex(selectedIndex);
    }
  }

  protected abstract void reloadItems();

  protected void addElementToModel(E element) {
    model.addElement(element);
  }

  private class JPokemonCellRenderer extends DefaultListCellRenderer {
    @Override
    @SuppressWarnings("rawtypes")
    public Component getListCellRendererComponent(JList list, Object o, int index, boolean isSelected, boolean cellHasFocus) {
      if (index >= 0) {
        E element = model.getElementAt(index);
        setText(element.toString());
      }

      return super.getListCellRendererComponent(list, o, index, isSelected, cellHasFocus);
    }

    private static final long serialVersionUID = 1L;
  }

  private DefaultComboBoxModel<E> model = new DefaultComboBoxModel<E>();

  private static final long serialVersionUID = 1L;
}