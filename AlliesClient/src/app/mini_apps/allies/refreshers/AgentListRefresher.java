package app.mini_apps.allies.refreshers;

import DTO.AgentData;
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


public class AgentListRefresher extends TimerTask {


    private final Consumer<List<AgentData>> usersListConsumer;
    private final String url=Constants.GET_AGENTS+"?entity=battle";


    public AgentListRefresher(Consumer<List<AgentData>> usersListConsumer) {
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
                AgentData[] agentData =GSON_INSTANCE.fromJson(response.body().string(), AgentData[].class);
                List<AgentData> list =new ArrayList<>();

                Arrays.stream(agentData).forEach(agent -> list.add(agent));
                usersListConsumer.accept(list);
            }
        });
    }

}
