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
public class CodeParameter {
//    String Name;
    private Integer location;
    private ArrayList<E_Type> selectedTypes = new ArrayList<>();

    public CodeParameter() {
        location= -1;
    }

     public CodeParameter(int location) {
         this.location = location;
    }
     
    public CodeParameter(int location, ArrayList<E_Type> selTypes) {
        this.location= location;
        selectedTypes =  selTypes;
        
        
    }

    /**
     * @return the selectedTypes
     */
    public ArrayList<E_Type> getSelectedTypes() {
        return selectedTypes;
    }

    /**
     * @param selectedTypes the selectedTypes to set
     */
    public void setSelectedTypes(ArrayList<E_Type> selectedTypes) {
        this.selectedTypes = selectedTypes;
    }

     public void addToSelectedType(E_Type type){
        selectedTypes.add(type);
    }

    public E_Type getPrameterType(String type){
        //Assuming there is only one specifiec type
        switch (type) {
        
            case "E_Image_Type" :
                    for (E_Type t : selectedTypes)
                            if (t instanceof E_Image_Type)
                                return t;
                    break;
            case "E_Channel_Type" :
                    for (E_Type t : selectedTypes)
                            if (t instanceof E_Channel_Type)
                                return t;
                    break;
                   
        }
    
        return null;
    }

    /**
     * @return the location
     */
    public Integer getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(int location) {
        this.location = location;
    }
    
    @Override
    public String toString(){
        
        String res =  "Loc : " + this.location + '\n';
        res += "Pramaters type: \n ***************** \n";
        for (E_Type type: this.selectedTypes ){
            res += type.toString() +'\n';
        }
        res+= "*****************" + '\n';
        return res;
    }
    
    
    
    
}
