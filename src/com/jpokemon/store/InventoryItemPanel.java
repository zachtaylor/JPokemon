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

    price = data.getInt("price");
    itemType = data.getString("type");
    itemName = data.getString("name");
    denomination = data.getInt("denomination");
    purchaseprice = data.getInt("purchase_price");

    JButton sellButton = new JButton(String.format("-%i (+$%i)", denomination, purchaseprice));
    sellButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        sell();
      }
    });
    add(sellButton);

    add(new JPanel());

    add(new JLabel(ImageService.item(itemType, itemName)));

    add(new JPanel());

    JButton buyButton = new JButton(String.format("+%i (-$%i)", denomination, price));
    buyButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        buy();
      }
    });
    add(buyButton);
  }

  private void sell() {
    System.out.println("Sell " + denomination + " of " + itemName + ", receive " + purchaseprice);
  }

  private void buy() {
    System.out.println("Buy " + denomination + " of " + itemName + ", pay " + price);
  }

  private String itemType, itemName;
  private int price, denomination, purchaseprice;

  private static final long serialVersionUID = 1L;
}