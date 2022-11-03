package chat.servlets.enigma;

import DTO.AgentData;
import DTO.QueueDataDTO;
import chat.utils.SessionUtils;
import com.google.gson.Gson;
import engine.enigma.battlefield.BattlesManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/queue-data")
public class QueueDataServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String username = SessionUtils.getUsername(request);

        // validate input
        QueueDataDTO queueDataDTO = BattlesManager.getInstance().getQueueData(username);


        Gson gson = new Gson();
        String jsonResponse = gson.toJson(queueDataDTO);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }

    }
}
