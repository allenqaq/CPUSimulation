package InstructionSet;

import Machine.Machine;
import Machine.Preprocessing;
import Registers.OtherRegister;

// this class is dealing with Arithmetic and Logical Instructions
// decode the instruction, analysis them than retuen a data to the upper class who called it
public class ArithmeticLogical {

    // Add Memory To Register
    public static int AMR(int r, int x, int i, int address, Machine machine) {
        int EA = Preprocessing.EffectiveAddress(i, x, address,machine);
        if (machine.memory.getLength() < EA) {
            OtherRegister.MFR_number = 3;
            return 3;
        } else {
            int EA1 = machine.cache.useCache(EA);
            int r2 = machine.generalPurposeRegister.getGeneralPurposeRegister(r);
            int re = EA1 + r2;
            machine.generalPurposeRegister.setGeneralPurposeRegister(r, re);
            return 10;
        }
    }

    //Subtract Memory From Register
    public static int SMR(int r, int x, int i, int address, Machine machine) {
        int EA = Preprocessing.EffectiveAddress(i, x, address,machine);
        if (machine.memory.getLength() < EA) {
            OtherRegister.MFR_number = 3;
            return 3;
        } else {
            int EA1 = machine.cache.useCache(EA);
            int r2 = machine.generalPurposeRegister.getGeneralPurposeRegister(r);
            int re = r2 - EA1;
            machine.generalPurposeRegister.setGeneralPurposeRegister(r, re);
            return 10;
        }
    }

    //Add  Immediate to Register 
    public static int AIR(int r, int immed, Machine machine) {
        if (immed == 0) {
            return 10;
        } else {
            int r2 = machine.generalPurposeRegister.getGeneralPurposeRegister(r);
            int re = r2 + immed;
            machine.generalPurposeRegister.setGeneralPurposeRegister(r, re);
            return 10;
        }

    }

    //Subtract  Immediate  from Registe
    public static int SIR(int r, int immed, Machine machine) {
        if (immed == 0) {
            return 10;
        } else {
            int re = machine.generalPurposeRegister.getGeneralPurposeRegister(r) - immed;
            machine.generalPurposeRegister.setGeneralPurposeRegister(r, re);
            return 10;
        }
    }


    public static int MLT(int x0, int y0, Machine machine) {
        int xValue = machine.generalPurposeRegister.getGeneralPurposeRegister(x0);
        int yValue = machine.generalPurposeRegister.getGeneralPurposeRegister(y0);
        int result = xValue * yValue;
        machine.generalPurposeRegister.setGeneralPurposeRegister(x0 + 1, result % 65536);
        machine.generalPurposeRegister.setGeneralPurposeRegister(x0, result / 65536);
        return 10;
    }

    public static int DVD(int x0, int y0, Machine machine) {
        int xValue = machine.generalPurposeRegister.getGeneralPurposeRegister(x0);
        int yValue = machine.generalPurposeRegister.getGeneralPurposeRegister(y0);
        int result = xValue / yValue;
        machine.generalPurposeRegister.setGeneralPurposeRegister(x0 + 1, result % 65536);
        machine.generalPurposeRegister.setGeneralPurposeRegister(x0, result / 65536);
        return 10;
    }


    public static int TRR(int x0, int y0, Machine machine) {
        if (machine.generalPurposeRegister.getGeneralPurposeRegister(x0) != machine.generalPurposeRegister.getGeneralPurposeRegister(y0)) {
            machine.conditionCodes[3] = 0;
            return 20;
        }
        machine.conditionCodes[3] = 1;
        return 10;

    }

    public static int AND(int x0, int y0, Machine machine) {
        machine.generalPurposeRegister.setGeneralPurposeRegister(x0, machine.generalPurposeRegister.getGeneralPurposeRegister(x0) & machine.generalPurposeRegister.getGeneralPurposeRegister(y0));
        return 10;
    }

    public static int ORR(int x0, int y0, Machine machine) {
        machine.generalPurposeRegister.setGeneralPurposeRegister(x0, machine.generalPurposeRegister.getGeneralPurposeRegister(x0) | machine.generalPurposeRegister.getGeneralPurposeRegister(y0));
        return 10;

    }

    public static int NOT(int x0, Machine machine) {
        machine.generalPurposeRegister.setGeneralPurposeRegister(x0, ~machine.generalPurposeRegister.getGeneralPurposeRegister(x0));
        return 10;
    }
}

