package DTO;

import engine.enigma.battlefield.Ally;

public class AllyDTO {
    private String allyName;
    private boolean isReady;

    public AllyDTO(Ally ally) {
        this.allyName = ally.getAllyName();
        this.isReady=ally.isReady();
    }

    public String getAllyName() {
        return allyName;
    }

    public void setAllyName(String allyName) {
        this.allyName = allyName;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }
}
