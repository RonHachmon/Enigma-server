package engine.machineutils;

import java.io.Serializable;

public class NewStatisticInput implements Serializable {


        private String input;

        private String output;

        private Integer duration;

    public NewStatisticInput(String input, String output, Integer duration) {
        this.input = input;
        this.output = output;
        this.duration = duration;

    }


    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }


    @Override
    public String toString()
    {
        return input +" --> "+output+"("+duration.toString()+")";
    }
}


