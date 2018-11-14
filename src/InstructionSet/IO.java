package InstructionSet;

import Machine.Machine;
import Tools.Utility;

import java.util.Arrays;

public class IO {
    public static void IN(int r0, int deviceID, Machine machine) throws Exception {
        int inputInteger = -1;
        if (deviceID == 0 && machine.inputStream.size() == 0)
            throw new Exception("Exception happened during IN");
        else if (deviceID == 0 || deviceID == 2) {
            if (deviceID == 0)
                inputInteger = Integer.valueOf(machine.inputStream.pop());
            if (deviceID == 2)
                inputInteger = machine.fileInputStream.read();
        } else
            throw new Exception("Device ID illegal");
        machine.generalPurposeRegister.setGeneralPurposeRegister(r0, inputInteger);
    }

    public static void OUT(int r, int deviceID, Machine machine) throws Exception {
        if (deviceID != 1) {
            throw new Exception("Device ID illegal");
        } else {
            machine.registerOutput(Utility.DecimalismToBinary(machine.generalPurposeRegister.getGeneralPurposeRegister(r)));
        }
    }

    public static void CHK(int r0, int deviceID, Machine machine) {
        int[] a = new int[16];
        Boolean result = machine.getDeviceStatus(deviceID);
        for (int i = 0; i < 16; i++) {
            a[i] = result ? 1 : 0;
        }
        machine.generalPurposeRegister.setGeneralPurposeRegister(r0, Utility.BinaryToDecimalism(Utility.replace(Arrays.toString(a))));
    }
}