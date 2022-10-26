package chat.servlets.enigma;

import chat.utils.SessionUtils;
import engine.enigma.battlefield.BattlesManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


@WebServlet("/task")
public class TaskSizeServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        String taskParameter = request.getParameter("task");
        String allyName = SessionUtils.getUsername(request);
        Integer taskSize=Integer.parseInt(taskParameter);
        System.out.println(taskSize);
        BattlesManager.getInstance().updateTask(allyName,taskSize);

    }
}
