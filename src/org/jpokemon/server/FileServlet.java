package org.jpokemon.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ResourceHandler;

public class FileServlet extends HttpServlet {
	public FileServlet() {
		resourceHandler = new ResourceHandler();
		resourceHandler.setWelcomeFiles(new String[] { "login.html" });
		resourceHandler.setDirectoriesListed(false);
		resourceHandler.setCacheControl("no-store,no-cache,must-revalidate");
		resourceHandler.setResourceBase("web");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resourceHandler.handle(req.getRequestURI(), (Request) req, req, resp);

		if (!resp.isCommitted()) {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			resp.setContentType("text/html");
			resp.getWriter().println(
					"<h1>Resource Not Found</h1><br/>Sorry, the resource you've requested could not be located.");
		}
	}

	private ResourceHandler resourceHandler;
	private static final long serialVersionUID = 1L;
}