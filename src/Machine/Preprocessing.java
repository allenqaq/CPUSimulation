package Machine;

import CacheBuilder.CacheCell;
import InstructionSet.*;
import Tools.Utility;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

// this class is the core class for it decodes the instructions and calculates the EA
public class Preprocessing {

    private static void traceCache(Machine machine) {
        machine.updateCache(null, true);
        for (CacheCell cacheCell : machine.cache) {
            machine.updateCache(cacheCell, false);
        }
    }

    // decode and save the instructions into codeset array.
    public static void decode(String code, int i) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(code.getBytes())));
            String line;
            while ((line = br.readLine()) != null) {
                int p = Integer.parseInt(Integer.valueOf(String.valueOf(Integer.parseInt(line.substring(0, 6))), 2).toString());
                if (p < 20 || p == 41 || p == 42) {
                    Machine.codeSet[i][0] = line.substring(0, 6);
                    Machine.codeSet[i][1] = line.substring(6, 8);
                    Machine.codeSet[i][2] = line.substring(8, 10);
                    Machine.codeSet[i][3] = line.substring(10, 11);
                    Machine.codeSet[i][4] = line.substring(11, 16);
                } else if (p <= 25 && p >= 20) {
                    Machine.codeSet[i][0] = line.substring(0, 6);
                    Machine.codeSet[i][1] = line.substring(6, 8);
                    Machine.codeSet[i][2] = line.substring(8, 10);
                    Machine.codeSet[i][3] = line.substring(10, 16);
                } else if (p == 31 || p == 32) {
                    Machine.codeSet[i][0] = line.substring(0, 6);
                    Machine.codeSet[i][1] = line.substring(6, 8);
                    Machine.codeSet[i][2] = line.substring(8, 9);
                    Machine.codeSet[i][3] = line.substring(9, 10);
                    Machine.codeSet[i][4] = line.substring(10, 12);
                    Machine.codeSet[i][5] = line.substring(12, 16);
                }else if(p >= 33 && p <= 37){
                    Machine.codeSet[i][0] = line.substring(0, 6);
                    Machine.codeSet[i][1] = line.substring(6, 8);
                    Machine.codeSet[i][2] = line.substring(8, 9);
                    Machine.codeSet[i][3] = line.substring(9, 11);
                    Machine.codeSet[i][4] = line.substring(11, 16);
                } else if(p == 50 || p == 51){
                    Machine.codeSet[i][0] = line.substring(0, 6);
                    Machine.codeSet[i][1] = line.substring(6, 8);
                    Machine.codeSet[i][2] = line.substring(8, 9);
                    Machine.codeSet[i][3] = line.substring(9, 11);
                    Machine.codeSet[i][4] = line.substring(11, 16);
                }
                else if (p <= 63 || p >= 61) {
                    Machine.codeSet[i][0] = line.substring(0, 6);
                    Machine.codeSet[i][1] = line.substring(6, 8);
                    Machine.codeSet[i][2] = line.substring(8, 11);
                    Machine.codeSet[i][3] = line.substring(11, 16);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // analysis the instructions of load and store or ArithmeticLogical instructions.
    public static int execution(String[] code, Machine machine) {
        int p = Integer.parseInt(Integer.valueOf(String.valueOf(Integer.parseInt(code[0])), 2).toString());
        traceCache(machine);
        switch (p) {
            // load and store
            case 1:
                return LoadStore.LDR(Utility.BinaryToDecimalism(code[1]), Utility.BinaryToDecimalism(code[2]), Utility.BinaryToDecimalism(code[3]), Utility.BinaryToDecimalism(code[4]), machine);
            case 2:
                return LoadStore.STR(Utility.BinaryToDecimalism(code[1]), Utility.BinaryToDecimalism(code[2]), Utility.BinaryToDecimalism(code[3]), Utility.BinaryToDecimalism(code[4]), machine);
            case 3:
                return LoadStore.LDA(Utility.BinaryToDecimalism(code[1]), Utility.BinaryToDecimalism(code[2]), Utility.BinaryToDecimalism(code[3]), Utility.BinaryToDecimalism(code[4]), machine);
            case 41:
                return LoadStore.LDX(Utility.BinaryToDecimalism(code[2]), Utility.BinaryToDecimalism(code[3]), Utility.BinaryToDecimalism(code[4]), machine);
            case 42:
                return LoadStore.STX(Utility.BinaryToDecimalism(code[2]), Utility.BinaryToDecimalism(code[3]), Utility.BinaryToDecimalism(code[4]), machine);

            //Transfer Instructions
            case 10:
                return Transfer.JZ(Utility.BinaryToDecimalism(code[1]), Utility.BinaryToDecimalism(code[2]), Utility.BinaryToDecimalism(code[3]), Utility.BinaryToDecimalism(code[4]), machine);
            case 11:
                return Transfer.JNE(Utility.BinaryToDecimalism(code[1]), Utility.BinaryToDecimalism(code[2]), Utility.BinaryToDecimalism(code[3]), Utility.BinaryToDecimalism(code[4]), machine);
            case 12:
                return Transfer.JCC(Utility.BinaryToDecimalism(code[1]), Utility.BinaryToDecimalism(code[2]), Utility.BinaryToDecimalism(code[3]), Utility.BinaryToDecimalism(code[4]), machine);
            case 13:
                return Transfer.JMA(Utility.BinaryToDecimalism(code[2]), Utility.BinaryToDecimalism(code[3]), Utility.BinaryToDecimalism(code[4]), machine);
            case 14:
                return Transfer.JSR(Utility.BinaryToDecimalism(code[2]), Utility.BinaryToDecimalism(code[3]), Utility.BinaryToDecimalism(code[4]), machine);
            case 15:
                return Transfer.RFS(Utility.BinaryToDecimalism(code[4]), machine);
            case 16:
                return Transfer.SOB(Utility.BinaryToDecimalism(code[1]), Utility.BinaryToDecimalism(code[2]), Utility.BinaryToDecimalism(code[3]), Utility.BinaryToDecimalism(code[4]), machine);
            case 17:
                return Transfer.JGE(Utility.BinaryToDecimalism(code[1]), Utility.BinaryToDecimalism(code[2]), Utility.BinaryToDecimalism(code[3]), Utility.BinaryToDecimalism(code[4]), machine);
//            case 18:
//                Utility.PC = Utility.lineCount;
//                return Transfer.JMP(Utility.BinaryToDecimalism(code[1]), Utility.BinaryToDecimalism(code[2]), Utility.BinaryToDecimalism(code[3]), Utility.BinaryToDecimalism(code[4]));

            //Arithmetic and Logical Instructions
            case 4:
                return ArithmeticLogical.AMR(Utility.BinaryToDecimalism(code[1]), Utility.BinaryToDecimalism(code[2]), Utility.BinaryToDecimalism(code[3]), Utility.BinaryToDecimalism(code[4]), machine);
            case 5:
                return ArithmeticLogical.SMR(Utility.BinaryToDecimalism(code[1]), Utility.BinaryToDecimalism(code[2]), Utility.BinaryToDecimalism(code[3]), Utility.BinaryToDecimalism(code[4]), machine);
            case 6:
                return ArithmeticLogical.AIR(Utility.BinaryToDecimalism(code[1]), Utility.BinaryToDecimalism(code[4]), machine);
            case 7:
                return ArithmeticLogical.SIR(Utility.BinaryToDecimalism(code[1]), Utility.BinaryToDecimalism(code[4]), machine);
            case 20:
                return ArithmeticLogical.MLT(Utility.BinaryToDecimalism(code[1]), Utility.BinaryToDecimalism(code[2]), machine);
            case 21:
                return ArithmeticLogical.DVD(Utility.BinaryToDecimalism(code[1]), Utility.BinaryToDecimalism(code[2]), machine);
            case 22:
                return ArithmeticLogical.TRR(Utility.BinaryToDecimalism(code[1]), Utility.BinaryToDecimalism(code[2]), machine);
            case 23:
                return ArithmeticLogical.AND(Utility.BinaryToDecimalism(code[1]), Utility.BinaryToDecimalism(code[2]), machine);
            case 24:
                return ArithmeticLogical.ORR(Utility.BinaryToDecimalism(code[1]), Utility.BinaryToDecimalism(code[2]), machine);
            case 25:
                return ArithmeticLogical.NOT(Utility.BinaryToDecimalism(code[1]), machine);

            //Shift or Rotate Operations
            case 31:
                return ShiftRotate.SRC(Utility.BinaryToDecimalism(code[1]), Utility.BinaryToDecimalism(code[5]), Utility.BinaryToDecimalism(code[3]), Utility.BinaryToDecimalism(code[2]), machine);
            case 32:
                return ShiftRotate.RRC(Utility.BinaryToDecimalism(code[1]), Utility.BinaryToDecimalism(code[5]), Utility.BinaryToDecimalism(code[3]), machine);

            //Floating Point
            case 33:
                return Floating.FADD(Utility.BinaryToDecimalism(code[1]),Utility.BinaryToDecimalism(code[3]),Utility.BinaryToDecimalism(code[2]),Utility.BinaryToDecimalism(code[4]), machine);
            case 34:
                return Floating.FSUB(Utility.BinaryToDecimalism(code[1]),Utility.BinaryToDecimalism(code[3]),Utility.BinaryToDecimalism(code[2]),Utility.BinaryToDecimalism(code[4]), machine);
            case 35:
                return Floating.VADD(Utility.BinaryToDecimalism(code[1]),Utility.BinaryToDecimalism(code[3]),Utility.BinaryToDecimalism(code[2]),Utility.BinaryToDecimalism(code[4]), machine);
            case 36:
                return Floating.VSUB(Utility.BinaryToDecimalism(code[1]),Utility.BinaryToDecimalism(code[3]),Utility.BinaryToDecimalism(code[2]),Utility.BinaryToDecimalism(code[4]), machine);
            case 37:
                return Floating.CNVRT(Utility.BinaryToDecimalism(code[1]),Utility.BinaryToDecimalism(code[3]),Utility.BinaryToDecimalism(code[2]),Utility.BinaryToDecimalism(code[4]), machine);
            case 50:
                return Floating.LDFR(Utility.BinaryToDecimalism(code[1]),Utility.BinaryToDecimalism(code[3]),Utility.BinaryToDecimalism(code[2]),Utility.BinaryToDecimalism(code[4]), machine);
            case 51:
                return Floating.STFR(Utility.BinaryToDecimalism(code[1]),Utility.BinaryToDecimalism(code[3]),Utility.BinaryToDecimalism(code[2]),Utility.BinaryToDecimalism(code[4]), machine);


            //I/O Operations
            case 61:
                try {
                    IO.IN(Utility.BinaryToDecimalism(code[1]), Utility.BinaryToDecimalism(code[3]), machine);
                } catch (Exception e) {
                }
                return 10;
            case 62:
                try {
                    IO.OUT(Utility.BinaryToDecimalism(code[1]), Utility.BinaryToDecimalism(code[3]), machine);
                } catch (Exception e) {
                }
                return 10;
            case 63:
                IO.CHK(Utility.BinaryToDecimalism(code[1]), Utility.BinaryToDecimalism(code[3]), machine);
                return 10;
        }
        return 0;
    }


    //calculating the EA(EffectiveAddress)
    public static int EffectiveAddress(int i, int x, int address, Machine machine) {

        // I means indirect addressing
        // when i == 0 it is direct
        if (i == 0) {
            if (x == 0) {
                return address;
            } else {
//                int a = Machine.indexRegister.indexRegisters[x - 1];
                return machine.indexRegister.getIndexRegisters(x - 1) + address;
            }
            // when i == 1 it is indirect
        } else if (i == 1) {
            if (x == 0) {
                return machine.cache.useCache(address);
            } else {
//                int a = Machine.indexRegister.indexRegisters[x - 1];
                return machine.cache.useCache(machine.indexRegister.getIndexRegisters(x - 1) + address);
            }
        }
        return 0;
    }

}
