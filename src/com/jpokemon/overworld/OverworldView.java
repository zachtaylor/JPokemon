package com.jpokemon.overworld;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jpokemon.service.ImageService;
import org.jpokemon.service.MapService;
import org.jpokemon.service.ServiceException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jpokemon.GameWindow;
import com.jpokemon.JPokemonView;

public class OverworldView extends JPokemonView {
  public OverworldView(GameWindow parent) {
    super(parent);

    nameLabel = new JLabel("x");
    infoPanel = new JPanel();
    npcPanel = new JPanel();

    setLayout(new BorderLayout());

    infoPanel.add(nameLabel);
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

    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public void onClickNPCButton(int number) {
    try {
      JSONObject request = new JSONObject();

      request.put("id", parent().playerID());
      request.put("number", number);

      MapService.npc(request);
      refresh();

    } catch (JSONException e) {
      e.printStackTrace();
    } catch (ServiceException e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean key(KeyEvent arg0) {
    return false;
  }

  private class NPCButton extends JButton implements ActionListener {
    private int _id;

    public NPCButton(JSONObject obj) throws JSONException {
      super(ImageService.npc(obj.getString("icon")));
      addActionListener(this);
      _id = obj.getInt("number");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      OverworldView.this.onClickNPCButton(_id);
    }
  }

  private JLabel nameLabel;
  private JPanel npcPanel, infoPanel;
}