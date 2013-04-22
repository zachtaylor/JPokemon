package com.jpokemon.overworld;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jpokemon.service.ImageService;
import org.jpokemon.service.MapService;
import org.jpokemon.service.ServiceException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jpokemon.GameWindow;
import com.jpokemon.JPokemonButton;
import com.jpokemon.JPokemonView;

public class OverworldView extends JPokemonView {
  public OverworldView(GameWindow parent) {
    super(parent);

    JPanel infoPanel = new JPanel();
    nameLabel = new JLabel("x");

    npcPanel = new JPanel();
    JPanel borderMenuAndLabel = new JPanel();
    borderSelection = new JComboBox<String>();

    borderSelection.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent e) {
        String currentAreaName = nameLabel.getText();
        if (e.getStateChange() == ItemEvent.SELECTED && !e.getItem().equals(currentAreaName))
          onSelectBorder((String) e.getItem());
      }
    });

    infoPanel.setLayout(new BorderLayout());
    setLayout(new BorderLayout());

    borderMenuAndLabel.add(new JLabel("Go to >"));
    borderMenuAndLabel.add(borderSelection);
    infoPanel.add(nameLabel, BorderLayout.WEST);
    infoPanel.add(borderMenuAndLabel, BorderLayout.EAST);
    add(infoPanel, BorderLayout.NORTH);
    add(npcPanel, BorderLayout.CENTER);
  }

  @Override
  public Dimension dimension() {
    return new Dimension(400, 200);
  }

  @Override
  public void update(JSONObject data) {
    try {
      nameLabel.setText(data.getString("name"));

      npcPanel.removeAll();
      JSONArray npcs = data.getJSONArray("npcs");
      for (int i = 0; i < npcs.length(); i++) {
        npcPanel.add(new NPCButton(npcs.getJSONObject(i)));
      }

      borderMemory.clear();
      borderSelection.removeAllItems();
      borderSelection.addItem(data.getString("name"));
      JSONArray borders = data.getJSONArray("borders");
      for (int i = 0; i < npcs.length(); i++) {
        JSONObject border = borders.getJSONObject(i);

        borderSelection.addItem(border.getString("name"));

        borderMemory.add(border);
      }

      parent().align();

    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public void onClickNPCButton(int number, String option) {
    try {
      JSONObject request = new JSONObject();

      request.put("id", parent().playerID());
      request.put("number", number);
      request.put("option", option);

      MapService.npc(request);
      refresh();

    } catch (JSONException e) {
      e.printStackTrace();
    } catch (ServiceException e) {
      e.printStackTrace();
    }
  }

  public void onSelectBorder(String border) {
    System.out.println("Go to >" + border);
  }

  @Override
  public boolean key(KeyEvent arg0) {
    return false;
  }

  private class NPCButton extends JPokemonButton implements ActionListener {

    public NPCButton(JSONObject obj) throws JSONException {
      super(ImageService.npc(obj.getString("icon")));

      addActionListener(this);

      _id = obj.getInt("number");

      _name = obj.getString("name");

      JSONArray options = obj.getJSONArray("options");
      _options = new String[options.length()];
      for (int i = 0; i < options.length(); i++) {
        _options[i] = options.getString(i);
      }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      String selectedOption;

      if (_options.length == 0) {
        return;
      }

      if (_options.length == 1) {
        selectedOption = _options[0];
      }
      else {
        selectedOption = (String) JOptionPane.showInputDialog(parent(), _name + "...", "Speaking to " + _name,
            JOptionPane.QUESTION_MESSAGE, getIcon(), _options, null);
      }

      if (selectedOption != null)
        OverworldView.this.onClickNPCButton(_id, selectedOption);
    }

    private int _id;
    private String _name;
    private String[] _options;

    private static final long serialVersionUID = 1L;
  }

  private JLabel nameLabel;
  private JPanel npcPanel;
  private JComboBox<String> borderSelection;
  private List<JSONObject> borderMemory = new ArrayList<JSONObject>();

  private static final long serialVersionUID = 1L;
}