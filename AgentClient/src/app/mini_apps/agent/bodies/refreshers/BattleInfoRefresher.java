package app.mini_apps.agent.bodies.refreshers;


import DTO.BattleFieldInfoDTO;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import web.http.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static web.Constants.*;


public class BattleInfoRefresher extends TimerTask {


    private final Consumer<BattleFieldInfoDTO> battleFieldInfoDTOConsumer;
    private final String URL;


    public BattleInfoRefresher(Consumer<BattleFieldInfoDTO> usersListConsumer, String allyName) {
        this.battleFieldInfoDTOConsumer = usersListConsumer;
        URL=BATTLE_DATA+"?ally="+allyName;
    }

    @Override
    public void run() {

        HttpClientUtil.runAsync(URL, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                BattleFieldInfoDTO battleFieldInfoDTO =GSON_INSTANCE.fromJson(response.body().string(), BattleFieldInfoDTO.class);
                battleFieldInfoDTOConsumer.accept(battleFieldInfoDTO);
            }
        });
    }

}
