package utils;

import java.util.ArrayList;
import java.util.List;

public class ListPermutation {

    String availableIntegers="";
    Permutation permutation;
    int amountOfPermutation;
    List<Character> initialSetOfRotors=new ArrayList<>();

    public ListPermutation(List<Integer> selectedRotorsIndexes,List<Integer> allRotorsIndexes)
    {
        allRotorsIndexes.forEach((Integer s) -> availableIntegers+=s.toString());
        selectedRotorsIndexes.forEach((Integer s) -> initialSetOfRotors.add(s.toString().charAt(0)));
        permutation=new Permutation(availableIntegers);
        amountOfPermutation= calculateAmountOfPermutation(selectedRotorsIndexes, allRotorsIndexes);
    }

    private int calculateAmountOfPermutation(List<Integer> selectedRotorsIndexes, List<Integer> allRotorsIndexes) {
        return EnigmaUtils.binomial(selectedRotorsIndexes.size(), allRotorsIndexes.size()) * EnigmaUtils.factorial(selectedRotorsIndexes.size());
    }
    public boolean done()
    {
        return amountOfPermutation==0;
    }

    public List<Integer> increasePermutation()
    {
        do{
            permutation.increasePermutation(1, initialSetOfRotors);
        }while (!checkArrayIsUnique(initialSetOfRotors));
        amountOfPermutation-=1;
        List<Integer> integers=new ArrayList<>();
        initialSetOfRotors.forEach(character -> integers.add(Integer.parseInt(character.toString())));
        return integers;
    }


    private  boolean  checkArrayIsUnique(List<Character> rotorsID) {
        if (rotorsID.stream().distinct().count() != rotorsID.size()) {
            return false;
        }
        return true;
    }

}
