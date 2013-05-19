package com.jpokemon.store;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jpokemon.service.ImageService;
import org.json.JSONException;
import org.json.JSONObject;

public class InventoryItemPanel extends JPanel {
  public InventoryItemPanel(JSONObject data) throws JSONException {

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
    iconAndInfoPanel.add(new JPanel());

    JPanel infoPanel = new JPanel();
    infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
    iconAndInfoPanel.add(infoPanel);

    infoPanel.add(new JLabel(itemName + " x" + denomination));
    infoPanel.add(new JLabel("Price: " + price));
    infoPanel.add(new JLabel("Purchase Price: " + purchaseprice));

    JPanel buttonAndAmountPanel = new JPanel();
    buttonAndAmountPanel.setLayout(new BoxLayout(buttonAndAmountPanel, BoxLayout.X_AXIS));
    add(buttonAndAmountPanel, BorderLayout.SOUTH);

    JButton sellButton = new JButton("-" + denomination);
    sellButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        sell();
      }
    });
    buttonAndAmountPanel.add(sellButton);

    buttonAndAmountPanel.add(new JPanel());
    buttonAndAmountPanel.add(amountLabel = new JLabel());
    buttonAndAmountPanel.add(new JPanel());

    JButton buyButton = new JButton("+" + denomination);
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
  }

  private void buy() {
    change++;
    update();
  }

  private void update() {
    String amountText;

    if (change > 0) {
      amountText = "Buy " + change + " (" + (amount + change) + ")";
    }
    else if (change < 0) {
      amountText = "Sell " + -change + " (" + (amount + change) + ")";
    }
    else {
      amountText = "No change (" + amount + ")";
    }

    amountLabel.setText(amountText);
  }

  private JLabel amountLabel;
  private String itemType, itemName;
  private int itemID, amount, price, denomination, purchaseprice, change;

  private static final long serialVersionUID = 1L;
}