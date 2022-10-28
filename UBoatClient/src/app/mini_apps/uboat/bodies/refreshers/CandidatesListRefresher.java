package app.mini_apps.uboat.bodies.refreshers;

import DTO.DecryptionCandidate;
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


public class CandidatesListRefresher extends TimerTask {


    private final Consumer<DecryptionCandidate[]> candidatesListConsumer;
    private final String url;


    public CandidatesListRefresher(Consumer<DecryptionCandidate[]> usersListConsumer,String battleFilendName) {
        this.candidatesListConsumer = usersListConsumer;
        url=Constants.CANDIDATES+"?entity=uboat";
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
