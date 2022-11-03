package chat.servlets.enigma;

import DTO.BattleStatusDTO;
import DTO.MachineInformationDTO;
import com.google.gson.Gson;
import engine.enigma.battlefield.BattleField;
import engine.enigma.battlefield.BattlesManager;
import engine.enigma.battlefield.entities.BattleStatus;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/update-status")
public class BattleStatusServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String battleShip = request.getParameter("battleship");
        String battleStatus = request.getParameter("status");
        Gson gson = new Gson();
        MachineInformationDTO machineInformationDTO =gson.fromJson(request.getReader(), MachineInformationDTO.class);
        if(battleStatus.equals("start"))
        {

            BattlesManager.getInstance().updateEncryptedWord(battleShip,machineInformationDTO.getEncryptedMessage());
            BattlesManager.getInstance().start(battleShip,machineInformationDTO);
        }
        if(battleStatus.equals("end"))
        {
            BattleField battleField = BattlesManager.getInstance().getBattleFieldByBattleName(battleShip);
            battleField.setStatus(BattleStatus.DONE);
            battleField.setWinningTeam(machineInformationDTO.getWinningTeam());
            battleField.clearAllies();

        }


    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String battleFieldName = request.getParameter("battleship");
        BattleField battleField = BattlesManager.getInstance().getBattleFieldByBattleName(battleFieldName);
        BattleStatusDTO battleStatusDTO=null;

        if(battleField!=null)
        {
            battleStatusDTO = new BattleStatusDTO();
            if (battleField.getBattleStatus() == BattleStatus.INPROGRESS) {
                battleStatusDTO.setEncryptedMessage(battleField.getEncryptedMessage());
            }
            if (battleField.getBattleStatus() == BattleStatus.DONE) {
                battleStatusDTO.setWinningAlly(battleField.getWinningTeam());
            }
            battleStatusDTO.setStatus(battleField.getBattleStatus().toString());
        }
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(battleStatusDTO);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
    }
}
