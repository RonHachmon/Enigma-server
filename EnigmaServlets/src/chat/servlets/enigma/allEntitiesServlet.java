package chat.servlets.enigma;

import DTO.AllBattles;
import DTO.AlliesArray;
import com.google.gson.Gson;
import engine.enigma.battlefield.BattlesManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;


//gets all battle set in the system
@WebServlet("/entities")
public class allEntitiesServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        String entityParameter=request.getParameter("entity");

        if(entityParameter.equals("battle"))
        {
            sendBattles(response);

        }
        if(entityParameter.equals("allies"))
        {
            sendAllies(response);

        }
    }

    //send all Allies
    private static void sendAllies(HttpServletResponse response) throws IOException {
        AlliesArray alliesArray = BattlesManager.getInstance().getAllAllies();


        Gson gson = new Gson();
        String jsonResponse = gson.toJson(alliesArray);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
    }
    //send all battles
    private static void sendBattles(HttpServletResponse response) throws IOException {
        AllBattles allBattles = BattlesManager.getInstance().convertToArray();


        Gson gson = new Gson();
        String jsonResponse = gson.toJson(allBattles);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
    }
}
