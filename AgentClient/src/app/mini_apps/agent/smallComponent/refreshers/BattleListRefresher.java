package app.mini_apps.agent.smallComponent.refreshers;

import DTO.AllBattles;

import DTO.AlliesArray;
import engine.enigma.battlefield.BattleFieldInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import web.Constants;
import web.http.HttpClientUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static web.Constants.GSON_INSTANCE;


public class BattleListRefresher extends TimerTask {


    private final Consumer<List<String>> usersListConsumer;
    private final String url=Constants.GET_ENTITIES+"?entity=allies";


    public BattleListRefresher(Consumer<List<String>> usersListConsumer) {
        this.usersListConsumer = usersListConsumer;

    }

    @Override
    public void run() {

        HttpClientUtil.runAsync(url, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                AlliesArray alliesArray =GSON_INSTANCE.fromJson(response.body().string(), AlliesArray.class);
                List<String> list =new ArrayList<>();
                Arrays.stream(alliesArray.getAllies()).forEach(ally -> list.add(ally));
                usersListConsumer.accept(list);
            }
        });
    }

}
