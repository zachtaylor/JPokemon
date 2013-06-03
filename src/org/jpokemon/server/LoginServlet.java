package org.jpokemon.server;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jpokemon.manager.PlayerManager;
import org.jpokemon.manager.ServiceException;

public class LoginServlet extends HttpServlet {
  public static final String URL_PATH = "/login/*";

  protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String playerName = request.getParameter("name");
    String rivalName = request.getParameter("rival");
    logger.info(request.getMethod() + " " + request.getRequestURI());

    if (playerName == null) {
      response.getWriter().write("\"name\" field required to create player");
      return;
    }
    if (rivalName == null) {
      response.getWriter().write("\"rival\" field required to create player");
      return;
    }
    

    String playerID = PlayerManager.createPlayer(playerName, rivalName);
    response.getWriter().write("Welcome to JPokemon\nYour id: " + playerID);
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String playerName = request.getParameter("name");
    logger.info(request.getMethod() + " " + request.getRequestURI());

    if (playerName == null) {
      response.getWriter().write("\"name\" field required to load player");
      return;
    }

    try {
      String playerID = PlayerManager.loadPlayer(playerName);
      response.getWriter().write("Welcome to JPokemon\nYour id: " + playerID);
    } catch (ServiceException e) {
      response.getWriter().write("Error loading player: " + e.getMessage());
    }
  }

  private static final long serialVersionUID = 1L;
  private static Logger logger = Logger.getLogger(LoginServlet.class);
}