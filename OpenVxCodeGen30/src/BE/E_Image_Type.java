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
public enum E_Image_Type implements E_Type {
    VX_DF_IMAGE_VIRT,
    VX_DF_IMAGE_RGB,
    VX_DF_IMAGE_RGBX,
    VX_DF_IMAGE_NV12 ,
    VX_DF_IMAGE_NV21 ,
    VX_DF_IMAGE_UYVY,
    VX_DF_IMAGE_YUYV,
    VX_DF_IMAGE_IYUV,
    VX_DF_IMAGE_YUV4,
    VX_DF_IMAGE_U8,
    VX_DF_IMAGE_U16,
    VX_DF_IMAGE_S16,
    VX_DF_IMAGE_U32,
    VX_DF_IMAGE_S32;
    
    public static E_Type getImageType(String s){
        switch (s) {
            case "VX_DF_IMAGE_VIRT":
                return VX_DF_IMAGE_VIRT;
            case "VX_DF_IMAGE_RGB":
                return VX_DF_IMAGE_RGB;
            case "VX_DF_IMAGE_RGBX":
                return VX_DF_IMAGE_RGBX;
            case "VX_DF_IMAGE_NV12":
                return VX_DF_IMAGE_NV12;
            case "VX_DF_IMAGE_NV21":
                return VX_DF_IMAGE_NV21;
            case "VX_DF_IMAGE_UYVY":
                return VX_DF_IMAGE_UYVY;
            case "VX_DF_IMAGE_YUYV":
                return VX_DF_IMAGE_YUYV;
            case "VX_DF_IMAGE_IYUV":
                return VX_DF_IMAGE_IYUV;
            case "VX_DF_IMAGE_YUV4":
                return VX_DF_IMAGE_YUV4;
            case "VX_DF_IMAGE_U8":
                return VX_DF_IMAGE_U8;
            case "VX_DF_IMAGE_U16":
                return VX_DF_IMAGE_U16;
            case "VX_DF_IMAGE_S16":
                return VX_DF_IMAGE_S16;
            case "VX_DF_IMAGE_U32":
                return VX_DF_IMAGE_U32;
            case "VX_DF_IMAGE_S32":
                return VX_DF_IMAGE_S32;
            default:
                return null;

        }
        
    
    }
    

    
}

