package com.jpokemon.mapeditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jpokemon.item.Inventory;

import com.jpokemon.util.ui.button.JPokemonButton;
import com.jpokemon.util.ui.selector.ItemSelector;
import com.jpokemon.util.ui.selector.StoreSelector;
import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.SqlStatement;

public class StoreEditor implements MapEditComponent {
  public static final String BUTTON_NAME = "Stores";

  public StoreEditor() {
    JPanel northPanel = new JPanel(), southPanel = new JPanel();

    storeSelector.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onStoreSelect();
      }
    });
    northPanel.add(storeSelector);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    southPanel.add(buttonPanel);

    JButton addRow = new JPokemonButton("Add Row");
    addRow.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onSelectAddRow();
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
    for (Inventory inventory : storeSelector.getCurrentElement()) {
      childContainer.add(new InventoryPanel(inventory));
    }

    readyToEdit = true;
    return editorPanel;
  }

  @Override
  public Dimension getSize() {
    return new Dimension(480, 480);
  }

  private void onStoreSelect() {
    if (!readyToEdit)
      return;

    getEditor();
  }

  private void onSelectAddRow() {
    if (!readyToEdit)
      return;

    Inventory inventory = new Inventory();
    inventory.setStore(storeSelector.getCurrentElement().getNumber());

    try {
      SqlStatement.insert(inventory).execute();
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    getEditor();
  }

  private class InventoryPanel extends JPanel {
    public InventoryPanel(Inventory i) {
      inventory = i;

      itemSelector.reload();
      itemSelector.setSelectedIndex(inventory.getItem() - 1);
      itemSelector.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
          onItemSelect();
        }
      });
      add(itemSelector);

      priceField.setText(inventory.getPrice() + "");
      priceField.setPreferredSize(new Dimension(40, 20));
      priceField.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
          onPriceFieldEnter();
        }
      });
      add(new JLabel("Price:"));
      add(priceField);

      purchasePriceField.setText(inventory.getPurchaseprice() + "");
      purchasePriceField.setPreferredSize(new Dimension(40, 20));
      purchasePriceField.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
          onPurchasePriceFieldEnter();
        }
      });
      add(new JLabel("Purchase:"));
      add(purchasePriceField);

      denominationField.setText(inventory.getDenomination() + "");
      denominationField.setPreferredSize(new Dimension(40, 20));
      denominationField.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
          onDenominationFieldEnter();
        }
      });
      add(new JLabel("Per:"));
      add(denominationField);

      availableField.setText(inventory.getAvailable() + "");
      availableField.setPreferredSize(new Dimension(40, 20));
      availableField.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
          onAvailableFieldEnter();
        }
      });
      add(new JLabel("Available:"));
      add(availableField);
    }

    private void onItemSelect() {
      int oldItem = inventory.getItem();
      int newItem = itemSelector.getCurrentElement().getNumber();

      inventory.setItem(newItem);

      try {
        SqlStatement.update(inventory).where("store").eq(inventory.getStore()).and("item").eq(oldItem).and("price").eq(inventory.getPrice()).and("purchaseprice").eq(inventory.getPurchaseprice()).and("denomination").eq(inventory.getDenomination()).and("available").eq(inventory.getAvailable()).execute();
      } catch (DataConnectionException e) {
        e.printStackTrace();
      }

      getEditor();
    }

    private void onPriceFieldEnter() {
      int oldPrice = inventory.getPrice();
      int newPrice = Integer.parseInt(priceField.getText());

      inventory.setPrice(newPrice);

      try {
        SqlStatement.update(inventory).where("store").eq(inventory.getStore()).and("item").eq(inventory.getItem()).and("price").eq(oldPrice).and("purchaseprice").eq(inventory.getPurchaseprice()).and("denomination").eq(inventory.getDenomination()).and("available").eq(inventory.getAvailable()).execute();
      } catch (DataConnectionException e) {
        e.printStackTrace();
      }

      getEditor();
    }

    private void onPurchasePriceFieldEnter() {
      int oldPurchasePrice = inventory.getPurchaseprice();
      int newPurchasePrice = Integer.parseInt(purchasePriceField.getText());

      inventory.setPurchaseprice(newPurchasePrice);

      try {
        SqlStatement.update(inventory).where("store").eq(inventory.getStore()).and("item").eq(inventory.getItem()).and("price").eq(inventory.getPrice()).and("purchaseprice").eq(oldPurchasePrice).and("denomination").eq(inventory.getDenomination()).and("available").eq(inventory.getAvailable()).execute();
      } catch (DataConnectionException e) {
        e.printStackTrace();
      }

      getEditor();
    }

    private void onDenominationFieldEnter() {
      int oldDenomination = inventory.getDenomination();
      int newDenomination = Integer.parseInt(denominationField.getText());

      inventory.setDenomination(newDenomination);

      try {
        SqlStatement.update(inventory).where("store").eq(inventory.getStore()).and("item").eq(inventory.getItem()).and("price").eq(inventory.getPrice()).and("purchaseprice").eq(inventory.getPurchaseprice()).and("denomination").eq(oldDenomination).and("available").eq(inventory.getAvailable()).execute();
      } catch (DataConnectionException e) {
        e.printStackTrace();
      }

      getEditor();
    }

    private void onAvailableFieldEnter() {
      int oldAvailable = inventory.getAvailable();
      int newAvailable = Integer.parseInt(availableField.getText());

      inventory.setAvailable(newAvailable);

      try {
        SqlStatement.update(inventory).where("store").eq(inventory.getStore()).and("item").eq(inventory.getItem()).and("price").eq(inventory.getPrice()).and("purchaseprice").eq(inventory.getPurchaseprice()).and("denomination").eq(inventory.getDenomination()).and("available").eq(oldAvailable).execute();
      } catch (DataConnectionException e) {
        e.printStackTrace();
      }

      getEditor();
    }

    private Inventory inventory;
    private JTextField priceField = new JTextField();
    private JTextField availableField = new JTextField();
    private ItemSelector itemSelector = new ItemSelector();
    private JTextField denominationField = new JTextField();
    private JTextField purchasePriceField = new JTextField();

    private static final long serialVersionUID = 1L;
  }

  private boolean readyToEdit = false;
  private StoreSelector storeSelector = new StoreSelector();
  private JPanel editorPanel = new JPanel(), childContainer = new JPanel();
}