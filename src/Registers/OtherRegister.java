package Registers;

import Machine.Machine;

//use array to simulates the real OtherRegister
public class OtherRegister {
    public static int MFR_number = 10;

    public static String MFR(int number, Machine machine) {
        switch (number) {
            case 3:
                MFR_number = 10;
                return "Operation in line" + machine.PC + ": Illegal Memory Address beyond" + machine.memory.getLength();
            case 2:
                MFR_number = 10;
                return "Operation in line" + machine.PC + ": Illegal Operation Code";
            case 1:
                MFR_number = 10;
                return "Operation in line" + machine.PC + ": Illegal TRAP code";
            case 0:
                MFR_number = 10;
                return "Operation in line" + machine.PC + ": Illegal Memory Address to Reserved Locations";
        }
        return "error";
    }

    public static String MBR;
    public static int MAR;

}



