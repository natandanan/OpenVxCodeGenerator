
package BE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author elyasaf
 */
public class DB_Kernels {    // singleton
    
    private static DB_Kernels instance = null;// DB singleton instance

            
     private HashMap<Integer, DBParameter> Param_AbsDiffVars;
     private HashMap<Integer, DBParameter> Param_AccumulateImageVars;
     private HashMap<Integer, DBParameter> Param_AccumulateSquareImageVars;
     private HashMap<Integer, DBParameter> Param_AccumulateWeightedImageVars;
     private HashMap<Integer, DBParameter> Param_AddVars;
     private HashMap<Integer, DBParameter> Param_colorConvertVars;
     private HashMap<Integer, DBParameter> Param_channelExtractVars;
     private HashMap<Integer, DBParameter> Param_EqualizeHistVars;
     private HashMap<Integer, DBParameter> Param_ChannelCombineVars;
     private HashMap<Integer, DBParameter> Param_Sobel3x3Vars;
     private HashMap<Integer, DBParameter> Param_MagnitudeVars;
     private HashMap<Integer, DBParameter> Param_ConvertDepthVars;
     private HashMap<Integer, DBParameter> Param_Gaussian3x3Vars;
     private HashMap<Integer, DBParameter> Param_ThresholdVars;
     private HashMap<Integer, DBParameter> Param_PhaseVars;
     private HashMap<Integer, DBParameter> Param_UserKernelVars;

    
    private DB_Kernels() {
      // Exists only to defeat instantiation.
      //Natan add
      //initialKernelsVars();
      initialKernelsParams();
   }

   public  static DB_Kernels getInstance() {
      if(instance == null) {
         instance = new DB_Kernels();
      }
      return instance;
   }
   
