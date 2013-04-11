package org.jpokemon.map;

import java.util.ArrayList;
import java.util.List;

import org.jpokemon.trainer.Player;
import org.json.JSONException;
import org.json.JSONObject;

public class Border {

  public int getArea() {
    return area;
  }

  public void setArea(int a) {
    area = a;
  }

  public int getNext() {
    return next;
  }

  public void setNext(int n) {
    next = n;
  }

  public void addRequirement(BorderRequirement br) {
    Requirement r = new Requirement(br.getType(), br.getData());

    _requirements.add(r);
  }

  public String isOkay(Player p) {
    if (_requirements == null)
      return null;

    for (Requirement requirement : _requirements)
      if (!requirement.isOkay(p)) {
        return requirement.denialReason();
      }

    return null;
  }

  public JSONObject toJSON(Player p) {
    JSONObject json = new JSONObject();

    Area nextArea = Map.area(next);

    try {
      json.put("name", nextArea.getName());

      String reason = isOkay(p);

      if (reason == null) {
        json.put("is_okay", true);
      }
      else {
        json.put("is_okay", false);
        json.put("reason", reason);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return json;
  }

  private int area, next;
  private List<Requirement> _requirements = new ArrayList<Requirement>();
}