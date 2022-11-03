package app.mini_apps.allies.refreshers;

import DTO.AgentData;
import DTO.QueueDataDTO;
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


public class QueueDataRefresher extends TimerTask {


    private final Consumer<QueueDataDTO> queueDataDTOConsumer;
    private final String url=Constants.QUEUE_DATA;


    public QueueDataRefresher(Consumer<QueueDataDTO> usersListConsumer) {
        this.queueDataDTOConsumer = usersListConsumer;
    }

    @Override
    public void run() {

        HttpClientUtil.runAsync(url, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
               QueueDataDTO queueDataDTO =GSON_INSTANCE.fromJson(response.body().string(), QueueDataDTO.class);
                queueDataDTOConsumer.accept(queueDataDTO);
            }
        });
    }

}
