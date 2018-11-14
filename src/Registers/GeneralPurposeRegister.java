package Registers;

//use array to simulates the real GeneralPurposeRegister
public class GeneralPurposeRegister {
    private int[] generalPurposeRegister = null;

    public GeneralPurposeRegister() {
        generalPurposeRegister = new int[4];
    }

    public void setGeneralPurposeRegister(int index, int content) {
        generalPurposeRegister[index] = content;
    }

    public int getGeneralPurposeRegister(int index) {
        return generalPurposeRegister[index];
    }
}