    public void initialKernelsParams(){
    
        setParam_AbsDiffVars(new HashMap<>());
        getParam_AbsDiffVars().put(0, new DBParameter("graph"));
        getParam_AbsDiffVars().put(1, new DBParameter("in1", 1, new ArrayList<>(Arrays.asList(E_Image_Type.VX_DF_IMAGE_U8, E_Image_Type.VX_DF_IMAGE_S16)), E_IO.input));
        getParam_AbsDiffVars().put(2, new DBParameter("in2", 2, new ArrayList<>(Arrays.asList(E_Image_Type.VX_DF_IMAGE_U8, E_Image_Type.VX_DF_IMAGE_S16)), E_IO.input));
        getParam_AbsDiffVars().put(3, new DBParameter("out", 3, new ArrayList<>(Arrays.asList(E_Image_Type.VX_DF_IMAGE_U8, E_Image_Type.VX_DF_IMAGE_S16)), E_IO.output));

        
        Param_AccumulateImageVars= new HashMap<>();
        getParam_AccumulateImageVars().put(0, new DBParameter("graph"));
        getParam_AccumulateImageVars().put(1, new DBParameter("input", 1, new ArrayList<>(Arrays.asList(E_Image_Type.VX_DF_IMAGE_U8)), E_IO.input));
        getParam_AccumulateImageVars().put(2, new DBParameter("accum", 2, new ArrayList<>(Arrays.asList(E_Image_Type.VX_DF_IMAGE_S16)), E_IO.output));
        
        Param_AccumulateSquareImageVars= new HashMap<>();
        getParam_AccumulateSquareImageVars().put(0, new DBParameter("graph"));
        getParam_AccumulateSquareImageVars().put(1, new DBParameter("input", 1, new ArrayList<>(Arrays.asList(E_Image_Type.VX_DF_IMAGE_U8)), E_IO.input));
        getParam_AccumulateSquareImageVars().put(2, new DBParameter("shift", 2, new ArrayList<>(Arrays.asList(E_Data_Type.VX_TYPE_UINT32)), E_IO.addingInfo));
        getParam_AccumulateSquareImageVars().put(3, new DBParameter("accum", 3, new ArrayList<>(Arrays.asList(E_Image_Type.VX_DF_IMAGE_U8)), E_IO.output));        
        
        Param_AccumulateWeightedImageVars= new HashMap<>();
        getParam_AccumulateWeightedImageVars().put(0, new DBParameter("graph"));
        getParam_AccumulateWeightedImageVars().put(1, new DBParameter("input", 1, new ArrayList<>(Arrays.asList(E_Image_Type.VX_DF_IMAGE_U8)), E_IO.input));
        getParam_AccumulateWeightedImageVars().put(2, new DBParameter("alpha", 2, new ArrayList<>(Arrays.asList(E_Data_Type.VX_TYPE_FLOAT32)), E_IO.addingInfo));
        getParam_AccumulateWeightedImageVars().put(3, new DBParameter("accum", 3, new ArrayList<>(Arrays.asList(E_Image_Type.VX_DF_IMAGE_U8)), E_IO.output));

        Param_AddVars= new HashMap<>();
        getParam_AddVars().put(0, new DBParameter("graph"));
        getParam_AddVars().put(1, new DBParameter("in1", 1, new ArrayList<>(Arrays.asList(E_Image_Type.VX_DF_IMAGE_U8, E_Image_Type.VX_DF_IMAGE_S16)), E_IO.input));
        getParam_AddVars().put(2, new DBParameter("in2", 2, new ArrayList<>(Arrays.asList(E_Image_Type.VX_DF_IMAGE_U8, E_Image_Type.VX_DF_IMAGE_S16)), E_IO.input));
        //getParam_AddVars().put(2, new Parameter("policy", new E_Type[]{E_Image_Type.VX_DF_IMAGE_U8}));
        getParam_AddVars().put(3, new DBParameter("out", 4, new ArrayList<>(Arrays.asList(E_Image_Type.VX_DF_IMAGE_U8, E_Image_Type.VX_DF_IMAGE_S16)), E_IO.output));
        
        Param_colorConvertVars= new HashMap<>();
        getParam_colorConvertVars().put(0, new DBParameter("graph"));
        getParam_colorConvertVars().put(1, new DBParameter("input", 1, new ArrayList<E_Type>(Arrays.asList(E_Image_Type.values())),E_IO.input));
        getParam_colorConvertVars().put(2, new DBParameter("output", 2, new ArrayList<E_Type>(Arrays.asList(E_Image_Type.values())), E_IO.output));
        
        Param_Sobel3x3Vars= new HashMap<>();
        getParam_Sobel3x3Vars().put(0, new DBParameter("graph"));
        getParam_Sobel3x3Vars().put(1, new DBParameter("input", 1, new ArrayList<>(Arrays.asList(E_Image_Type.VX_DF_IMAGE_U8)), E_IO.input));
        getParam_Sobel3x3Vars().put(2, new DBParameter("output_x", 2, new ArrayList<E_Type>(Arrays.asList(E_Image_Type.VX_DF_IMAGE_S16)), E_IO.output, true));
        getParam_Sobel3x3Vars().put(3, new DBParameter("output_y", 3, new ArrayList<>(Arrays.asList(E_Image_Type.VX_DF_IMAGE_S16)), E_IO.output, true));
        
        Param_MagnitudeVars= new HashMap<>();
        getParam_MagnitudeVars().put(0, new DBParameter("graph"));
        getParam_MagnitudeVars().put(1, new DBParameter("grad_x", 1, new ArrayList<>(Arrays.asList(E_Image_Type.VX_DF_IMAGE_S16)), E_IO.input));
        getParam_MagnitudeVars().put(2, new DBParameter("grad_y", 2, new ArrayList<>(Arrays.asList(E_Image_Type.VX_DF_IMAGE_S16)), E_IO.input));
        getParam_MagnitudeVars().put(3, new DBParameter("mag", 3, new ArrayList<>(Arrays.asList(E_Image_Type.VX_DF_IMAGE_S16)), E_IO.output));
        
        Param_ConvertDepthVars= new HashMap<>();
        getParam_ConvertDepthVars().put(0, new DBParameter("graph"));
        getParam_ConvertDepthVars().put(1, new DBParameter("input", 1, new ArrayList<E_Type>(Arrays.asList(E_Image_Type.values())), E_IO.input));
        getParam_ConvertDepthVars().put(2, new DBParameter("output", 2, new ArrayList<E_Type>(Arrays.asList(E_Image_Type.values())), E_IO.output));
        getParam_ConvertDepthVars().put(3, new DBParameter("policy", 3, new ArrayList<E_Type>(Arrays.asList(E_Convert_Policy.values())), E_IO.addingInfo));
        getParam_ConvertDepthVars().put(4, new DBParameter("shift", 4, new ArrayList<>(Arrays.asList(E_Data_Type.VX_TYPE_INT32)), E_IO.addingInfo));

        Param_Gaussian3x3Vars= new HashMap<>();
        getParam_Gaussian3x3Vars().put(0, new DBParameter("graph"));
        getParam_Gaussian3x3Vars().put(1, new DBParameter("input", 1, new ArrayList<>(Arrays.asList(E_Image_Type.VX_DF_IMAGE_U8)), E_IO.input));
        getParam_Gaussian3x3Vars().put(2, new DBParameter("output", 2, new ArrayList<>(Arrays.asList(E_Image_Type.VX_DF_IMAGE_U8)), E_IO.output));
        
        Param_ThresholdVars= new HashMap<>();
        getParam_ThresholdVars().put(0, new DBParameter("graph"));
        getParam_ThresholdVars().put(1, new DBParameter("input", 1, new ArrayList<>(Arrays.asList(E_Image_Type.VX_DF_IMAGE_U8)), E_IO.input));
        getParam_ThresholdVars().put(2, new DBParameter("thresh", 2, new ArrayList<E_Type>(Arrays.asList(E_Threshold_Type.values())), E_IO.addingInfo));
        getParam_ThresholdVars().put(3, new DBParameter("output", 3, new ArrayList<E_Type>(Arrays.asList(E_Image_Type.values())), E_IO.output));
                        
        Param_channelExtractVars= new HashMap<>();
        getParam_channelExtractVars().put(0, new DBParameter("graph"));
        getParam_channelExtractVars().put(1, new DBParameter("input", 1, new ArrayList<>(Arrays.asList(E_Image_Type.VX_DF_IMAGE_IYUV, E_Image_Type.VX_DF_IMAGE_RGB, E_Image_Type.VX_DF_IMAGE_RGBX, E_Image_Type.VX_DF_IMAGE_UYVY, E_Image_Type.VX_DF_IMAGE_YUV4, E_Image_Type.VX_DF_IMAGE_YUYV)), E_IO.input));
        getParam_channelExtractVars().put(2, new DBParameter("channel", 2, new ArrayList<E_Type>(Arrays.asList(E_Channel_Type.values())), E_IO.addingInfo));
        getParam_channelExtractVars().put(3, new DBParameter("output", 3, new ArrayList<>(Arrays.asList(E_Image_Type.VX_DF_IMAGE_U8)), E_IO.output));

        Param_EqualizeHistVars= new HashMap<>();
        getParam_EqualizeHistVars().put(0, new DBParameter("graph"));
        getParam_EqualizeHistVars().put(1, new DBParameter("input", 1, new ArrayList<>(Arrays.asList(E_Image_Type.VX_DF_IMAGE_U8)), E_IO.input));
        getParam_EqualizeHistVars().put(2, new DBParameter("output", 2, new ArrayList<>(Arrays.asList(E_Image_Type.VX_DF_IMAGE_U8)), E_IO.output));

        Param_ChannelCombineVars= new HashMap<>();
        getParam_ChannelCombineVars().put(0, new DBParameter("graph"));
        getParam_ChannelCombineVars().put(1, new DBParameter("plane0", 1, new ArrayList<>(Arrays.asList(E_Image_Type.VX_DF_IMAGE_U8)), E_IO.input));
        getParam_ChannelCombineVars().put(2, new DBParameter("plane1", 2, new ArrayList<>(Arrays.asList(E_Image_Type.VX_DF_IMAGE_U8)), E_IO.input));
        getParam_ChannelCombineVars().put(3, new DBParameter("plane2", 3, new ArrayList<>(Arrays.asList(E_Image_Type.VX_DF_IMAGE_U8)), E_IO.input,true));
        getParam_ChannelCombineVars().put(4, new DBParameter("plane3", 4, new ArrayList<>(Arrays.asList(E_Image_Type.VX_DF_IMAGE_U8)), E_IO.input,true));
        getParam_ChannelCombineVars().put(5, new DBParameter("output", 5, new ArrayList<E_Type>(Arrays.asList(E_Image_Type.values())), E_IO.output));
       
        setParam_PhaseVars(new HashMap<>());
        getParam_PhaseVars().put(0, new DBParameter("graph"));
        getParam_PhaseVars().put(1, new DBParameter("grad_x", 1, new ArrayList<>(Arrays.asList(E_Image_Type.VX_DF_IMAGE_S16)), E_IO.input));
        getParam_PhaseVars().put(2, new DBParameter("grad_y", 2, new ArrayList<>(Arrays.asList(E_Image_Type.VX_DF_IMAGE_S16)), E_IO.input));
        getParam_PhaseVars().put(3, new DBParameter("orientation", 3, new ArrayList<>(Arrays.asList(E_Image_Type.VX_DF_IMAGE_U8)), E_IO.output));

        setParam_UserKernelVars(new HashMap<>());
        getParam_UserKernelVars().put(0, new DBParameter("graph"));
        getParam_UserKernelVars().put(1, new DBParameter("input", 1, new ArrayList<>(Arrays.asList(E_Image_Type.values())), E_IO.input));
        getParam_UserKernelVars().put(2, new DBParameter("output", 2, new ArrayList<>(Arrays.asList(E_Image_Type.values())), E_IO.output));
        
     }
    
    
    /**
     * @return the Param_colorConvertVars
     */
    public  HashMap<Integer, DBParameter> getParam_colorConvertVars() {
        return Param_colorConvertVars;
    }

