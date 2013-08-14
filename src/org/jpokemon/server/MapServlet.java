package org.jpokemon.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.jpokemon.overworld.Map;

public class MapServlet extends HttpServlet {
  public MapServlet() {
    resourceHandler = new ResourceHandler();
    resourceHandler.setDirectoriesListed(false);
    resourceHandler.setCacheControl(FileServlet.cache);
    resourceHandler.setResourceBase(Map.mappath);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String modifiedPath = req.getRequestURI();
    modifiedPath = modifiedPath.substring(modifiedPath.lastIndexOf('/') + 1);

    resourceHandler.handle(modifiedPath, (Request) req, req, resp);

    // if (!resp.isCommitted()) {
    // resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    // resp.setContentType("text/html");
    // resp.getWriter().println("<h1>Resource Not Found</h1><br/>Sorry, the resource you've requested could not be located.");
    // }
  }

  private ResourceHandler resourceHandler;
  private static final long serialVersionUID = 1L;
}