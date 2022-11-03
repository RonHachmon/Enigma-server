package app.mini_apps.agent.utils;


import DTO.DecryptionCandidate;
import engine.bruteForce2.utils.QueueData;
import javafx.application.Platform;

import java.util.function.Consumer;

public class UIAdapter {

    private final Consumer<String> updateProgress;
    private Consumer<DecryptionCandidate> addNewCandidate;

    private Consumer<QueueData> updateProgressData;
    private Runnable updateDistinct;
    private Runnable onDone;
    public UIAdapter(Consumer<DecryptionCandidate> addNewCandidate, Consumer<QueueData> updateProgressData, Runnable updateDistinct,Runnable onDone,Consumer<String> progress) {
        this.addNewCandidate = addNewCandidate;
        this.updateProgressData = updateProgressData;
        this.updateDistinct = updateDistinct;
        this.onDone=onDone;
        this.updateProgress=progress;
    }
    public void addNewCandidate(DecryptionCandidate decryptionCandidate) {
        Platform.runLater(
                () -> {
                    addNewCandidate.accept(decryptionCandidate);
                    updateDistinct.run();
                }
        );
    }

    public void done() {
        Platform.runLater(
                () -> {
                    onDone.run();
                }
        );
    }

    public void updateProgressData(QueueData queueData) {
        Platform.runLater(
                () -> updateProgressData.accept(queueData)
        );
    }

}
