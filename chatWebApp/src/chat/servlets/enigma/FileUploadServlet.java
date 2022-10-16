package chat.servlets.enigma;

//taken from: http://www.servletworld.com/servlet-tutorials/servlet3/multipartconfig-file-upload-example.html
// and http://docs.oracle.com/javaee/6/tutorial/doc/glraq.html

import DTO.MachineInfo;
import chat.utils.SessionUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import engine.enigma.battlefield.BattlesManager;
import engine.enigma.machineutils.MachineInformation;
import engine.enigma.machineutils.MachineManager;
import jakarta.servlet.ServletException;
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

@WebServlet("/upload-file")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileUploadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        Part file = request.getPart("enigmaFile");
        MachineManager machineManager=new MachineManager();

        machineManager.createMachineFromXML(file.getInputStream());
        String username = SessionUtils.getUsername(request);
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson =builder.create();
        MachineInformation machineInformation=machineManager.getMachineInformation();
        MachineInfo machineInfo= new MachineInfo(machineInformation.getAvailableReflectors(),machineInformation.getAmountOfRotorsRequired(),machineInformation.getAmountOfRotors(),machineInformation.getAvailableChars());
        /*out.println(gson.toJson(machineInfo));*/
        out.println(readFromInputStream(file.getInputStream()));
    }


    private void printPart(Part part, PrintWriter out) {
        StringBuilder sb = new StringBuilder();
        sb
            .append("Parameter Name: ").append(part.getName()).append("\n")
            .append("Content Type (of the file): ").append(part.getContentType()).append("\n")
            .append("Size (of the file): ").append(part.getSize()).append("\n")
            .append("Part Headers:").append("\n");

        for (String header : part.getHeaderNames()) {
            sb.append(header).append(" : ").append(part.getHeader(header)).append("\n");
        }

        out.println(sb.toString());
    }

    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }

    private void printFileContent(String content, PrintWriter out) {
        out.println("File content:");
        out.println(content);
    }
}
