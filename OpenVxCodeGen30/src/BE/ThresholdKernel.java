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
public class ThresholdKernel extends Kernel{
    private int value; // for binary threshold 
    private int upper; // for range threshold
    private int lower;
    private E_Data_Type dataType;
    private E_Threshold_Type type;
    
    // constructor for range threshold type
    public ThresholdKernel(E_Kernels_Name Name, int Number, ArrayList<CodeParameter> OutputParameters,
                            ArrayList<ParametersMap> InputParameters, 
                            E_Threshold_Type _type, E_Data_Type _dataType, 
                            int _upper,int _lower)
    {
        super(Name, Number, OutputParameters, InputParameters);
        type= _type;
        dataType= _dataType;
        upper= _upper;
        lower= _lower;
    }

    // constructor for binary threshold type
    public ThresholdKernel(E_Kernels_Name Name, int Number, ArrayList<CodeParameter> OutputParameters,
                            ArrayList<ParametersMap> InputParameters, 
                            E_Threshold_Type _type, E_Data_Type _dataType, 
                            int _value)
    {
        super(Name, Number, OutputParameters, InputParameters);
        type= _type;
        dataType= _dataType;
        value= _value;
    }

    public ThresholdKernel() {
        super();
    }

        // constructor for range threshold type
    public ThresholdKernel(E_Kernels_Name Name, int Number, 
                            E_Threshold_Type _type, E_Data_Type _dataType, 
                            int _upper,int _lower)
    {
        super(Name, Number);
        type= _type;
        dataType= _dataType;
        upper= _upper;
        lower= _lower;
    }

    // constructor for binary threshold type
    public ThresholdKernel(E_Kernels_Name Name, int Number, 
                            E_Threshold_Type _type, E_Data_Type _dataType, 
                            int _value)
    {
        super(Name, Number);
        type= _type;
        dataType= _dataType;
        value= _value;
    }
    
    
    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * @return the upper
     */
    public int getUpper() {
        return upper;
    }

    /**
     * @param upper the upper to set
     */
    public void setUpper(int upper) {
        this.upper = upper;
    }

    /**
     * @return the lower
     */
    public int getLower() {
        return lower;
    }

    /**
     * @param lower the lower to set
     */
    public void setLower(int lower) {
        this.lower = lower;
    }

    /**
     * @return the dataType
     */
    public E_Data_Type getDataType() {
        return dataType;
    }

    /**
     * @param dataType the dataType to set
     */
    public void setDataType(E_Data_Type dataType) {
        this.dataType = dataType;
    }

    /**
     * @return the type
     */
    public E_Threshold_Type getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(E_Threshold_Type type) {
        this.type = type;
    }    
}
