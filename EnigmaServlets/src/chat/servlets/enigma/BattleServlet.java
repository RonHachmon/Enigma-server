package chat.servlets.enigma;


import DTO.BattleFieldInfoDTO;
import com.google.gson.Gson;
import engine.enigma.battlefield.BattleField;
import engine.enigma.battlefield.BattlesManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/battles")
public class BattleServlet extends HttpServlet {

    //return battle data that the ally connect to
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        // validate input
        String allyName = request.getParameter("ally");
        BattleField battleField = BattlesManager.getInstance().getBattleFieldByAllyName(allyName);
        BattleFieldInfoDTO battleFieldInfoDTO=null;
        if(battleField!=null) {
             battleFieldInfoDTO = new BattleFieldInfoDTO(battleField.getBattleFieldInfo());
        }

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(battleFieldInfoDTO);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }

    }
}
