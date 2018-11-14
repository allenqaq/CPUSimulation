package InstructionSet;

import Machine.Machine;
import Tools.Utility;

import java.util.Arrays;

public class ShiftRotate {
    public static int SRC(int r, int count, int direction, int mode, Machine machine) {
        int[] registerData = Utility.StringToInts(Utility.DecimalismToBinary(machine.generalPurposeRegister.getGeneralPurposeRegister(r)));
        if (direction == 1) {
            // Left
            if (mode == 1) {
                // Logically
                System.arraycopy(registerData, count, registerData, 0, registerData.length - count);
                for (int i = 0; i < count; i++)
                    registerData[registerData.length - i - 1] = 0;
            } else {
                // Arithmetically
                System.arraycopy(registerData, count + 1, registerData, 1, registerData.length - count - 1);
                for (int i = 0; i < count; i++)
                    registerData[registerData.length - i - 1] = 0;
            }
        } else {
            // Right
            if (mode == 1) {
                // Logically
                System.arraycopy(registerData, 0, registerData, count, registerData.length - count);
                for (int i = 0; i < count; i++)
                    registerData[i] = 0;
            } else {
                // Arithmetically
                System.arraycopy(registerData, 1, registerData, count + 1, registerData.length - count);
                for (int i = 0; i < count - 1; i++)
                    registerData[i + 1] = 0;
            }
        }
        machine.generalPurposeRegister.setGeneralPurposeRegister(r, Utility.BinaryToDecimalism(Utility.replace(Arrays.toString(registerData))));
        return 10;
    }

    public static int RRC(int r, int count, int direction, Machine machine) {
        int[] registerData = Utility.StringToInts(Utility.DecimalismToBinary(machine.generalPurposeRegister.getGeneralPurposeRegister(r)));
        if (direction == 1) {
            // Left
            int[] temp = new int[count];
            System.arraycopy(registerData, 0, temp, 0, count);
            System.arraycopy(registerData, count, registerData, 0, registerData.length - count);
            System.arraycopy(temp, 0, registerData, registerData.length - count, count);
        } else {
            // Right
            int[] temp = new int[count];
            System.arraycopy(registerData, registerData.length - count, temp, 0, count);
            System.arraycopy(registerData, 0, registerData, count, registerData.length - count);
            System.arraycopy(temp, 0, registerData, 0, count);
        }
        machine.generalPurposeRegister.setGeneralPurposeRegister(r, Utility.BinaryToDecimalism(Utility.replace(Arrays.toString(registerData))));
        return 10;
    }
}
