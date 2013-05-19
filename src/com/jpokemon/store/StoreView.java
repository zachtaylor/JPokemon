package com.jpokemon.store;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jpokemon.service.PlayerService;
import org.jpokemon.service.ServiceException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jpokemon.GameWindow;
import com.jpokemon.JPokemonButton;
import com.jpokemon.JPokemonView;

public class StoreView extends JPokemonView {
  public StoreView(GameWindow parent) {
    super(parent);

    itemsPanel = new JPanel();
    add(itemsPanel);

    JPanel buttonsPanel = new JPanel();
    add(buttonsPanel);

    JButton acceptButton = new JPokemonButton("Accept");
    acceptButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onClickAcceptButton();
      }
    });
    buttonsPanel.add(acceptButton);

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
  }

  @Override
  public Dimension dimension() {
    return new Dimension(400, 200);
  }

  @Override
  public void update(JSONObject data) {
    itemsPanel.removeAll();
    itemFamilyPanels = new HashMap<String, JPanel>();
    itemPanels = new ArrayList<InventoryItemPanel>();

    try {
      JSONArray inventoryArray = data.getJSONArray("inventory");

      for (int index = 0; index < inventoryArray.length(); index++) {
        JSONObject inventoryItem = inventoryArray.getJSONObject(index);
        String itemType = inventoryItem.getString("type");

        if (itemFamilyPanels.get(itemType) == null) {
          JPanel newPanel = new JPanel();
          newPanel.setLayout(new BoxLayout(newPanel, BoxLayout.Y_AXIS));
          newPanel.add(new JLabel(itemType + "s"));
          itemFamilyPanels.put(itemType, newPanel);
        }

        InventoryItemPanel itemPanel = new InventoryItemPanel(inventoryItem);
        itemPanels.add(itemPanel);
        itemFamilyPanels.get(itemType).add(itemPanel);
      }

      for (Map.Entry<String, JPanel> itemFamilyPanelMapping : itemFamilyPanels.entrySet()) {
        itemsPanel.add(itemFamilyPanelMapping.getValue());
      }
    } catch (JSONException e) {
    }
  }

  @Override
  public boolean key(KeyEvent arg0) {
    return false;
  }

  private void onClickAcceptButton() {
    JSONArray items = new JSONArray();

    for (InventoryItemPanel itemPanel : itemPanels) {
      JSONObject change = itemPanel.getChanges();

      if (change == null) {
        continue;
      }

      items.put(change);
    }

    JSONObject request = new JSONObject();

    try {
      request.put("id", parent().playerID());
      request.put("items", items);

      PlayerService.activity(request);
    } catch (JSONException e) {
    } catch (ServiceException e) {
      e.printStackTrace();
    }
  }

  private JPanel itemsPanel;
  private List<InventoryItemPanel> itemPanels;
  private Map<String, JPanel> itemFamilyPanels;

  private static final long serialVersionUID = 1L;
}