package InstructionSet;

import Machine.Machine;
import Machine.Preprocessing;
import Registers.OtherRegister;

//this class is dealing with Load/Store Instructions
//decode the instruction, analysis them than retuen a data to the upper class who called it
public class LoadStore {

    //Load Register From Memory
    public static int LDR(int r, int x, int i, int address, Machine machine) {
        int EA = Preprocessing.EffectiveAddress(i, x, address, machine);
        if (machine.memory.getLength() < EA) {
            OtherRegister.MFR_number = 3;
            return 3;
        } else {
            machine.generalPurposeRegister.setGeneralPurposeRegister(r, machine.cache.useCache(EA));
            return 10;
        }
    }

    //Store Register To Memory
    public static int STR(int r, int x, int i, int address, Machine machine) {
        int EA = Preprocessing.EffectiveAddress(i, x, address, machine);
        if (machine.memory.getLength() < EA) {
            OtherRegister.MFR_number = 3;
            return 3;
        } else {
            machine.cache.writeCache(EA, machine.generalPurposeRegister.getGeneralPurposeRegister(r));
            return 10;
        }
    }

    //Load Register with Address
    public static int LDA(int r, int x, int i, int address, Machine machine) {
        int EA = Preprocessing.EffectiveAddress(i, x, address, machine);
        machine.generalPurposeRegister.setGeneralPurposeRegister(r, EA);
        return 10;
    }

    //Load Index Register from Memory
    public static int LDX(int x, int i, int address, Machine machine) {
        int EA = Preprocessing.EffectiveAddress(i, 0, address, machine);
        x = x - 1;
        if (machine.memory.getLength() < EA) {
            OtherRegister.MFR_number = 3;
            return 3;
        } else {
            machine.indexRegister.setIndexRegisters(x, machine.cache.useCache(EA));
            return 10;
        }

    }

    //Store Index Register to Memory
    public static int STX(int x, int i, int address, Machine machine) {
        int EA = Preprocessing.EffectiveAddress(i, x, address, machine);
        x = x - 1;
        if (machine.memory.getLength() < EA) {
            return 3;
        } else {
            machine.cache.writeCache(EA, machine.indexRegister.getIndexRegisters(x));
            return 10;
        }
    }
}
