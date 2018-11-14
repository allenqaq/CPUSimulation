package Memory;

/**
 * Created by Icarus on 16/09/2016.
 */
// use array to simulates the real memory
public class Memory {
    private int[] memory = null;

    public Memory(int bits) {
        this.memory = new int[bits];
    }

    public int getLength() {
        return memory.length;
    }

    public void setMemory(int index, int content) {
        memory[index] = content;
    }

    public int getMemory(int index) {
        return memory[index];
    }
}
