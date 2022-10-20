package chat.servlets.enigma;

import DTO.AgentData;
import DTO.AlliesArray;
import chat.utils.SessionUtils;
import com.google.gson.Gson;
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
@WebServlet("/agents")
public class GetAgents extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        String username = SessionUtils.getUsername(request);

        // validate input
        AgentData[] agents = BattlesManager.getInstance().getAgents(username);


        Gson gson = new Gson();
        String jsonResponse = gson.toJson(agents);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }

    }
}
