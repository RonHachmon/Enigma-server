package chat.servlets.enigma;

import DTO.AlliesArray;
import com.google.gson.Gson;
import engine.enigma.battlefield.BattleField;
import engine.enigma.battlefield.BattlesManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;


@WebServlet("/allies")
public class AlliesServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        String battleShip = request.getParameter("battleship");
        // validate input
        List<String> allies = BattlesManager.getInstance().getAllies(battleShip);
        AlliesArray alliesArray=new AlliesArray(allies);


        Gson gson = new Gson();
        String jsonResponse = gson.toJson(alliesArray);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }

    }
}
