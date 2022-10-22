package chat.servlets.enigma;

import chat.utils.SessionUtils;
import engine.enigma.battlefield.BattlesManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


@WebServlet("/ready")
public class readyServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        String entityParameter = request.getParameter("entity");
        String username = SessionUtils.getUsername(request);

        if (entityParameter.equals("uboat")) {
            readyBattle(response);

        }
        if (entityParameter.equals("ally")) {
            readyAlly(response, username);

        }
    }

    private void readyAlly(HttpServletResponse response, String username) {

        BattlesManager.getInstance().readyAlly(username);

    }

    private void readyBattle(HttpServletResponse response) {
    }
}
