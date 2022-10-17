package web;

import com.google.gson.Gson;

public class Constants {

    // global constants

    public final static String JHON_DOE = "<Anonymous>";


    // fxml locations





    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/enigmaApp";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    public final static String SEND_CHAT_LINE = FULL_SERVER_PATH + "/pages/chatroom/sendChat";
    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/loginShortResponse";
    public final static String UPLOAD_FILE = FULL_SERVER_PATH + "/upload-file";
    public final static String JOIN_BATTLE = FULL_SERVER_PATH + "/join";

    public final static String GET_ALLIES = FULL_SERVER_PATH + "/allies";

    public final static String GET_BATTLES = FULL_SERVER_PATH + "/battles";

    public final static String UNJOIN_BATTLE = FULL_SERVER_PATH + "/unjoin";




    public final static int REFRESH_RATE = 2000;


    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();
}
