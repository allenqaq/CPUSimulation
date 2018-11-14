package CacheBuilder;

public class CacheCell {
    private int memoryAddress = -1;
    private int memoryContent = -1;

    public CacheCell(int memoryAddress, int memoryContent) {
        this.memoryAddress = memoryAddress;
        this.memoryContent = memoryContent;
    }

    public int getMemoryAddress() {
        return memoryAddress;
    }

    public int getMemoryContent() {
        return memoryContent;
    }
}
