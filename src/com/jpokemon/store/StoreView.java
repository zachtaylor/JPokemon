package com.jpokemon.store;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jpokemon.GameWindow;
import com.jpokemon.JPokemonView;

public class StoreView extends JPokemonView {
  public StoreView(GameWindow parent) {
    super(parent);

    setLayout(new FlowLayout());
  }

  @Override
  public Dimension dimension() {
    return new Dimension(400, 200);
  }

  @Override
  public void update(JSONObject data) {
    removeAll();
    itemFamilyPanels.clear();

    try {
      JSONArray inventoryArray = data.getJSONArray("inventory");

      for (int index = 0; index < inventoryArray.length(); index++) {
        JSONObject inventoryItem = inventoryArray.getJSONObject(index);
        String itemType = inventoryItem.getString("type");

        if (itemFamilyPanels.get(itemType) == null) {
          JPanel newPanel = new JPanel();
          newPanel.setLayout(new BoxLayout(newPanel, BoxLayout.Y_AXIS));
          itemFamilyPanels.put(itemType, newPanel);
        }

        itemFamilyPanels.get(itemType).add(new InventoryItemPanel(inventoryItem));
      }

      for (Map.Entry<String, JPanel> itemFamilyPanelMapping : itemFamilyPanels.entrySet()) {
        add(itemFamilyPanelMapping.getValue());
      }
    } catch (JSONException e) {
    }
  }

  @Override
  public boolean key(KeyEvent arg0) {
    return false;
  }

  private Map<String, JPanel> itemFamilyPanels = new HashMap<String, JPanel>();

  private static final long serialVersionUID = 1L;
}