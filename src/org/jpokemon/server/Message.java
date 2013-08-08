package org.jpokemon.server;

import org.json.JSONException;
import org.json.JSONObject;

public class Message {
  public Message(String s, String m, Message.Level l) {
    source = s;
    message = m;
    level = l;
  }

  public String getType() {
    return source;
  }

  public Message.Level getLevel() {
    return level;
  }

  public JSONObject toJson() {
    JSONObject data = new JSONObject();

    try {
      data.put("action", "message");
      data.put("type", source);
      data.put("message", message);
      data.put("level", level.toString().toLowerCase());
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return data;
  }

  private Message.Level level;
  private String source, message;

  public enum Level {
    LOG, MESSAGE, NOTIFICATION;
  }
}