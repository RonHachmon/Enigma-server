package chat.servlets.enigma;


import DTO.TaskDataDTO;
import com.google.gson.Gson;
import engine.enigma.battlefield.BattlesManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/tasks")
public class ConfigurationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String allyName=request.getParameter("ally");
        int amountOfTasks=Integer.parseInt(request.getParameter("task"));

        TaskDataDTO tasks = BattlesManager.getInstance().getTasks(allyName, amountOfTasks);
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(tasks);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }


    }
}
