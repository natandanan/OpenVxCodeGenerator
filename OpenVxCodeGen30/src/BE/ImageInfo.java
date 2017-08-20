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
public class ImageInfo {
    private E_Type type;

    
public ImageInfo() {
    }
    
    
   public ImageInfo(E_Type type) {
        this.type = type;
    }

    
   /**
     * @return the type
     */
    public E_Type getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(E_Type type) {
        this.type = type;
    }




}
