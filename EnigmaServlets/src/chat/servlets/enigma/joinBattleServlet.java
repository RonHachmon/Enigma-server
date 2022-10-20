package chat.servlets.enigma;

import chat.utils.SessionUtils;
import engine.enigma.battlefield.BattlesManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


//join battle
//query parameter:
// battleship - name of the battleship you wish to join
//entity - as which entity you join the battle. (uboat,ally,agent)
@WebServlet("/join")
public class joinBattleServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/plain;charset=UTF-8");
        String battleShip = request.getParameter("battleship");
        String entity = request.getParameter("entity");

        battleShip.replace('-', ' ');
        battleShip = cleanBattleName(battleShip);


        System.out.println("join: "+battleShip+" "+ entity);

        String username = SessionUtils.getUsername(request);
        System.out.println(username);
        validateInput(battleShip, entity);
        BattlesManager battlesManager=BattlesManager.getInstance();
        if(entity.equals("uboat"))
        {
            battlesManager.joinBoatToBattle(battleShip,username);
        }
        if(entity.equals("ally"))
        {
            battlesManager.joinAllyToBattle(battleShip,username);
        }
        if(entity.equals("agent"))
        {
            String allyName = request.getParameter("ally");
            allyName.replace('-', ' ');
            allyName = cleanBattleName(allyName);
        }

    }

    private  void validateInput(String battleShip, String entity) {
        if (battleShip == null) {
            battleShip = "";
        }
        if (entity == null) {
            entity = "";
        }
    }

    private  String cleanBattleName(String battleShip) {
        StringBuilder stringBuilder= new StringBuilder(battleShip);
        int temp = stringBuilder.indexOf("-");
        if(temp!=-1) {
            stringBuilder.replace(temp, temp + 1, " ");
            battleShip = stringBuilder.toString();
        }
        return battleShip;
    }


}
