package utils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

import java.lang.reflect.Field;

public class EnigmaUtils {
    public static String convertIntToRoman(int number) {
        switch (number) {
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            default:
                throw new IllegalArgumentException("Invalid Reflector,id must between 1-5 ");
        }
    }
    public static Integer convertRomanToInt(String number)
    {
        switch (number) {
            case "I":
                return 1;
            case "II":
                return 2;
            case "III":
                return 3;
            case "IV":
                return 4;
            case "V":
                return 5;
            default:
                throw new IllegalArgumentException("Invalid Reflector,id must between 1-5 ");
        }

    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    //changes duration until tool tip is shown
    public static void hackTooltipStartTiming(Tooltip tooltip,int milliSecondDelay) {
        try {
            Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
            fieldBehavior.setAccessible(true);
            Object objBehavior = fieldBehavior.get(tooltip);

            Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
            fieldTimer.setAccessible(true);
            Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

            objTimer.getKeyFrames().clear();
            objTimer.getKeyFrames().add(new KeyFrame(new Duration(milliSecondDelay)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Integer binomial(int numOfActiveRotors, int numOfAllRotors) {
        return factorial(numOfAllRotors) / (factorial(numOfActiveRotors) * factorial(numOfAllRotors - numOfActiveRotors));
    }
    public static Integer factorial(int size) {
        if(size == 0) {
            return 1;
        }
        return size * factorial(size - 1);
    }
    public static String formatToIntWithCommas(long longNumber) {
        StringBuilder stringBuilder=new StringBuilder("");
        int count=0;
        int number;
        while (longNumber!=0)
        {
            number= (int) (longNumber%10);
            if(count%3==0&&count!=0)
            {
                stringBuilder.append(",");
            }
            stringBuilder.append(number);
            count++;
            longNumber=longNumber/10;
        }
        return stringBuilder.reverse().toString();

    }
}
