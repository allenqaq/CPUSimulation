package Registers;

//use array to simulates the real IndexRegister
public class IndexRegister {
    private int[] indexRegisters = null;

    public IndexRegister() {
        indexRegisters = new int[3];
    }

    public void setIndexRegisters(int index, int content) {
        indexRegisters[index] = content;
    }

    public int getIndexRegisters(int index) {
        return indexRegisters[index];
    }
}
