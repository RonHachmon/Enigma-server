package chat.servlets.enigma;

//taken from: http://www.servletworld.com/servlet-tutorials/servlet3/multipartconfig-file-upload-example.html
// and http://docs.oracle.com/javaee/6/tutorial/doc/glraq.html


import chat.utils.SessionUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import engine.enigma.battlefield.BattlesManager;
import engine.enigma.machineutils.MachineInformation;
import engine.enigma.machineutils.MachineManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import utils.EnigmaUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Scanner;



//upload file , send it back at response for debugging
@WebServlet("/upload-file")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileUploadServlet extends HttpServlet {
    private static Scanner scannerOut=new Scanner(System.in);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        Part file = request.getPart("enigmaFile");
        MachineManager machineManager=new MachineManager();

        machineManager.createMachineFromXML(file.getInputStream());
        String username = SessionUtils.getUsername(request);
        System.out.println("File upload: "+ username);
        BattlesManager.getInstance().addFile(file.getInputStream(),machineManager.getBattleField().getBattleName());

        /*out.println(gson.toJson(machineInfo));*/
        out.println(readFromInputStream(file.getInputStream()));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        String allyName=request.getParameter("ally");
        InputStream machineFile = BattlesManager.getInstance().getFile(allyName);
        ServletOutputStream outputStream = response.getOutputStream();
        byte[] buffer = new byte[10240];


        int amountRead=machineFile.read(buffer);
        while(amountRead>0)
        {
            outputStream.write(buffer, 0, amountRead);
            amountRead=machineFile.read(buffer);
        }
        machineFile.reset();


/*        PrintWriter out = response.getWriter();
        out.println(outputStream);*/

    }





    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }

    private void printFileContent(String content, PrintWriter out) {
        out.println("File content:");
        out.println(content);
    }
}
