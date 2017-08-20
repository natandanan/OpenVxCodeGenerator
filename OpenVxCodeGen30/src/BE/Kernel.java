/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BE;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author elyasaf
 */
public class Kernel {
    protected E_Kernels_Name Name;
    protected int Number;  // uniqe number for every node
    
    // parameters i/o information from the gui
    private ArrayList<CodeParameter> outputParameters;
    private ArrayList<ParametersMap> inputParameters;// include the input parameter list
    
    // variables name - for the final c code
    private HashMap<Integer, String> outputVariablesNames;
    private HashMap<Integer, String> inputVariablesNames;

    //Natan - to delete 
    public Kernel(E_Kernels_Name name, int number, ArrayList<CodeParameter> OutputParameters, ArrayList<ParametersMap> InputParameters){
            Name = name;
            Number = number;

            inputVariablesNames= new HashMap<>();
            outputVariablesNames= new HashMap<>();

            inputParameters= new ArrayList<>();
            outputParameters= new ArrayList<>();
            
            outputParameters= OutputParameters;
            inputParameters= InputParameters;
        }

        public Kernel(E_Kernels_Name name, int number){
            Name = name;
            Number = number;
            inputVariablesNames= new HashMap<>();
            outputVariablesNames= new HashMap<>();
            inputParameters= new ArrayList<>();
            outputParameters= new ArrayList<>();
        }
        
        
        public Kernel() {
            inputVariablesNames= new HashMap<>();
            outputVariablesNames= new HashMap<>();

            inputParameters= new ArrayList<>();
            outputParameters= new ArrayList<>();
        }

    
    /**
     * @return the Name
     */
    public E_Kernels_Name getName() {
        return Name;
    }

    /**
     * @param Name the Name to set
     */
    public void setName(E_Kernels_Name Name) {
        this.Name = Name;
    }

    /**
     * @return the Number
     */
    public int getNumber() {
        return Number;
    }

    /**
     * @param Number the Number to set
     */
    public void setNumber(int Number) {
        this.Number = Number;
    }
    
    
    //Natan add
    public void setPrameterType(int parmNum, E_Type type){
        
    }
    
    public void setPrameterName(int parmNum, String name){
        
    }
    
    
    

    /**
     * @return the outputParameters
     */
    public ArrayList<CodeParameter> getOutputParameters() {
        return outputParameters;
    }

    /**
     * @param outputParameters the outputParameters to set
     */
    public void setOutputParameters(ArrayList<CodeParameter> outputParameters) {
        this.outputParameters = outputParameters;
    }

    /**
     * @return the inputParameters
     */
    public ArrayList<ParametersMap> getInputParameters() {
        return inputParameters;
    }

    /**
     * @param inputParameters the inputParameters to set
     */
    public void setInputParameters(ArrayList<ParametersMap> inputParameters) {
        this.inputParameters = inputParameters;
    }

    /**
     * @return the outputVariablesNames
     */
    public HashMap<Integer, String> getOutputVariablesNames() {
        return outputVariablesNames;
    }

    /**
     * @param outputVariablesNames the outputVariablesNames to set
     */
    public void setOutputVariablesNames(HashMap<Integer, String> outputVariablesNames) {
        this.outputVariablesNames = outputVariablesNames;
    }

    /**
     * @return the inputVariablesNames
     */
    public HashMap<Integer, String> getInputVariablesNames() {
        return inputVariablesNames;
    }

    /**
     * @param inputVariablesNames the inputVariablesNames to set
     */
    public void setInputVariablesNames(HashMap<Integer, String> inputVariablesNames) {
        this.inputVariablesNames = inputVariablesNames;
    }

    
    public void addOutputParameters(ArrayList<CodeParameter> outputParameters) {
        this.outputParameters.addAll(outputParameters);
    }
    
    public void addInputParameters(ArrayList<ParametersMap> inputParameters) {
        this.inputParameters.addAll(inputParameters);
    }
    
    
    public void setFatherInfoByLocation(Integer p_loc,father_Info f){
        this.inputParameters.forEach((t) -> { 
            if (t.getInputParameter().getLocation().equals(p_loc))
                    t.setFatherInfo(f);
        });
       
    }
    
    @Override
    public String toString (){
        String res = "Name : " + this.Name;
        res += "Number : " + this.Number + '\n';
        res += "Input Parameters: " + '\n' +"**************" + '\n';
        for (ParametersMap param : this.inputParameters)
            res += param.toString() + '\n';
        res += "*****************" + '\n';
        /*res += "output Parameters: " + '\n' +"**************" + '\n';
        for (CodeParameter param : this.outputParameters)
            res += param.toString() + '\n';
        res += "*****************";
        res +="****************************************************";*/
        return res;
    }
    
}
