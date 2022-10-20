package chat.servlets.enigma;

import DTO.AgentData;
import com.google.gson.Gson;
import engine.enigma.battlefield.BattlesManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/agent")
public class addAgentServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        AgentData agentData =gson.fromJson(request.getReader(), AgentData.class);
        if(agentData!=null)
        {
            System.out.println(agentData.getAgentName());
            BattlesManager.getInstance().joinAgentToBattle(agentData);
        }



    }
}
