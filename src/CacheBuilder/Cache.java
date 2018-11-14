package CacheBuilder;

import Machine.Machine;

import java.util.LinkedList;


public class Cache extends LinkedList<CacheCell> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int cellLimit = 0;
    private Machine machine;

    public Cache(int limit, Machine machine) {
        cellLimit = limit;
        this.machine = machine;
    }

    @Override
    public boolean add(CacheCell cacheCell) {
        super.add(cacheCell);
        if (size() > cellLimit) {
            CacheCell headElement = pop();
            machine.memory.setMemory(headElement.getMemoryAddress(), headElement.getMemoryContent());
        }
        return true;
    }


    public void clearCache() {
        while (size() > 0) {
            CacheCell headElement = pop();
            machine.memory.setMemory(headElement.getMemoryAddress(), headElement.getMemoryContent());
        }
    }

    public int useCache(int address) {
        for (int i = 0; i < size(); i++) {
            if (get(i).getMemoryAddress() == address) {
                CacheCell rmt = get(i);
                remove(i);
                add(rmt);
                return get(size() - 1).getMemoryContent();
            }
        }
        CacheCell addElement = new CacheCell(address, machine.memory.getMemory(address));
        add(addElement);
        return machine.memory.getMemory(address);
    }

    public void writeCache(int address, int content) {
        CacheCell rmt = new CacheCell(address, content);
        for (int i = 0; i < size(); i++) {
            if (get(i).getMemoryAddress() == address) {
                remove(i);
                break;
            }
        }
        add(rmt);
    }
}
