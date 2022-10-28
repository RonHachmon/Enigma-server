package app.mini_apps.allies.refreshers;

import DTO.AgentData;
import DTO.DecryptionCandidate;
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


public class CandidatesListRefresher extends TimerTask {


    private final Consumer<DecryptionCandidate[]> candidatesListConsumer;
    private final String url=Constants.CANDIDATES+"?entity=ally";


    public CandidatesListRefresher(Consumer<DecryptionCandidate[]> usersListConsumer) {
        this.candidatesListConsumer = usersListConsumer;
    }

    @Override
    public void run() {

        HttpClientUtil.runAsync(url, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println();
                DecryptionCandidate[] agentData =GSON_INSTANCE.fromJson(response.body().string(), DecryptionCandidate[].class);

                candidatesListConsumer.accept(agentData);
            }
        });
    }

}
