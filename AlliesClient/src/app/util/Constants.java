package app.util;

import com.google.gson.Gson;

public class Constants {

    // global constants

    public final static String JHON_DOE = "<Anonymous>";


    // fxml locations
    public final static String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/app/allies-app-main.fxml";
    public final static String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/app/components/logic/login/login.fxml";



    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/chatApp";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/loginShortResponse";
    public final static String UPLOAD_FILE = FULL_SERVER_PATH + "/upload-file";
    public final static String GET_BATTLES = FULL_SERVER_PATH + "/battles";
    public final static String JOIN_BATTLE = FULL_SERVER_PATH + "/join";
    public final static String UNJOIN_BATTLE = FULL_SERVER_PATH + "/unjoin";
    public final static int REFRESH_RATE = 2000;



    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();
}
