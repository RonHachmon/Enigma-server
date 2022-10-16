package app.mini_apps.allies.refreshers;

import DTO.AllBattles;
import app.util.Constants;

import app.util.http.HttpClientUtil;
import engine.enigma.battlefield.BattleFieldInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static app.util.Constants.GSON_INSTANCE;


public class BattleListRefresher extends TimerTask {


    private final Consumer<List<BattleFieldInfo>> usersListConsumer;


    public BattleListRefresher(Consumer<List<BattleFieldInfo>> usersListConsumer) {
        this.usersListConsumer = usersListConsumer;
    }

    @Override
    public void run() {

        HttpClientUtil.runAsync(Constants.GET_BATTLES, new Callback() {

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
