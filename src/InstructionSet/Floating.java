package InstructionSet;

import Machine.*;
import Registers.OtherRegister;

public class Floating {
    public static int FADD(int fr, int x, int i, int address, Machine machine) {
        int EA = Preprocessing.EffectiveAddress(i, x, address, machine);
        if (machine.memory.getLength() < EA) {
            OtherRegister.MFR_number = 3;
            return 3;
        }
        else {
            int EA1 = machine.cache.useCache(EA);
            int EA2 = machine.generalPurposeRegister.getGeneralPurposeRegister(fr);
            //int re = EA1 + r2;// add need to fix
            
            int base1= EA1 % 512;
            int ep1 = ((EA1 % 65536)/256)*2;
            int sign1 = EA1 / 65536;
            int number1 = base1 * ep1;
            if(sign1==1)
            	number1 = -number1;
            
            int base2= EA2 % 512;
            int ep2 = ((EA2 % 65536)/256)*2;
            int sign2 = EA2 / 65536;
            int number2 = base2 * ep2;
            if(sign2==1)
            	number2 = -number2;
            
            int re = number1 + number2;
            
            machine.generalPurposeRegister.setGeneralPurposeRegister(fr, re);
            return 10;
        }
    }

    public static int FSUB(int fr, int x, int i, int address, Machine machine) {
        int EA = Preprocessing.EffectiveAddress(i, x, address, machine);
        if (machine.memory.getLength() < EA) {
            OtherRegister.MFR_number = 3;
            return 3;
        }
        else {
            int EA1 = machine.cache.useCache(EA);
            int EA2 = machine.generalPurposeRegister.getGeneralPurposeRegister(fr);
           // int re =r2 - EA1; // need to fix
            
            int base1= EA1 % 512;
            int ep1 = ((EA1 % 65536)/256)*2;
            int sign1 = EA1 / 65536;
            int number1 = base1 * ep1;
            if(sign1==1)
            	number1 = -number1;
            
            int base2= EA2 % 512;
            int ep2 = ((EA2 % 65536)/256)*2;
            int sign2 = EA2 / 65536;
            int number2 = base2 * ep2;
            if(sign2==1)
            	number2 = -number2;
            
            int re = number1 - number2;
            
            machine.generalPurposeRegister.setGeneralPurposeRegister(fr, re);
            return 10;
        }
    }

    public static int VADD(int fr, int x, int i, int address, Machine machine) 
    {
    	int EA1 = Preprocessing.EffectiveAddress(i, x, address, machine);
        if (machine.memory.getLength() < EA1) {
            OtherRegister.MFR_number = 3;
            return 3;
        }
        else {
            int EA2 = EA1 + 1;
            int Cfr = machine.generalPurposeRegister.getGeneralPurposeRegister(fr);
            if(i==0)
            {
            	for(int j=1; j<= Cfr ; j++)
            	{
            		int v1 = machine.cache.useCache(EA1);
            		int v2 = machine.cache.useCache(EA2);
            		machine.cache.writeCache(v1, v1 + v2);
            	}
            }
            else
            {
            	int iEA1 = machine.cache.useCache(EA1); 
            	int iEA2 = machine.cache.useCache(EA2);
            	
            	for(int j=1; j<= Cfr ; j++)
            	{
            		int v1 = machine.cache.useCache(iEA1);
            		int v2 = machine.cache.useCache(iEA2);
            		machine.cache.writeCache(v1, v1 + v2);
            	}
			}
            return 10;
        }
    }
    
    public static int VSUB (int fr, int x, int i, int address, Machine machine)
    {
        int EA1 = Preprocessing.EffectiveAddress(i, x, address, machine);
        if (machine.memory.getLength() < EA1) {
            OtherRegister.MFR_number = 3;
            return 3;
        }
        else {
            int EA2 = EA1 + 1;
            int Cfr = machine.generalPurposeRegister.getGeneralPurposeRegister(fr);
            if(i==0)
            {
            	for(int j=1; j<= Cfr ; j++)
            	{
            		int v1 = machine.cache.useCache(EA1);
            		int v2 = machine.cache.useCache(EA2);
            		machine.cache.writeCache(v1, v1 - v2);
            	}
            }
            else
            {
            	int iEA1 = machine.cache.useCache(EA1);
            	int iEA2 = machine.cache.useCache(EA2);
            	
            	for(int j=1; j<= Cfr ; j++)
            	{
            		int v1 = machine.cache.useCache(iEA1);
            		int v2 = machine.cache.useCache(iEA2);
            		machine.cache.writeCache(v1, v1 - v2);
            	}
			}
            return 10;
        }
    }
    
    public static int CNVRT (int r, int x, int i, int address, Machine machine)
    {
    	int EA = Preprocessing.EffectiveAddress(i, x, address, machine);
        if (machine.memory.getLength() < EA) {
            OtherRegister.MFR_number = 3;
            return 3;
        }
        else
        {
        	int F = machine.generalPurposeRegister.getGeneralPurposeRegister(r);
        	if(F==0)
        	{
        		int change = machine.cache.useCache(EA);
        		machine.generalPurposeRegister.setGeneralPurposeRegister(r, change);
        	}
        	else
        	{
        		int change = machine.cache.useCache(EA);
        		machine.generalPurposeRegister.setGeneralPurposeRegister(0, change);
			}
        	return 10;
        }
    }
    
    public static int LDFR(int fr, int x, int i, int address, Machine machine)
    {
    	int EA = Preprocessing.EffectiveAddress(i, x, address, machine);
        if (machine.memory.getLength() < EA) {
            OtherRegister.MFR_number = 3;
            return 3;
        }
        else
        {
        	machine.generalPurposeRegister.setGeneralPurposeRegister(0, EA);
        	machine.generalPurposeRegister.setGeneralPurposeRegister(1, EA+1);
        	return 10;
        }
    }
    
    public static int STFR(int fr, int x, int i, int address, Machine machine)
    {
    	int EA = Preprocessing.EffectiveAddress(i, x, address, machine);
        if (machine.memory.getLength() < EA) {
            OtherRegister.MFR_number = 3;
            return 3;
        }
        else
        {
        	int fr1 = machine.generalPurposeRegister.getGeneralPurposeRegister(0);
        	int fr2 = machine.generalPurposeRegister.getGeneralPurposeRegister(1);
        	
        	if(i==0)
        	{
	        	machine.cache.writeCache(EA, fr1);
	        	machine.cache.writeCache(EA+1, fr2);
        	}
        	else
        	{
				int iEA = machine.cache.useCache(EA);
				machine.cache.writeCache(iEA, fr1);
	        	machine.cache.writeCache(iEA+1, fr2);
			}
        	return 10;
        }
    }
}