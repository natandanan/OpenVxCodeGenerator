/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BE;

/**
 *
 * @author elyasaf
 */
public class ParametersMap {
    private father_Info FatherInfo; 
    private CodeParameter inputParameter;

    public ParametersMap() {
    }

    public ParametersMap(father_Info FatherInfo, CodeParameter inputParameter) {
        this.FatherInfo = FatherInfo;
        this.inputParameter = inputParameter;
    }

    /**
     * @return the FatherInfo
     */
    public father_Info getFatherInfo() {
        return FatherInfo;
    }

    /**
     * @param FatherInfo the FatherInfo to set
     */
    public void setFatherInfo(father_Info FatherInfo) {
        this.FatherInfo = FatherInfo;
    }

    /**
     * @return the inputParameter
     */
    public CodeParameter getInputParameter() {
        return inputParameter;
    }

    /**
     * @param inputParameter the inputParameter to set
     */
    public void setInputParameter(CodeParameter inputParameter) {
        this.inputParameter = inputParameter;
    }
    
    
    
    
    @Override
    public String toString(){
        if (this.FatherInfo != null){
            return "Father ID : " + this.FatherInfo.getFatherId() + '\n'
                  +"Father Parm Loc: " + this.FatherInfo.getFatherParameterLocation() + '\n'
                  + this.inputParameter.toString();
        }
        else 
            return "";
    }
}