    /**
     * @return the Param_channelExtractVars
     */
    public  HashMap<Integer, DBParameter> getParam_channelExtractVars() {
        return Param_channelExtractVars;
    }

    /**
     * @return the Param_EqualizeHistVars
     */
    public HashMap<Integer, DBParameter> getParam_EqualizeHistVars() {
        return Param_EqualizeHistVars;
    }

    /**
     * @return the Param_ChannelCombineVars
     */
    public  HashMap<Integer, DBParameter> getParam_ChannelCombineVars() {
        return Param_ChannelCombineVars;
    }

    /**
     * @return the Param_Sobel3x3Vars
     */
    public  HashMap<Integer, DBParameter> getParam_Sobel3x3Vars() {
        return Param_Sobel3x3Vars;
    }

    /**
     * @return the Param_MagnitudeVars
     */
    public  HashMap<Integer, DBParameter> getParam_MagnitudeVars() {
        return Param_MagnitudeVars;
    }

    /**
     * @return the Param_ConvertDepthVars
     */
    public  HashMap<Integer, DBParameter> getParam_ConvertDepthVars() {
        return Param_ConvertDepthVars;
    }

    /**
     * @return the Param_Gaussian3x3Vars
     */
    public  HashMap<Integer, DBParameter> getParam_Gaussian3x3Vars() {
        return Param_Gaussian3x3Vars;
    }

