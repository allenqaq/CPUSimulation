package InstructionSet;

import Machine.Machine;
import Machine.Preprocessing;


public class Transfer {
    //Jump If Zero:
    public static int JZ(int r, int x, int i, int address, Machine machine) {
        // C(R)
        int r2 = machine.generalPurposeRegister.getGeneralPurposeRegister(r);
        int EA;

        //If c(r) = 0
        // PC <- EA or c(EA), if I bit set;
        if (r2 == 0) {
            EA = Preprocessing.EffectiveAddress(i, x, address, machine) - 1000 + 1;
            // illegal address
            if (EA > machine.lineCount) {
                return 3;
            }
            machine.PC = EA - 2;
        }
        return 10;
    }
/*

    public static int JMP(int r, int x, int i, int address) {
        int[] number = new int[20];
        for (int k = 0; k < 20; k++) {
            number[k] = Integer.parseInt(Machine.inputStream.pop());
            int tmp = number[k];
            for (int o = 15; o >= 0; o--) {
                int t = tmp % 2;
                Machine.memory.memory[21 + k][o] = t;
                tmp = tmp / 2;
            }
        }

        int usernumber = Integer.parseInt(Machine.inputStream.pop());

        int[] miner = new int[20];
        for (int k = 0; k < 20; k++) {
            miner[k] = usernumber - number[k];
            int tmp = number[k];
            for (int o = 15; o >= 0; o--) {
                int t = tmp % 2;
                Machine.memory.memory[41 + k][o] = t;
                tmp = tmp / 2;
            }
            if (miner[k] < 0) {
                miner[k] = -miner[k];
            }
            tmp = number[k];
            for (int o = 15; o >= 0; o--) {
                int t = tmp % 2;
                Machine.memory.memory[61 + k][o] = t;
                tmp = tmp / 2;
            }
        }

        int min = Integer.MAX_VALUE;
        for (int k = 0; k < 20; k++) {
            if (miner[k] <= min) {
                min = miner[k];
            }
        }

        int plus = usernumber + min;
        int subtract = usernumber - min;

        for (int k = 0; k < 20; k++) {
            if (number[k] == plus || number[k] == subtract) {
                Machine.userInterface.textProgramOutputs.append(String.valueOf(number[k])+"\n");
            }
        }

        return 11;
    }
*/

    //Jump If Not Equal:
    public static int JNE(int r, int x, int i, int address, Machine machine) {
        // C(R)
        int r2 = machine.generalPurposeRegister.getGeneralPurposeRegister(r);
        int EA;

        //If c(r) != 0
        // PC <- EA or c(EA), if I bit set;
        if (r2 != 0) {
            EA = Preprocessing.EffectiveAddress(i, x, address, machine) - 1000 + 1;
            // illegal address
            if (EA > machine.lineCount) {
                return 3;
            }
            machine.PC = EA - 2;
        }
        return 10;
    }

    //Jump If Condition Code
    public static int JCC(int cc, int x0, int i0, int address, Machine machine) {
//        String[] passages = new String[6];
//        String pattern = machine.inputStream.pop();
//
//        int dot = 0;
//        int x;
//        StringBuilder sb = new StringBuilder();
//        try {
//            x = machine.fileInputStream.read();
//            while (x != -1) {
//                if (x != 46) {
//                    sb.append((char) x);
//                    x = machine.fileInputStream.read();
//                } else {
//                    passages[dot] = sb.toString();
//                    sb = new StringBuilder();
//                    dot++;
//                    x = machine.fileInputStream.read();
//                    if (x == 32) {
//                        x = machine.fileInputStream.read();
//                    }
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        int addr = 100;
//        for (int j = 0; j < 6; j++)
//            for (int k = 0; k < passages[j].length(); k++) {
//                machine.memory.setMemory(addr, passages[j].charAt(k));
//                addr++;
//            }
//
//        for (int i = 0; i < 6; i++) {
//            assert pattern != null;
//            int position = passages[i].indexOf(pattern);
//            if (position != -1) {
//                machine.textProgramOutputs.append((i + 1) + " ");
//                int n = 1;
//                for (int j = 0; j < position; j++) {
//                    if (passages[i].charAt(j) == ' ')
//                        n++;
//                }
//                machine.textProgramOutputs.append(n + "");
//            }
//        }
        return 11;
    }

    //Unconditional Jump To Address
    public static int JMA(int x, int i, int address, Machine machine) {
        int EA = Preprocessing.EffectiveAddress(i, x, address, machine) - 1000 + 1;
        if (EA > machine.lineCount) {
            return 3;
        }
        machine.PC = EA - 2;
        return 10;
    }

    //Jump and Save Return Address:
    public static int JSR(int x, int i, int address, Machine machine) {
        //R3 <- PC+1;
        int r3 = machine.PC + 2;
        int EA = Preprocessing.EffectiveAddress(i, x, address, machine) - 1000 + 1;
        // 10 -> 2 and save to r3
        machine.generalPurposeRegister.setGeneralPurposeRegister(3, r3);

        //PC <- EA    or      PC <- c(EA), if I bit set
        if (EA > machine.lineCount) {
            return 3;
        }
        machine.PC = EA - 2;
        return 10;
    }

    //Return From Subroutine w/ return code
    //as Immed portion (optional) stored
    //in the instruction address field.
    public static int RFS(int Immed, Machine machine) {
        //PC <- c(R3)
        int temp = machine.generalPurposeRegister.getGeneralPurposeRegister(3);
        if (temp > machine.lineCount) {
            return 3;
        }
        machine.PC = temp - 2;

        //R0 <- Immed
        machine.generalPurposeRegister.setGeneralPurposeRegister(0, Immed);

        return 10;
    }

    //Subtract One and Branch. R = 0..3
    public static int SOB(int r, int x, int i, int address, Machine machine) {
        //r <- c(r)
        int r2 = machine.generalPurposeRegister.getGeneralPurposeRegister(r);
        r2 = r2 - 1;
        //If c(r) > 0
        machine.generalPurposeRegister.setGeneralPurposeRegister(r, r2);

        int EA = Preprocessing.EffectiveAddress(i, x, address, machine) - 1000 + 1;
        if (EA > machine.lineCount) {
            return 3;
        }
        if (machine.generalPurposeRegister.getGeneralPurposeRegister(r) > 0) {
            machine.PC = EA - 2;
        }

        return 10;
    }

    //Jump Greater Than or Equal To:
    public static int JGE(int r, int x, int i, int address, Machine machine) {
        // C(r)
//        int r2 = machine.generalPurposeRegister.getGeneralPurposeRegister(r);
        //If c(r) >= 0
        //PC <- EA or c(EA
        int EA = Preprocessing.EffectiveAddress(i, x, address, machine) - 1000 + 1;
        if (EA > machine.lineCount) {
            return 3;
        }
        if (machine.generalPurposeRegister.getGeneralPurposeRegister(r) >= 0) {
            machine.PC = EA - 2;
        }
        return 10;
    }
}