package jpkmn.map.spawner;

import java.util.ArrayList;
import java.util.List;

import jpkmn.game.base.EventInfo;
import jpkmn.map.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EventSpawner {
  public EventSpawner(List<EventInfo> list) {
    _data = new ArrayList<Event>();

    for (EventInfo info : list)
      _data.add(new Event(info));
  }

  public Event get(int eventID) {
    for (Event event : _data)
      if (event.id() == eventID)
        return event;

    return null;
  }

  public JSONArray toJSON() {
    JSONArray array = new JSONArray();

    try {
      for (Event event : _data) {
        JSONObject cur = new JSONObject();
        cur.put("id", event.id());
        cur.put("description", event.description());
        array.put(cur);
      }
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }

    return array;
  }

  private List<Event> _data;
}