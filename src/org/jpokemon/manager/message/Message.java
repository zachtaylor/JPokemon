package org.jpokemon.manager.message;

import org.json.JSONException;
import org.json.JSONObject;

public class Message {
  public Message(String s, String m, MessageLevel l) {
    source = s;
    message = m;
    level = l;
  }

  public String getType() {
    return source;
  }

  public MessageLevel getLevel() {
    return level;
  }

  public JSONObject toJSON() {
    JSONObject data = new JSONObject();

    try {
      data.put("type", source);
      data.put("message", message);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return data;
  }

  private MessageLevel level;
  private String source, message;
}