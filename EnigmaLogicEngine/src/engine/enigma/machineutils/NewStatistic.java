package engine.enigma.machineutils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewStatistic implements Serializable {
    private final Map<String, ArrayList<NewStatisticInput>> statistic = new HashMap<>();
    private int totalEncryptedWordCounter=0;

    public ArrayList<NewStatisticInput> getStatsPerCode(String code)
    {
        return statistic.get(code);

    }
    public void addStatistic(String code, String input,String output,Integer duration)
    {
        totalEncryptedWordCounter++;
        statistic.computeIfAbsent(code,s -> new ArrayList<>());
        statistic.get(code).add(new NewStatisticInput(input,output,duration) );

    }
    public void printMap()
    {
        for (String code: statistic.keySet()) {
            String key = code;
            ArrayList<NewStatisticInput> value = statistic.get(code);
        }
    }
    public int getTotalEncryptedWordCounter(){
        return totalEncryptedWordCounter;
    }
}
