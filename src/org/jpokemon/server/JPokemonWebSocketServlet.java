package org.jpokemon.server;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

public class JPokemonWebSocketServlet extends WebSocketServlet {
  private static final long serialVersionUID = 1L;

  @Override
  public WebSocket doWebSocketConnect(HttpServletRequest arg0, String arg1) {
    return new JPokemonWebSocket();
  }
}