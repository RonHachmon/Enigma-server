package app.mini_apps.allies.refreshers;

import DTO.AlliesArray;
import DTO.AllyDTO;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import web.http.HttpClientUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static web.Constants.GET_ALLIES;
import static web.Constants.GSON_INSTANCE;


public class AlliesListRefresher extends TimerTask {


    private final Consumer<List<AllyDTO>> usersListConsumer;
    private final String URL;


    public AlliesListRefresher(Consumer<List<AllyDTO>> usersListConsumer, String battleArea) {
        this.usersListConsumer = usersListConsumer;
        URL=GET_ALLIES+"?battleship="+battleArea;
    }

    @Override
    public void run() {

        HttpClientUtil.runAsync(URL, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                AlliesArray alliesArray =GSON_INSTANCE.fromJson(response.body().string(), AlliesArray.class);
                List<AllyDTO> list =new ArrayList<>();
                AllyDTO[] allies = alliesArray.getAllies();
                Arrays.stream(allies).forEach(battleField -> list.add(battleField));
                usersListConsumer.accept(list);
            }
        });
    }

}