    /**
     * @return the Param_ThresholdVars
     */
    public  HashMap<Integer, DBParameter> getParam_ThresholdVars() {
        return Param_ThresholdVars;
    }


    public  HashMap<Integer, DBParameter> getParam_AbsDiffVars() {
        return Param_AbsDiffVars;
    }

    /**
     * @param aParam_AbsDiffVars the Param_AbsDiffVars to set
     */
    public  void setParam_AbsDiffVars(HashMap<Integer, DBParameter> aParam_AbsDiffVars) {
        Param_AbsDiffVars = aParam_AbsDiffVars;
    }

    /**
     * @return the Param_AccumulateImageVars
     */
    public  HashMap<Integer, DBParameter> getParam_AccumulateImageVars() {
        return Param_AccumulateImageVars;
    }

    /**
     * @param aParam_AccumulateImageVars the Param_AccumulateImageVars to set
     */
    public  void setParam_AccumulateImageVars(HashMap<Integer, DBParameter> aParam_AccumulateImageVars) {
        Param_AccumulateImageVars = aParam_AccumulateImageVars;
    }

    /**
     * @return the Param_AccumulateSquareImageVars
     */
    public  HashMap<Integer, DBParameter> getParam_AccumulateSquareImageVars() {
        return Param_AccumulateSquareImageVars;
    }

