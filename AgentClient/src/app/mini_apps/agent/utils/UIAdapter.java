package app.mini_apps.agent.utils;


import DTO.DecryptionCandidate;
import javafx.application.Platform;

import java.util.function.Consumer;

public class UIAdapter {

    private final Consumer<String> updateProgress;
    private Consumer<DecryptionCandidate> addNewCandidate;


    private Consumer<Integer> updateTotalFoundInteger;
    private Runnable updateDistinct;
    private Runnable onDone;
    public UIAdapter(Consumer<DecryptionCandidate> addNewCandidate, Consumer<Integer> updateTotalFoundWords, Runnable updateDistinct,Runnable onDone,Consumer<String> progress) {
        this.addNewCandidate = addNewCandidate;
        this.updateTotalFoundInteger = updateTotalFoundWords;
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



    public void updateTotalFoundWords(int delta) {
        Platform.runLater(
                () -> updateTotalFoundInteger.accept(delta)
        );
    }
    public void updateProgress(String data){
        Platform.runLater(
                () -> updateProgress.accept(data)
        );

    }

}
