package app.mini_apps.allies.refreshers;

import DTO.AllBattles;

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


    private final Consumer<List<BattleFieldInfo>> usersListConsumer;
    private final String url=Constants.GET_ENTITIES+"?entity=battle";


    public BattleListRefresher(Consumer<List<BattleFieldInfo>> usersListConsumer) {
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
                AllBattles allBattles =GSON_INSTANCE.fromJson(response.body().string(), AllBattles.class);
                List<BattleFieldInfo> list =new ArrayList<>();
                BattleFieldInfo[] battleFields = allBattles.getBattleFields();
                Arrays.stream(allBattles.getBattleFields()).forEach(battleField -> list.add(battleField));
                usersListConsumer.accept(list);
            }
        });
    }

}
