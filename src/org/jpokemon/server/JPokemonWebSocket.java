package org.jpokemon.server;

import java.io.IOException;

import org.eclipse.jetty.websocket.WebSocket;
import org.jpokemon.manager.PlayerManager;
import org.jpokemon.manager.ServiceException;
import org.json.JSONException;
import org.json.JSONObject;

public class JPokemonWebSocket implements WebSocket.OnTextMessage {
  private Connection connection;

  @Override
  public void onClose(int arg0, String arg1) {
  }

  @Override
  public void onOpen(Connection c) {
    connection = c;
  }

  @Override
  public void onMessage(String arg0) {
    try {
      PlayerManager.dispatchRequest(this, new JSONObject(arg0));
    } catch (JSONException e) {
      e.printStackTrace();
    } catch (ServiceException e) {
      e.printStackTrace();
    }
  }

  public void sendJson(JSONObject json) {
    try {
      connection.sendMessage(json.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void sendLog(String message) {
    JSONObject json = new JSONObject();

    try {
      json.put("action", "log");
      json.put("message", message);
    } catch (JSONException e) {
    }

    sendJson(json);
  }
}