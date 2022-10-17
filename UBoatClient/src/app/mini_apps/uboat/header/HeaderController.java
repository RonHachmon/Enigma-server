package app.mini_apps.uboat.header;


import app.mini_apps.uboat.bodies.absractScene.MainAppScene;

import engine.enigma.machineutils.MachineManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import web.http.HttpClientUtil;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;


import static web.Constants.UPLOAD_FILE;
import static web.Constants.JOIN_BATTLE;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InputStream inputStream=HeaderController.class.getResourceAsStream(COMMAND_PROMPT_TTF);
        Font font = Font.loadFont(inputStream, 26);
        titleLabel.setFont(font);
    }



    @FXML
    void contestClicked(ActionEvent event) {
        encryptButton.setDisable(true);
        machineButton.setDisable(false);
        uboatController.displayContest();

    }

    @FXML
    void machineClicked(ActionEvent event) {
        machineButton.setDisable(true);
        encryptButton.setDisable(false);
        uboatController.displayMachineConfigScene();

    }


    @FXML
    void loadXML(ActionEvent event) throws IOException {
        FileChooser fileChooser = configFileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile!=null) {
            this.fileHttpRequest(selectedFile);
        }
        this.joinBattle();

    }

    private  void joinBattle() throws IOException {

        String battleName = this.machineManager.getBattleField().getBattleName().replace(' ', '-');
        String finalUrl = HttpUrl
                .parse(JOIN_BATTLE)
                .newBuilder()
                .addQueryParameter("battleship", battleName)
                .addQueryParameter("entity","uboat")
                .build()
                .toString();


        //currently does nothing except sending the request, maybe we should add something in the future.
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

        //build body
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("enigmaFile", selectedFile.getAbsolutePath(),
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                selectedFile))
                .build();
        //build request
        Request request = new Request.Builder()
                .url(UPLOAD_FILE)
                .method("POST", body)
                .addHeader("Accept-Language", "en")
                .build();
        //sends request
        Response response = client.newCall(request).execute();
        //request send back the same file
        machineManager.createMachineFromXML(response.body().byteStream());

        updateGUI(selectedFile);

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

    public void enableContest(boolean toEnable) {
        encryptButton.setDisable(!toEnable);
    }




}