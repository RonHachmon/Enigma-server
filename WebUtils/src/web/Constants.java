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

    public final static String GET_ENTITIES = FULL_SERVER_PATH + "/entities";

    public final static String UNJOIN_BATTLE = FULL_SERVER_PATH + "/unjoin";
    public final static String AGENTS_DATA = FULL_SERVER_PATH + "/agent";
    public final static String BATTLE_DATA = FULL_SERVER_PATH + "/battles";

    public final static String READY = FULL_SERVER_PATH + "/ready";



    public final static String ALLY_TASK_SIZE = FULL_SERVER_PATH + "/task";
    public final static String BATTLE_STATUS = FULL_SERVER_PATH + "/update-status";

    public final static String CANDIDATES = FULL_SERVER_PATH + "/candidate";


    public final static String CONFIGURATION_TASKS = FULL_SERVER_PATH +"/tasks";

    public final static String QUEUE_DATA = FULL_SERVER_PATH +"/queue-data";







    public final static int REFRESH_RATE = 2000;


    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();
}
