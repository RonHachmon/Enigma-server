package DTO;

import engine.enigma.bruteForce2.utils.CodeConfiguration;

public class TaskDataDTO {
    private final CodeSettingDTO [] codeSettingDTO;
    private final int taskSize;
    public TaskDataDTO(CodeSettingDTO[] codeConfiguration, int taskSize) {
        this.codeSettingDTO=codeConfiguration;
        this.taskSize=taskSize;
    }

    public CodeSettingDTO[] getCodeSettingDTO() {
        return codeSettingDTO;
    }

    public int getTaskSize() {
        return taskSize;
    }
}
