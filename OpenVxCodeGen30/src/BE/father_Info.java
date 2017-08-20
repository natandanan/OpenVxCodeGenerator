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
public class father_Info {
    private Integer fatherId;
    private Integer fatherParameterLocation;

    
    public father_Info() {
    }


    public father_Info(Integer fatherId, Integer fatherParameter) {
        this.fatherId = fatherId;
        this.fatherParameterLocation = fatherParameter;
    }
    

    /**
     * @return the fatherParameter
     */
    public Integer getFatherParameterLocation() {
        return fatherParameterLocation;
    }

    /**
     * @param fatherParameterLocation the fatherParameter to set
     */
    public void setFatherParameterLocation(Integer fatherParameterLocation) {
        this.fatherParameterLocation = fatherParameterLocation;
    }

    /**
     * @return the fatherId
     */
    public Integer getFatherId() {
        return fatherId;
    }

    /**
     * @param fatherId the fatherId to set
     */
    public void setFatherId(Integer fatherId) {
        this.fatherId = fatherId;
    }
    
}
