package org.jpokemon.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jpokemon.manager.PlayerManager;
import org.jpokemon.manager.ServiceException;
import org.json.JSONException;
import org.json.JSONObject;

public class PlayerServlet extends HttpServlet {
  public static final String URL_PATH = "/player/*";

  /**
   * Respond to a GET request
   * 
   * @param request The HTTP Request
   * @param response The response to write
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String playerName = request.getParameter("name");
    logger.info(request.getMethod() + " " + request.getRequestURI());

    try {
      JSONObject resp = PlayerManager.getDataRequest(new JSONObject("{id:\"" + playerName + "\"}"));
      response.getWriter().write(resp.toString());
    } catch (ServiceException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  private static final long serialVersionUID = 1L;
  private static Logger logger = Logger.getLogger(PlayerServlet.class);
}