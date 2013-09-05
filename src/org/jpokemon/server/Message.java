package org.jpokemon.server;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class Message {
  public Message(String t) {
    text = t;
  }

  public JSONObject toJson() {
    try {
      return getJson();
    }
    catch (JSONException e) {
    }

    return null;
  }

  /* Make things easy for subclasses */
  protected abstract JSONObject getJson() throws JSONException;

  protected String text;

  public static class Notification extends Message {
    public Notification(String t) {
      super(t);
    }

    protected JSONObject getJson() throws JSONException {
      JSONObject json = new JSONObject();
      json.put("action", "notification");
      json.put("text", text);
      return json;
    }
  }
}