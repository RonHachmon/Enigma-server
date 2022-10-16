package utils;

import java.util.List;

public class Permutation {
    private String allAvailableChars;
    private int allAvailableCharsSize;
    private boolean overFlow=false;


    public Permutation(String allAvailableChars)
    {
        this.allAvailableChars=allAvailableChars;
        this.allAvailableCharsSize=allAvailableChars.length();

    }

    public void increasePermutation(int amountToIncrease,List<Character> current)
    {
        int carry=0;
        int add=0;
        for (int i=current.size()-1;i>=0;i--)
        {
            add=amountToIncrease%allAvailableCharsSize;
            amountToIncrease=amountToIncrease/allAvailableCharsSize;
            int currentIndex = allAvailableChars.indexOf(current.get(i));
            add=currentIndex+add+carry;
            if (add>=allAvailableCharsSize)
            {
                add=add%allAvailableCharsSize;
                carry=1;
            }
            else
            {
                carry=0;
            }
            current.set(i,allAvailableChars.charAt(add));

        }
    }
    public String  increasePermutation(int amountToIncrease,String current)
    {
        int carry=0;
        int add=0;
        StringBuilder stringBuilder= new StringBuilder(current);
        for (int i=current.length()-1;i>=0;i--)
        {
            add=amountToIncrease%allAvailableCharsSize;
            amountToIncrease=amountToIncrease/allAvailableCharsSize;
            int currentIndex = allAvailableChars.indexOf(stringBuilder.charAt(i));
            add=currentIndex+add+carry;
            if (add>=allAvailableCharsSize)
            {
                add=add%allAvailableCharsSize;
                carry=1;
            }
            else
            {
                carry=0;
            }
            stringBuilder.setCharAt(i,allAvailableChars.charAt(add));

        }
        if(carry==1||amountToIncrease>0)
        {
            overFlow=true;
        }
        return stringBuilder.toString();
    }
    public boolean isOverFlow()
    {
        return this.overFlow;
    }

    public void cleanOverFLow()
    {
        this.overFlow=false;
    }


}
