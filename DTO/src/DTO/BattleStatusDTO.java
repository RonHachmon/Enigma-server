package DTO;

public class BattleStatusDTO {
    private String status;
    private String encryptedMessage;

    public String getWinningAlly() {
        return winningAlly;
    }

    public void setWinningAlly(String winningAlly) {
        this.winningAlly = winningAlly;
    }

    private String winningAlly;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEncryptedMessage() {
        return encryptedMessage;
    }

    public void setEncryptedMessage(String encryptedMessage) {
        this.encryptedMessage = encryptedMessage;
    }
}
