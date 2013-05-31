package org.jpokemon.manager.message;

import org.json.JSONException;
import org.json.JSONObject;

public class Message {
  public Message(String t, String m, MessageLevel l) {
    type = t;
    message = m;
    level = l;
  }

  public String getType() {
    return type;
  }

  public MessageLevel getLevel() {
    return level;
  }

  public JSONObject toJSON() {
    JSONObject data = new JSONObject();

    try {
      data.put("type", type);
      data.put("message", message);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return data;
  }

  private MessageLevel level;
  private String type, message;
}