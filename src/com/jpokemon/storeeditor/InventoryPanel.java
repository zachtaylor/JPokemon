package com.jpokemon.storeeditor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jpokemon.item.Inventory;

import com.jpokemon.util.FeedbackInputField;
import com.jpokemon.util.ui.selector.ItemSelector;

public class InventoryPanel extends JPanel {
  private Inventory inventory;
  private ItemSelector itemSelector = new ItemSelector();
  private FeedbackInputField priceField = new FeedbackInputField();
  private FeedbackInputField availableField = new FeedbackInputField();
  private FeedbackInputField denominationField = new FeedbackInputField();
  private FeedbackInputField purchaseField = new FeedbackInputField();

  private static final long serialVersionUID = 1L;

  public InventoryPanel(Inventory i) {
    inventory = i;

    buildItemSelector();
    buildPriceField();
    buildPurchaseField();
    buildDenominationField();
    buildAvailableField();
  }

  protected void buildItemSelector() {
    itemSelector.reload();
    itemSelector.setSelectedIndex(inventory.getItem() - 1);
    itemSelector.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onItemSelect();
      }
    });
    add(itemSelector);
  }

  protected void onItemSelect() {
    int newItemId = itemSelector.getCurrentElement().getId();

    inventory.setItem(newItemId);
    inventory.commit();

    revalidate();
  }

  protected void buildPriceField() {
    String priceString = inventory.getPrice() + "";

    priceField.setText(priceString);
    priceField.setSavedValue(priceString);
    priceField.setPreferredSize(new Dimension(40, 20));
    priceField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onPriceChange();
      }
    });
    add(new JLabel("Price:"));
    add(priceField);
  }

  protected void onPriceChange() {
    try {
      int newPrice = Integer.parseInt(priceField.getText());
      priceField.setSavedValue(newPrice + "");
      inventory.setPrice(newPrice);
      inventory.commit();
      validate();
    }
    catch (NumberFormatException e) {
      priceField.setText(priceField.getSavedValue());
    }
  }

  protected void buildPurchaseField() {
    String purchaseString = inventory.getPurchaseprice() + "";

    purchaseField.setText(purchaseString);
    purchaseField.setSavedValue(purchaseString);
    purchaseField.setPreferredSize(new Dimension(40, 20));
    purchaseField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onPurchaseChange();
      }
    });
    add(new JLabel("Purchase:"));
    add(purchaseField);
  }

  protected void onPurchaseChange() {
    try {
      int newPurchase = Integer.parseInt(purchaseField.getText());
      purchaseField.setSavedValue(newPurchase + "");
      inventory.setPurchaseprice(newPurchase);
      inventory.commit();
      validate();
    }
    catch (NumberFormatException e) {
      purchaseField.setText(purchaseField.getSavedValue());
    }
  }

  protected void buildDenominationField() {
    String denominationString = inventory.getDenomination() + "";

    denominationField.setText(denominationString);
    denominationField.setSavedValue(denominationString);
    denominationField.setPreferredSize(new Dimension(40, 20));
    denominationField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onDenominationChange();
      }
    });
    add(new JLabel("Per:"));
    add(denominationField);
  }

  protected void onDenominationChange() {
    try {
      int newDenomination = Integer.parseInt(denominationField.getText());
      denominationField.setSavedValue(newDenomination + "");
      inventory.setDenomination(newDenomination);
      inventory.commit();
      validate();
    }
    catch (NumberFormatException e) {
      denominationField.setText(denominationField.getSavedValue());
    }
  }

  protected void buildAvailableField() {
    String availableString = inventory.getAvailable() + "";

    availableField.setText(availableString);
    availableField.setSavedValue(availableString);
    availableField.setPreferredSize(new Dimension(40, 20));
    availableField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onAvailableChange();
      }
    });
    add(new JLabel("Available:"));
    add(availableField);
  }

  protected void onAvailableChange() {
    try {
      int newAvailable = Integer.parseInt(availableField.getText());
      availableField.setSavedValue(newAvailable + "");
      inventory.setAvailable(newAvailable);
      inventory.commit();
      validate();
    }
    catch (NumberFormatException e) {
      availableField.setText(availableField.getSavedValue());
    }
  }
}