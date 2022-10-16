package DTO;

import engine.enigma.bruteForce2.utils.DifficultyLevel;

public class DMData {
    private int amountOfAgents;
    private DifficultyLevel difficulty;
    private int assignmentSize;
    private String encryptedString;

    public int getAmountOfAgents() {
        return amountOfAgents;
    }

    public void setAmountOfAgents(int amountOfAgents) {
        this.amountOfAgents = amountOfAgents;
    }

    public int getAssignmentSize() {
        return assignmentSize;
    }

    public void setAssignmentSize(int assignmentSize) {
        this.assignmentSize = assignmentSize;
    }

    public DifficultyLevel getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(DifficultyLevel difficulty) {
        this.difficulty = difficulty;
    }

    public String getEncryptedString() {
        return encryptedString;
    }

    public void setEncryptedString(String encryptedString) {
        this.encryptedString = encryptedString;
    }
}
