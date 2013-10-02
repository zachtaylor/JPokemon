package com.jpokemon.mapeditor.storeeditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jpokemon.item.Inventory;
import org.jpokemon.item.Store;

import com.jpokemon.mapeditor.MapEditComponent;
import com.jpokemon.util.ui.button.JPokemonButton;
import com.jpokemon.util.ui.selector.StoreSelector;

public class StoreEditor implements MapEditComponent {
  public static final String BUTTON_NAME = "Stores";

  private boolean readyToEdit = false;
  private JTextField storeNameTextField = new JTextField();
  private StoreSelector storeSelector = new StoreSelector();
  private JPanel editorPanel = new JPanel(), childContainer = new JPanel();

  public StoreEditor() {
    JPanel northPanel = new JPanel(), southPanel = new JPanel();

    storeSelector.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onStoreSelect();
      }
    });
    northPanel.add(storeSelector);

    storeNameTextField.setPreferredSize(new Dimension(100, 25));
    storeNameTextField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onStoreNameSelect();
      }
    });
    northPanel.add(storeNameTextField);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    southPanel.add(buttonPanel);

    JButton addStore = new JPokemonButton("Add Store");
    addStore.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onSelectAddStore();
      }
    });
    buttonPanel.add(addStore);

    JButton addRow = new JPokemonButton("Add Inventory");
    addRow.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onSelectAddInventory();
      }
    });
    buttonPanel.add(addRow);

    editorPanel.setLayout(new BorderLayout());
    editorPanel.add(northPanel, BorderLayout.NORTH);
    editorPanel.add(childContainer, BorderLayout.CENTER);
    editorPanel.add(southPanel, BorderLayout.SOUTH);
  }

  @Override
  public JPanel getEditor() {
    readyToEdit = false;

    storeSelector.reload();
    childContainer.removeAll();

    Store store = storeSelector.getCurrentElement();
    if (store == null) {
      storeNameTextField.setText("");
    }
    else {
      storeNameTextField.setText(store.getName());
      for (Inventory inventory : store.getInventory()) {
        childContainer.add(new InventoryPanel(inventory));
      }
    }

    readyToEdit = true;
    return editorPanel;
  }

  @Override
  public Dimension getSize() {
    return new Dimension(480, 480);
  }

  private void onStoreSelect() {
    if (!readyToEdit) return;

    getEditor();
  }

  private void onStoreNameSelect() {
    if (!readyToEdit) return;

    String storeName = storeNameTextField.getText();
    if (storeName == null || storeName.isEmpty()) { return; }

    Store store = storeSelector.getCurrentElement();
    if (store == null) { return; }

    store.setName(storeName);
    store.commit();
    storeSelector.reload();
    storeSelector.setSelectedIndex(store.getId() - 1);
  }

  private void onSelectAddStore() {
    if (!readyToEdit) return;

    Store store = new Store();
    store.commit();

    storeSelector.reload();
    storeSelector.setSelectedIndex(storeSelector.getModel().getSize() - 1);
  }

  private void onSelectAddInventory() {
    if (!readyToEdit) return;

    int storeId = storeSelector.getCurrentElement().getId();

    Inventory inventory = new Inventory();
    inventory.setStore(storeId);
    inventory.commit();

    getEditor();
  }
}