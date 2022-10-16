package app.mini_apps.uboat.header;


import DTO.MachineInfo;
import app.mini_apps.uboat.bodies.absractScene.MainAppScene;
import app.util.Constants;
import app.util.http.HttpClientUtil;
import engine.enigma.machineutils.MachineInformation;
import engine.enigma.machineutils.MachineManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import static app.util.Constants.*;

public class HeaderController extends MainAppScene implements Initializable   {
    public static final String COMMAND_PROMPT_TTF = "/resources/fonts/windows_command_prompt.ttf";

    @FXML
    private Button machineButton;

    @FXML
    private Button encryptButton;



    @FXML
    private TextField currentPath;

    @FXML
    private Label titleLabel;



    @FXML
    void encryptClicked(ActionEvent event) {
        encryptButton.setDisable(true);
        machineButton.setDisable(false);
        uboatController.displayEncrypt();

    }

    @FXML
    void machineClicked(ActionEvent event) {
        machineButton.setDisable(true);
        encryptButton.setDisable(false);
        uboatController.displayMachineConfigScene();

    }


    @FXML
    void loadXML(ActionEvent event) throws IOException {

/*        machineManager.createMachineFromXML("/ex2-basic.xml");
        this.currentPath.setText("test_files/ex2-basic.xml");

        mainAppController.resetAll();
        mainAppController.updateAllControllers();*/


        FileChooser fileChooser = configFileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile!=null) {
            this.fileHttpRequest(selectedFile);
        }
        this.joinBattle();


        String finalUrl = HttpUrl
                .parse(Constants.SEND_CHAT_LINE)
                .newBuilder()
                .addQueryParameter("userstring", "Hey :D")
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
              /*  httpStatusUpdate.updateHttpLine("Attempt to send chat line [" + chatLine + "] request ended with failure...:(");*/
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    /*httpStatusUpdate.updateHttpLine("Attempt to send chat line [" + chatLine + "] request ended with failure. Error code: " + response.code());*/
                }
            }
        });
    }

    private  void joinBattle() throws IOException {
        String battleName = this.machineManager.getBattleField().getBattleName().replace(' ', '-');
        String url = jOIN_BATTLE+"?"+"battleship="+battleName+"&"+"entity="+"uboat";

        String finalUrl = HttpUrl
                .parse(url)
                .newBuilder()
                .addQueryParameter("battleship", battleName)
                .addQueryParameter("entity","uboat")
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                /*  httpStatusUpdate.updateHttpLine("Attempt to send chat line [" + chatLine + "] request ended with failure...:(");*/
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    /*httpStatusUpdate.updateHttpLine("Attempt to send chat line [" + chatLine + "] request ended with failure. Error code: " + response.code());*/
                }
            }
        });
    }

    private  void fileHttpRequest(File selectedFile) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("enigmaFile", "/C:/Users/97254/IdeaProjects/Enigma-Machine/test_files/ex2-basic.xml",
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                selectedFile))
                .build();
        Request request = new Request.Builder()
                .url(UPLOAD_FILE)
                .method("POST", body)
                .addHeader("Accept-Language", "en")
                .build();
        Response response = client.newCall(request).execute();
        machineManager.createMachineFromXML(response.body().byteStream());
        updateGUI(selectedFile);



/*        InputStream inputStream=GSON_INSTANCE.fromJson(response.body().string(), InputStream.class);
        machineManager.createMachineFromXML(inputStream);
        System.out.println(machineManager.getMachineInformation().getAmountOfRotorsRequired());*/
/*        MachineInfo machineInfo =GSON_INSTANCE.fromJson(response.body().string(), MachineInfo.class);
        MachineInformation machineInformationFromServer= new MachineInformation(machineInfo);
        this.uboatController.updateMachineInformation(machineInformationFromServer);*/
    }

    private void updateGUI(File selectedFile) {
        this.encryptButton.setDisable(true);
        this.currentPath.setText(selectedFile.getAbsolutePath());
        uboatController.resetAll();
        uboatController.updateAllControllers();
        this.machineButton.setDisable(true);
    }


    private FileChooser configFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload File Path");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML", "*.xml"),
                new FileChooser.ExtensionFilter("ALL FILES", "*.*")
        );
        return fileChooser;
    }

    public void setMachineManager(MachineManager machineManager) {
        this.machineManager = machineManager;
    }

    public void enableEncrypt(boolean toEnable) {
        encryptButton.setDisable(!toEnable);
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InputStream inputStream=HeaderController.class.getResourceAsStream(COMMAND_PROMPT_TTF);
        Font font = Font.loadFont(inputStream, 26);
        titleLabel.setFont(font);
    }
}