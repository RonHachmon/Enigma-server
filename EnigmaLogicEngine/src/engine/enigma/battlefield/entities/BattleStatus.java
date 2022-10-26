package engine.enigma.battlefield.entities;


public enum BattleStatus {

    IDLE("Idle"),
    INPROGRESS("In Progress"),
    DONE("Finished");


    private String message;

    private BattleStatus(String message) {
        this.message = message;
    }

    public String toString() {
        return this.message;
    }
}



