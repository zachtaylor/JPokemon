package org.jpokemon.server;

import org.json.JSONException;
import org.json.JSONObject;

public class Message {
  public Message(String messageClass, String text) {
    this.messageClass = messageClass;
    this.text = text;
  }

  public Message(String text) {
    this("update", text);
  }

  public JSONObject toJson() {
    try {
      return getJson();
    }
    catch (JSONException e) {
    }

    return null;
  }

  protected JSONObject getJson() throws JSONException {
    JSONObject json = new JSONObject();
    json.put("action", "notification:" + messageClass);
    json.put("text", text);
    return json;
  }

  private String text;

  private String messageClass;
}