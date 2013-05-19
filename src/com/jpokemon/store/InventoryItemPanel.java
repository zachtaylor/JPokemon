package com.jpokemon.store;

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
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

    itemID = data.getInt("id");
    price = data.getInt("price");
    itemType = data.getString("type");
    itemName = data.getString("name");
    denomination = data.getInt("denomination");
    purchaseprice = data.getInt("purchase_price");

    JButton sellButton = new JButton("-" + denomination + " (+$" + purchaseprice + ")");
    sellButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        sell();
      }
    });
    add(sellButton);

    add(new JPanel());

    add(new JLabel(ImageService.item(itemType, itemName)));

    add(new JPanel());

    JButton buyButton = new JButton("+" + denomination + " (-$" + price + ")");
    buyButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        buy();
      }
    });
    add(buyButton);
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

    // TODO : Graphically show
  }

  private void buy() {
    change++;

    // TODO : Graphically show
  }

  private String itemType, itemName;
  private int itemID, price, denomination, purchaseprice, change;

  private static final long serialVersionUID = 1L;
}