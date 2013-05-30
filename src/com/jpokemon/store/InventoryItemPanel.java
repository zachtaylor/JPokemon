package com.jpokemon.store;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jpokemon.manager.component.ImageService;
import org.json.JSONException;
import org.json.JSONObject;

public class InventoryItemPanel extends JPanel {
  public InventoryItemPanel(StoreView view, JSONObject data) throws JSONException {

    storeView = view;

    itemID = data.getInt("id");
    price = data.getInt("price");
    amount = data.getInt("amount");
    itemType = data.getString("type");
    itemName = data.getString("name");
    denomination = data.getInt("denomination");
    purchaseprice = data.getInt("purchase_price");

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    JPanel iconAndInfoPanel = new JPanel();
    iconAndInfoPanel.setLayout(new BoxLayout(iconAndInfoPanel, BoxLayout.X_AXIS));
    add(iconAndInfoPanel);

    iconAndInfoPanel.add(new JLabel(ImageService.item(itemType, itemName)));

    iconAndInfoPanel.add(new JLabel(itemName + " x" + denomination));
    iconAndInfoPanel.add(new JPanel());
    JPanel buttonAndAmountPanel = new JPanel();
    buttonAndAmountPanel.setLayout(new BoxLayout(buttonAndAmountPanel, BoxLayout.X_AXIS));
    add(buttonAndAmountPanel, BorderLayout.SOUTH);

    JButton sellButton = new JButton("-" + denomination + " +$" + purchaseprice);
    sellButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        sell();
      }
    });
    buttonAndAmountPanel.add(sellButton);

    buttonAndAmountPanel.add(new JPanel());
    buttonAndAmountPanel.add(amountLabel = new JLabel());
    buttonAndAmountPanel.add(new JPanel());

    JButton buyButton = new JButton("+" + denomination + " -$" + price);
    buyButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        buy();
      }
    });
    buttonAndAmountPanel.add(buyButton);

    update();
  }

  public JSONObject getChanges() {
    if (change == 0) {
      return null;
    }

    JSONObject request = new JSONObject();

    try {
      request.put("item", itemID);
      request.put("denomination", denomination);
      request.put("change", change);
    } catch (JSONException e) {
    }

    return request;
  }

  private void sell() {
    change--;
    update();

    if (change >= 0) {
      storeView.updateCashLabel(price);
    }
    else {
      storeView.updateCashLabel(purchaseprice);
    }
  }

  private void buy() {
    change++;
    update();

    if (change <= 0) {
      storeView.updateCashLabel(-purchaseprice);
    }
    else {
      storeView.updateCashLabel(-price);
    }
  }

  private void update() {
    String changeText;

    if (change >= 0) {
      changeText = "+" + change;
    }
    else {
      changeText = change + "";
    }

    amountLabel.setText(amount + change + "(" + changeText + ")");
  }

  private StoreView storeView;
  private JLabel amountLabel;
  private String itemType, itemName;
  private int itemID, amount, price, denomination, purchaseprice, change;

  private static final long serialVersionUID = 1L;
}