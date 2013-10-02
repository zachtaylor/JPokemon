package com.jpokemon.mapeditor.storeeditor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jpokemon.item.Inventory;

import com.jpokemon.util.ui.selector.ItemSelector;
import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.SqlStatement;

public class InventoryPanel extends JPanel {
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
    int newItemId = itemSelector.getCurrentElement().getId();

    inventory.setItem(newItemId);
    inventory.commit();

    validate();
  }

  private void onPriceFieldEnter() {
    int oldPrice = inventory.getPrice();
    int newPrice = Integer.parseInt(priceField.getText());

    inventory.setPrice(newPrice);

    try {
      SqlStatement.update(inventory).where("store").eq(inventory.getStore()).and("item").eq(inventory.getItem()).and("price").eq(oldPrice).and("purchaseprice")
          .eq(inventory.getPurchaseprice()).and("denomination").eq(inventory.getDenomination()).and("available").eq(inventory.getAvailable()).execute();
    }
    catch (DataConnectionException e) {
      e.printStackTrace();
    }

    validate();
  }

  private void onPurchasePriceFieldEnter() {
    int oldPurchasePrice = inventory.getPurchaseprice();
    int newPurchasePrice = Integer.parseInt(purchasePriceField.getText());

    inventory.setPurchaseprice(newPurchasePrice);

    try {
      SqlStatement.update(inventory).where("store").eq(inventory.getStore()).and("item").eq(inventory.getItem()).and("price").eq(inventory.getPrice())
          .and("purchaseprice").eq(oldPurchasePrice).and("denomination").eq(inventory.getDenomination()).and("available").eq(inventory.getAvailable())
          .execute();
    }
    catch (DataConnectionException e) {
      e.printStackTrace();
    }

    validate();
  }

  private void onDenominationFieldEnter() {
    int oldDenomination = inventory.getDenomination();
    int newDenomination = Integer.parseInt(denominationField.getText());

    inventory.setDenomination(newDenomination);

    try {
      SqlStatement.update(inventory).where("store").eq(inventory.getStore()).and("item").eq(inventory.getItem()).and("price").eq(inventory.getPrice())
          .and("purchaseprice").eq(inventory.getPurchaseprice()).and("denomination").eq(oldDenomination).and("available").eq(inventory.getAvailable())
          .execute();
    }
    catch (DataConnectionException e) {
      e.printStackTrace();
    }

    validate();
  }

  private void onAvailableFieldEnter() {
    int oldAvailable = inventory.getAvailable();
    int newAvailable = Integer.parseInt(availableField.getText());

    inventory.setAvailable(newAvailable);

    try {
      SqlStatement.update(inventory).where("store").eq(inventory.getStore()).and("item").eq(inventory.getItem()).and("price").eq(inventory.getPrice())
          .and("purchaseprice").eq(inventory.getPurchaseprice()).and("denomination").eq(inventory.getDenomination()).and("available").eq(oldAvailable)
          .execute();
    }
    catch (DataConnectionException e) {
      e.printStackTrace();
    }

    validate();
  }

  private Inventory inventory;
  private JTextField priceField = new JTextField();
  private JTextField availableField = new JTextField();
  private ItemSelector itemSelector = new ItemSelector();
  private JTextField denominationField = new JTextField();
  private JTextField purchasePriceField = new JTextField();

  private static final long serialVersionUID = 1L;
}