package chat.servlets.enigma;

import DTO.AllBattles;
import chat.constants.Constants;
import chat.servlets.chat.ChatServlet;
import chat.utils.ServletUtils;
import chat.utils.SessionUtils;
import com.google.gson.Gson;
import engine.chat.ChatManager;
import engine.chat.SingleChatEntry;
import engine.enigma.battlefield.BattlesManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/battles")
public class allBattlesServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        AllBattles allBattles = BattlesManager.getInstance().convertToArray();


        Gson gson = new Gson();
        String jsonResponse = gson.toJson(allBattles);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }

    }
}
