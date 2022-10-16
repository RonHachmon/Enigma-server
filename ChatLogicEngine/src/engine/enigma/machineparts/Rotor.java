package engine.enigma.machineparts;

import engine.enigma.jaxb_classes.CTEPositioning;
import engine.enigma.jaxb_classes.CTERotor;

import java.io.Serializable;
import java.util.*;

public class Rotor implements Serializable {
    private List<Line> lineArray = new ArrayList<>();
    private int notchIndex;
    private int rotatorIndex = 0;

    @Override
    protected Object clone() throws CloneNotSupportedException{
        Rotor clonedRotor = new Rotor();
        clonedRotor.lineArray = new ArrayList<>(this.lineArray);
        clonedRotor.notchIndex = this.notchIndex;
        clonedRotor.rotatorIndex = this.rotatorIndex;

        return clonedRotor;
    }
    public int getRotatorIndex() {
        return rotatorIndex;
    }

    public int distanceFromNotch() {
        int temp = notchIndex - rotatorIndex;
        if (temp < 0) {
            temp += lineArray.size();
        }

        return temp;
    }

    public void rotate() {
        rotatorIndex = (rotatorIndex + 1) % lineArray.size();
    }

    public Character currentStartingChar() {
        return lineArray.get(rotatorIndex).getRightChar();
    }

    public boolean isRotorOnNotch() {
        if (notchIndex == rotatorIndex) {
            //System.out.println("        notch");
        }
        return notchIndex == rotatorIndex;
    }

    public int getExitIndexFromRight(int index) {
        int real_index = (index + rotatorIndex) % lineArray.size();
        char right_char = lineArray.get(real_index).getRightChar();
        int rotator_exit_index = 0;
        //System.out.println("        Right char = "+right_char);

        for (Line current_line : lineArray) {
            if (current_line.getLeftChar() == right_char) {
                rotator_exit_index -= rotatorIndex;
                if (rotator_exit_index < 0) {
                    rotator_exit_index += lineArray.size();
                }
                break;
            }
            rotator_exit_index++;
        }
        return rotator_exit_index;
    }

    public int getExitIndexFromLeft(int index) {
        int real_index = (index + rotatorIndex) % lineArray.size();
        char left_char = lineArray.get(real_index).getLeftChar();
        //System.out.println("        Left char = "+left_char);

        int rotator_exit_index = 0;
        for (Line current_line : lineArray) {
            if (current_line.getRightChar() == left_char) {
                rotator_exit_index -= rotatorIndex;
                if (rotator_exit_index < 0) {
                    rotator_exit_index += lineArray.size();
                }
                break;
            }
            rotator_exit_index++;
        }
        return rotator_exit_index;
    }

    public static Rotor createRotorFromXML(CTERotor xml_rotor, String allChars) {
        Rotor rotor = new Rotor();
        if (xml_rotor.getNotch() > allChars.length() || xml_rotor.getNotch() <= 0) {
            throw new IllegalArgumentException("notch position out of bound, notch range is 1 - " + allChars.length()
                    + ", while one notch set on " + xml_rotor.getNotch());
        }
        rotor.notchIndex = xml_rotor.getNotch() - 1;
        for (CTEPositioning positioning : xml_rotor.getCTEPositioning()) {

            upperCaseInput(positioning);
            checkValidChar(rotor, allChars, positioning);
            Line current_line = new Line(positioning.getRight(), positioning.getLeft());

            rotor.lineArray.add(current_line);
        }
        if (rotor.lineArray.size() != allChars.length()) {
            throw new IllegalArgumentException("not all characters are included in the rotor");
        }
        return rotor;
    }

    private static void upperCaseInput(CTEPositioning positioning) {
        positioning.setLeft(positioning.getLeft().toUpperCase());
        positioning.setRight(positioning.getRight().toUpperCase());
    }

    private static void checkValidChar(Rotor rotor, String allChars, CTEPositioning xmlLine) {
        if (!allChars.contains(xmlLine.getRight().toUpperCase())) {
            throw new IllegalArgumentException("Invalid rotor, since '" + xmlLine.getRight() + "' on rotor but isn't on char collection");
        }
        if (!allChars.contains(xmlLine.getLeft())) {
            throw new IllegalArgumentException("Invalid rotor, since '" + xmlLine.getLeft() + "' on rotor but isn't on char collection");
        }
        for (Line line : rotor.lineArray) {
            if (xmlLine.getRight().charAt(0) == line.getRightChar()) {
                throw new IllegalArgumentException(xmlLine.getRight().charAt(0) + " appears twice on rotor");
            }
            if (xmlLine.getLeft().charAt(0) == line.getLeftChar()) {
                throw new IllegalArgumentException(xmlLine.getLeft().charAt(0) + " appears twice on rotor");
            }
        }
    }

    public void setStartingIndex(int startingIndex) {
        this.rotatorIndex = startingIndex;
    }

    public void setStartingIndex(char characterToLook) {
        for (int i = 0; i < this.lineArray.size(); i++) {
            if (this.lineArray.get(i).getRightChar() == characterToLook) {
                this.setStartingIndex(i);
                return;
            }
        }
        throw new IllegalArgumentException("starting rotor index " + characterToLook + " not on rotor");
    }

    public String toString() {
        String res = new String();
        for (Line line : lineArray) {
            res += line.getLeftChar() + " , " + line.getRightChar() + "\n";
        }
        return res;
    }
}
