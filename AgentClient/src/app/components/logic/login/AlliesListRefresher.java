package app.components.logic.login;

import DTO.AlliesArray;
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

import static web.Constants.*;


public class AlliesListRefresher extends TimerTask {


    private final Consumer<List<String>> usersListConsumer;
    private final String URL;


    public AlliesListRefresher(Consumer<List<String>> usersListConsumer) {
        this.usersListConsumer = usersListConsumer;
        URL=GET_ENTITIES+"?entity=allies";
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
                List<String> list =new ArrayList<>();
                String[] allies = alliesArray.getAllies();
                Arrays.stream(allies).forEach(battleField -> list.add(battleField));
                usersListConsumer.accept(list);
            }
        });
    }

}
