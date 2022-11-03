package chat.servlets.enigma;

import DTO.AlliesArray;
import DTO.DecryptionCandidate;
import chat.utils.SessionUtils;
import com.google.gson.Gson;
import engine.enigma.battlefield.BattlesManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;

import java.io.PrintWriter;
import java.util.List;

@WebServlet("/candidate")
public class CandidateServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        String allyName=request.getParameter("ally");
        String agentName = SessionUtils.getUsername(request);

        DecryptionCandidate[] decryptionCandidates =gson.fromJson(request.getReader(), DecryptionCandidate[].class);
        System.out.println(agentName);
        BattlesManager.getInstance().addAmountOfCandidate(allyName,agentName,decryptionCandidates.length);
         BattlesManager.getInstance().addCandidates(allyName,decryptionCandidates);
         /*BattlesManager.getInstance().addAmountOfCandidate(allyName,agentName,decryptionCandidates.length);*/
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String entityParameter=request.getParameter("entity");
        String username = SessionUtils.getUsername(request);
        if(entityParameter.equals("uboat"))
        {
            sendBattleCandidates(response,username);

        }
        if(entityParameter.equals("ally"))
        {
            sendAllyCandidates(response,username);

        }

        Gson gson = new Gson();
        String allyName=request.getParameter("ally");
        DecryptionCandidate[] decryptionCandidates =gson.fromJson(request.getReader(), DecryptionCandidate[].class);
        BattlesManager.getInstance().addCandidates(allyName,decryptionCandidates);
    }
    private static void sendAllyCandidates(HttpServletResponse response, String username) throws IOException {
        List<DecryptionCandidate> allyCandidates = BattlesManager.getInstance().getAllyCandidates(username);
        int CandidatesSize=0;
        if(allyCandidates!=null)
        {
            CandidatesSize=allyCandidates.size();
        }
        DecryptionCandidate[] decryptionCandidates=new DecryptionCandidate[CandidatesSize];
        for (int i = 0; i < CandidatesSize; i++) {
            decryptionCandidates[i]=allyCandidates.get(i);
        }

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(decryptionCandidates);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
    }
    private static void sendBattleCandidates(HttpServletResponse response, String username) throws IOException {
        List<DecryptionCandidate> allyCandidates = BattlesManager.getInstance().getUboatCandidates(username);
        DecryptionCandidate[] decryptionCandidates=new DecryptionCandidate[allyCandidates.size()];
        for (int i = 0; i < allyCandidates.size(); i++) {
            decryptionCandidates[i]=allyCandidates.get(i);
        }

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(decryptionCandidates);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
    }


}
