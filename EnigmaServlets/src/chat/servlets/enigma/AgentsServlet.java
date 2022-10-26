package chat.servlets.enigma;

import DTO.AgentData;
import chat.utils.SessionUtils;
import com.google.gson.Gson;
import engine.enigma.battlefield.BattlesManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/agent")
public class AgentsServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        AgentData agentData =gson.fromJson(request.getReader(), AgentData.class);
        if(agentData!=null)
        {
            System.out.println(agentData.getAgentName());
            BattlesManager.getInstance().joinAgentToBattle(agentData);
        }
    }
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