    /**
     * @param aParam_AccumulateSquareImageVars the Param_AccumulateSquareImageVars to set
     */
    public  void setParam_AccumulateSquareImageVars(HashMap<Integer, DBParameter> aParam_AccumulateSquareImageVars) {
        Param_AccumulateSquareImageVars = aParam_AccumulateSquareImageVars;
    }

    /**
     * @return the Param_AccumulateWeightedImageVars
     */
    public  HashMap<Integer, DBParameter> getParam_AccumulateWeightedImageVars() {
        return Param_AccumulateWeightedImageVars;
    }

    /**
     * @param aParam_AccumulateWeightedImageVars the Param_AccumulateWeightedImageVars to set
     */
    public  void setParam_AccumulateWeightedImageVars(HashMap<Integer, DBParameter> aParam_AccumulateWeightedImageVars) {
        Param_AccumulateWeightedImageVars = aParam_AccumulateWeightedImageVars;
    }

    
    /**
     * @return the Param_PhaseVars
     */
    public HashMap<Integer, DBParameter> getParam_PhaseVars() {
        return Param_PhaseVars;
    }

    /**
     * @param Param_PhaseVars the Param_PhaseVars to set
     */
    public void setParam_PhaseVars(HashMap<Integer, DBParameter> Param_PhaseVars) {
        this.Param_PhaseVars = Param_PhaseVars;
    }

     
    
    
    /**
     * @return the Param_AddVars
     */
    public  HashMap<Integer, DBParameter> getParam_AddVars() {
        return Param_AddVars;
    }

    /**
     * @param aParam_AddVars the Param_AddVars to set
     */
    public void setParam_AddVars(HashMap<Integer, DBParameter> aParam_AddVars) {
        Param_AddVars = aParam_AddVars;
    }
     
