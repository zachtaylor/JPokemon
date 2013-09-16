package org.jpokemon.server;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.zachtaylor.myna.Myna;

/**
 * Main server component of the entire project. Contains the server and all of its configuration, and assigns the servlets and filtering, as well as contexts,
 * etc.
 * 
 * @author Graham
 */
public class JPokemonServer {
  public static int port = 9001;

  public static String savepath = "data/save";

  public static String imagepath = "data/image";

  public static String scriptedbattlepath = "data/trainer";

  public static String databasepath = "data/Pokemon.db";

  static {
    Myna.configure(JPokemonServer.class, "org.jpokemon.server");
  }

  public static void main(String[] args) throws Exception {
    BasicConfigurator.configure();
    PlayerManager.bootstrapServices();

    logger.trace("Spinning up servlets");
    ServletContextHandler root = new ServletContextHandler();
    root.setContextPath("/");
    root.addServlet(new ServletHolder(new FileServlet()), "/*");
    root.addServlet(new ServletHolder(new MapServlet()), "/map/*");
    root.addServlet(new ServletHolder(new JPokemonWebSocketServlet()), "/socket");

    // Instantiate the server on the specified port
    logger.info("Server starting on port " + port);
    Server server = new Server(port);
    server.setHandler(root);
    server.start();
    server.join();
  }

  private static Logger logger = Logger.getLogger(JPokemonServer.class);
}