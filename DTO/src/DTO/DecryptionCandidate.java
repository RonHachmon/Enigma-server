package DTO;

public class DecryptionCandidate {
    private int agentID;
    private String decryptedString;
    private String codeConfiguration;

    public DecryptionCandidate(int agentID, String decryptedString, String codeConfiguration) {
        this.agentID = agentID;
        this.decryptedString = decryptedString;
        this.codeConfiguration = codeConfiguration;
    }

    public String getCodeConfiguration() {
        return codeConfiguration;
    }

    public void setCodeConfiguration(String codeConfiguration) {
        this.codeConfiguration = codeConfiguration;
    }

    public String getDecryptedString() {
        return decryptedString;
    }

    public void setDecryptedString(String decryptedString) {
        this.decryptedString = decryptedString;
    }

    public int getAgentID() {
        return agentID;
    }

    public void setAgentID(int agentID) {
        this.agentID = agentID;
    }
}
