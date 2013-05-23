package com.jpokemon.mapeditor.widget;

import java.awt.Component;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;

import org.jpokemon.map.Area;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class AreaSelector extends JComboBox {
  public AreaSelector() {
    setModel(model);
    setRenderer(new AreaCellRenderer());
  }

  public Area getArea() {
    return (Area) model.getSelectedItem();
  }

  public void reload() {
    int selectedIndex = getSelectedIndex();

    if (selectedIndex < 0) {
      selectedIndex = 0;
    }

    removeAllItems();

    Area area;
    for (int i = 1; (area = Area.get(i)) != null; i++) {
      model.addElement(area);
    }

    setSelectedIndex(selectedIndex);
  }

  private class AreaCellRenderer extends DefaultListCellRenderer {
    public AreaCellRenderer() {
      setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object o, int index, boolean isSelected,
        boolean cellHasFocus) {
      if (index >= 0) {
        Area area = (Area) model.getElementAt(index);
        setText(area.toString());
      }

      return super.getListCellRendererComponent(list, o, index, isSelected, cellHasFocus);
    }

    private static final long serialVersionUID = 1L;
  }

  private DefaultComboBoxModel model = new DefaultComboBoxModel();

  private static final long serialVersionUID = 1L;
}