     public ArrayList<String> getVarsNameListByKernelName(E_Kernels_Name name,E_IO IOtype){
        ArrayList<String> l = new ArrayList<>();
        switch (name){
            case colorConvert:
                getParamNameListByIOType(getParam_colorConvertVars(), l, IOtype);
                return l; 
            case channelExtract:
                getParamNameListByIOType(getParam_channelExtractVars(), l, IOtype);
                return l; 
            case EqualizeHist:
                getParamNameListByIOType(getParam_EqualizeHistVars(), l, IOtype);
                return l;
            case ChannelCombine:
                getParamNameListByIOType(getParam_ChannelCombineVars(), l, IOtype);
                return l;
            case Sobel3x3:
                getParamNameListByIOType(getParam_Sobel3x3Vars(), l, IOtype);
                return l;
            case Magnitude:
                getParamNameListByIOType(getParam_MagnitudeVars(), l, IOtype);
                return l;
            case ConvertDepth:
                getParamNameListByIOType(getParam_ConvertDepthVars(), l, IOtype);
                return l;
            case Gaussian3x3:
                getParamNameListByIOType(getParam_Gaussian3x3Vars(), l, IOtype);
                return l;
            case Phase:
                getParamNameListByIOType(getParam_PhaseVars(), l, IOtype);
                return l;
            case Threshold:
                getParamNameListByIOType(getParam_ThresholdVars(), l, IOtype);
                return l; 
            case UserKernel:
                getParamNameListByIOType(getParam_UserKernelVars(), l, IOtype);
                return l; 
        }
        return null;
    }
     public ArrayList<E_Type> getOptionalTypesByKernelName(E_Kernels_Name name,Integer loc){
        switch (name){
            case colorConvert:
                return getParam_colorConvertVars().get(loc).getOptionalTypes();
            case channelExtract:
                return getParam_channelExtractVars().get(loc).getOptionalTypes();
            case EqualizeHist:
                return getParam_EqualizeHistVars().get(loc).getOptionalTypes();
            case ChannelCombine:
                return getParam_ChannelCombineVars().get(loc).getOptionalTypes();
            case Sobel3x3:
                return getParam_Sobel3x3Vars().get(loc).getOptionalTypes(); 
            case Magnitude:
                return getParam_MagnitudeVars().get(loc).getOptionalTypes();
            case ConvertDepth:
                return getParam_ConvertDepthVars().get(loc).getOptionalTypes();
            case Gaussian3x3:
                return getParam_Gaussian3x3Vars().get(loc).getOptionalTypes();
            case Phase:
                return getParam_PhaseVars().get(loc).getOptionalTypes();
            case Threshold:
                return getParam_ThresholdVars().get(loc).getOptionalTypes();
            case UserKernel:
                return getParam_UserKernelVars().get(loc).getOptionalTypes();
            

        }
        return null;
    }
     public Integer getParamLocationByParamName(E_Kernels_Name kernelName,String paramName){
         switch (kernelName){
            case colorConvert:
                for(Entry<Integer,DBParameter> entry: getParam_colorConvertVars().entrySet())
                   if (entry.getValue().getName().equals(paramName))
                       return entry.getKey();
             break;
            case channelExtract:
                for(Entry<Integer,DBParameter> entry: getParam_channelExtractVars().entrySet())
                   if (entry.getValue().getName().equals(paramName))
                       return entry.getKey();
             break;
            case EqualizeHist:
                for(Entry<Integer,DBParameter> entry: getParam_EqualizeHistVars().entrySet())
                   if (entry.getValue().getName().equals(paramName))
                       return entry.getKey();
             break;
            case ChannelCombine:
                for(Entry<Integer,DBParameter> entry: getParam_ChannelCombineVars().entrySet())
                   if (entry.getValue().getName().equals(paramName))
                       return entry.getKey();
             break;
            case Sobel3x3: 
                for(Entry<Integer,DBParameter> entry: getParam_Sobel3x3Vars().entrySet())
                   if (entry.getValue().getName().equals(paramName))
                       return entry.getKey();
             break;
            case Magnitude:
                for(Entry<Integer,DBParameter> entry: getParam_MagnitudeVars().entrySet())
                   if (entry.getValue().getName().equals(paramName))
                       return entry.getKey();
             break;
            case ConvertDepth:
                for(Entry<Integer,DBParameter> entry: getParam_ConvertDepthVars().entrySet())
                   if (entry.getValue().getName().equals(paramName))
                       return entry.getKey();
             break;
            case Gaussian3x3:
                for(Entry<Integer,DBParameter> entry: getParam_Gaussian3x3Vars().entrySet())
                   if (entry.getValue().getName().equals(paramName))
                       return entry.getKey();
             break;
            case Phase:
                for(Entry<Integer,DBParameter> entry: getParam_PhaseVars().entrySet())
                   if (entry.getValue().getName().equals(paramName))
                       return entry.getKey();
             break;
            case Threshold:
                for(Entry<Integer,DBParameter> entry: getParam_ThresholdVars().entrySet())
                   if (entry.getValue().getName().equals(paramName))
                       return entry.getKey();
             break;
             
             case UserKernel:
                for(Entry<Integer,DBParameter> entry: getParam_UserKernelVars().entrySet())
                   if (entry.getValue().getName().equals(paramName))
                       return entry.getKey();
             break;
             
        }
        return null;
     } 
     public void getParamNameListByIOType(HashMap<Integer,DBParameter> hm,ArrayList<String> l,E_IO IOtype){
         hm.forEach((key, paramDetails) -> {
         if (key > 0){ 
                        switch (IOtype){
                            case input:
                                if (paramDetails.getIO().equals(E_IO.input))
                                        l.add(paramDetails.getName());
                            break;
                             case output:
                                if (paramDetails.getIO().equals(E_IO.output))
                                        l.add(paramDetails.getName());
                            break;
                             case addingInfo:
                                if (paramDetails.getIO().equals(E_IO.addingInfo))
                                        l.add(paramDetails.getName());
                            break;
                        }
                    }
         
         });
         
     }

    /**
     * @return the Param_UserKernelVars
     */
    public HashMap<Integer, DBParameter> getParam_UserKernelVars() {
        return Param_UserKernelVars;
    }

    /**
     * @param Param_UserKernelVars the Param_UserKernelVars to set
     */
    public void setParam_UserKernelVars(HashMap<Integer, DBParameter> Param_UserKernelVars) {
        this.Param_UserKernelVars = Param_UserKernelVars;
    }
     
}
