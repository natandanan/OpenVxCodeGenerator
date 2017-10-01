/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BE;
import BE.ImageInfo;

/**
 *
 * @author natan
 */
public class GraphInfo {

   
     private String name;
     private ImageInfo inputImageInfo;
     private ImageInfo outputImageInfo;

    public GraphInfo() {
         this.name = new String();
         this.outputImageInfo = new ImageInfo();
   }
    
    

   /* public GraphInfo(String name, ImageInfo imageInfo) {
        this.name = name;
        this.outputImageInfo = imageInfo;
                
    }*/

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

     /**
     * @return the inputImageInfo
     */
    public ImageInfo getInputImageInfo() {
        return inputImageInfo;
    }

    /**
     * @param inputImageInfo the inputImageInfo to set
     */
    public void setInputImageInfo(ImageInfo inputImageInfo) {
        this.inputImageInfo = inputImageInfo;
    }
    
    /**
     * @return the outputImageInfo
     */
    public ImageInfo getOutputImageInfo() {
        return outputImageInfo;
    }

    /**
     * @param outputImageInfo the outputImageInfo to set
     */
    public void setOutputImageInfo(ImageInfo outputImageInfo) {
        this.outputImageInfo = outputImageInfo;
    }
     
     
     
}
