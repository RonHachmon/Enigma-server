package app.mini_apps.allies.refreshers;

import DTO.BattleStatusDTO;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import web.Constants;
import web.http.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static web.Constants.GSON_INSTANCE;


public class BattleStatusRefresher extends TimerTask {


    private final Consumer<BattleStatusDTO> battleStatusConsumer;
    private final String url;


    public BattleStatusRefresher(Consumer<BattleStatusDTO> battleStatusConsumer,String battleName) {
        this.battleStatusConsumer = battleStatusConsumer;
        url=Constants.BATTLE_STATUS+"?battleship="+battleName;
    }

    @Override
    public void run() {

        HttpClientUtil.runAsync(url, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                BattleStatusDTO battleStatusDTO =GSON_INSTANCE.fromJson(response.body().string(), BattleStatusDTO.class);

                battleStatusConsumer.accept(battleStatusDTO);
            }
        });
    }

}
