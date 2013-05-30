package com.jpokemon.mapeditor.widget.selector;

import java.awt.Component;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;

@SuppressWarnings("unchecked")
public abstract class JPokemonSelector<E> extends JComboBox {
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

  protected void renderElement(Component c, E element) {
    ((JLabel) c).setText(element.toString());
  }

  private class JPokemonCellRenderer extends DefaultListCellRenderer {
    @Override
    @SuppressWarnings("rawtypes")
    public Component getListCellRendererComponent(JList list, Object o, int index, boolean isSelected, boolean cellHasFocus) {
      Component c = super.getListCellRendererComponent(list, o, index, isSelected, cellHasFocus);

      E element;
      if (index >= 0) {
        element = (E) model.getElementAt(index);
      }
      else {
        element = (E) model.getSelectedItem();
      }

      if (element != null) {
        renderElement(c, element);
      }

      return c;
    }

    private static final long serialVersionUID = 1L;
  }

  private DefaultComboBoxModel model = new DefaultComboBoxModel();

  private static final long serialVersionUID = 1L;
}