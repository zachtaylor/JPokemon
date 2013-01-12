package jpkmn.map.spawner;

import java.util.ArrayList;
import java.util.List;

import jpkmn.exceptions.LoadException;
import jpkmn.game.base.AIInfo;

import org.jpokemon.trainer.Trainer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TrainerSpawner {
  public TrainerSpawner(List<AIInfo> list) {
    _data = new ArrayList<Spawn>();

    for (AIInfo info : list)
      _data.add(new Spawn(info.getNumber(), info.getName()));
  }

  public JSONArray toJSON() {
    JSONArray array = new JSONArray();

    try {
      for (Spawn spawn : _data) {
        JSONObject cur = new JSONObject();
        cur.put("name", spawn.name());
        cur.put("id", spawn.id());
        array.put(cur);
      }
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }

    return array;
  }

  public Trainer spawn(int trainerID) {
    Spawn target = null;

    for (Spawn cur : _data)
      if (cur.id() == trainerID)
        target = cur;

    try {
      if (target != null)
        return new Trainer(target.id());
    } catch (LoadException e) {
      e.printStackTrace();
    }

    return null;
  }

  private class Spawn {
    public Spawn(int id, String name) {
      _id = id;
      _name = name;
    }

    public int id() {
      return _id;
    }

    public String name() {
      return _name;
    }

    private int _id;
    private String _name;
  }

  private List<Spawn> _data;
}