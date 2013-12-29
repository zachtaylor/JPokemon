package com.jpokemon.storeeditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jpokemon.item.Inventory;
import org.jpokemon.item.Store;

import com.jpokemon.util.FeedbackInputField;
import com.jpokemon.util.ui.button.JPokemonButton;
import com.jpokemon.util.ui.selector.StoreSelector;

public class StoreEditor extends JFrame {
  private boolean readyToEdit = false;
  private StoreSelector storeSelector = new StoreSelector();
  private FeedbackInputField storeNameTextField = new FeedbackInputField();
  private JPanel centerPanel = new JPanel(), northPanel = new JPanel(), southPanel = new JPanel();

  private static final long serialVersionUID = 1L;

  public StoreEditor() {
    super("Store Editor");

    buildNorthPanel();
    buildSouthPanel();

    setLayout(new BorderLayout());
    add(northPanel, BorderLayout.NORTH);
    add(southPanel, BorderLayout.SOUTH);

    centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
    JScrollPane scrollPane = new JScrollPane(centerPanel);
    add(scrollPane, BorderLayout.CENTER);

    pack();
    setVisible(true);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    readyToEdit = true;
  }

  protected void buildNorthPanel() {
    storeSelector.reload();
    storeSelector.setSelectedItem(null);
    storeSelector.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onStoreSelect();
      }
    });
    northPanel.add(storeSelector);

    storeNameTextField.setPreferredSize(new Dimension(120, 25));
    storeNameTextField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onStoreRename();
      }
    });
    northPanel.add(storeNameTextField);
  }

  protected void onStoreSelect() {
    if (!readyToEdit) { return; }

    Store store = storeSelector.getCurrentElement();

    storeNameTextField.setText(store.getName());
    storeNameTextField.setSavedValue(store.getName());

    fillInventory();
  }

  protected void onStoreRename() {
    String newStoreName = storeNameTextField.getText();
    if (newStoreName == null || newStoreName.isEmpty()) {
      storeNameTextField.setText(storeNameTextField.getSavedValue());
      return;
    }

    Store store = storeSelector.getCurrentElement();
    if (store == null) { return; }

    store.setName(newStoreName);
    store.commit();

    storeNameTextField.setSavedValue(newStoreName);
    storeSelector.repaint();
  }

  protected void buildSouthPanel() {
    southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.X_AXIS));

    JButton addStore = new JPokemonButton("Add Store");
    addStore.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onAddStore();
      }
    });
    southPanel.add(addStore);

    JButton addRow = new JPokemonButton("Add Inventory");
    addRow.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onAddInventory();
      }
    });
    southPanel.add(addRow);
  }

  protected void onAddStore() {
    (new Store()).commit();

    readyToEdit = false;
    storeSelector.reload();
    readyToEdit = true;

    storeSelector.setSelectedIndex(storeSelector.getModel().getSize() - 1);
    fillInventory();
  }

  protected void onAddInventory() {
    int storeIndex = storeSelector.getSelectedIndex();
    int storeId = storeSelector.getCurrentElement().getId();

    Inventory inventory = new Inventory();
    inventory.setStore(storeId);
    inventory.commit();

    readyToEdit = false;
    storeSelector.reload();
    storeSelector.setSelectedIndex(storeIndex);
    readyToEdit = true;

    fillInventory();
  }

  protected void fillInventory() {
    Store store = storeSelector.getCurrentElement();
    if (store == null) { return; }

    centerPanel.removeAll();
    for (Inventory inventory : store.getInventory()) {
      centerPanel.add(new InventoryPanel(inventory));
    }

    revalidate();
    repaint();
  }
}