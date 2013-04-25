package org.jpokemon.server;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.jpokemon.pokemon.PokemonInfo;
import org.jpokemon.service.PlayerService;
import org.jpokemon.service.ServiceException;
import org.json.JSONException;
import org.json.JSONObject;

public class PlayerServlet extends HttpServlet {
    static int zachsId = 0;

    /**
     * Respond to a GET request
     *
     * @param request The HTTP Request
     * @param response The response to write
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info(request.getMethod() + " " + request.getRequestURI());

        if (zachsId == 0) {
            try {
                zachsId = PlayerService.load("Zach");
            }
            catch (ServiceException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        PokemonInfo.get(53);

        JSONObject resp = null;
        try {
            resp = PlayerService.pull(new JSONObject("{id:\"" + zachsId + "\"}"));
        }
        catch (ServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        response.getWriter().write(resp.toString());
    }

    /**
     * Respond to a POST request
     *
     * @param request The HTTP Request
     * @param response The response to write
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info(request.getMethod() + " " + request.getRequestURI());

        response.getWriter().write("Welcome to JPokemon");
    }

    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(PlayerServlet.class);
}
