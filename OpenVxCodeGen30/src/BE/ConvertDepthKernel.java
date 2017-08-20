/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BE;

import java.util.ArrayList;

/**
 *
 * @author elyasaf
 */
public class ConvertDepthKernel extends Kernel {
    private E_Convert_Policy ConvertPolicy;
    private int shift;

    public ConvertDepthKernel(){
        super();
    }
    
    public ConvertDepthKernel(E_Kernels_Name Name, int Number,
                                    E_Convert_Policy _convertPolicy,
                                    int _shift)
    {
        super(Name, Number);
        ConvertPolicy= _convertPolicy;
        shift= _shift;
    }

    /**
     * @return the ConvertPolicy
     */
    public E_Convert_Policy getConvertPolicy() {
        return ConvertPolicy;
    }

    /**
     * @param ConvertPolicy the ConvertPolicy to set
     */
    public void setConvertPolicy(E_Convert_Policy ConvertPolicy) {
        this.ConvertPolicy = ConvertPolicy;
    }

    /**
     * @return the shift
     */
    public int getShift() {
        return shift;
    }

    /**
     * @param shift the shift to set
     */
    public void setShift(int shift) {
        this.shift = shift;
    }
}
