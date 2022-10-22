package chat.servlets.enigma;

import DTO.AlliesArray;
import DTO.AllyDTO;
import com.google.gson.Gson;
import engine.enigma.battlefield.Ally;
import engine.enigma.battlefield.BattlesManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.List;


//gets all allies per battle
//query parameter:
// battleship - name of the battleship that the allies connected to
@WebServlet("/allies")
public class GetAllies extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        String battleShip = request.getParameter("battleship");
        System.out.println(battleShip);
        // validate input
        List<Ally> allies = BattlesManager.getInstance().getAllies(battleShip);
        AlliesArray alliesArray=new AlliesArray(allies);
        System.out.println(allies.size());
        /*AlliesArray alliesArray=new AlliesArray(allies)*/;


        Gson gson = new Gson();
        String jsonResponse = gson.toJson(alliesArray);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }

    }
}
