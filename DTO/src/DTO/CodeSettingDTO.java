package DTO;



public class CodeSettingDTO {


    private String allChars;
    private int amountOfRotors;
    private int [] selectedRotor;

    private int selectedReflector;
    private int amountOf;
    private String encryptedMessage;

    public String getAllChars() {
        return allChars;
    }



    public String getEncryptedMessage() {
        return encryptedMessage;
    }

    public void setAllChars(String allChars) {
        this.allChars = allChars;
    }


}
