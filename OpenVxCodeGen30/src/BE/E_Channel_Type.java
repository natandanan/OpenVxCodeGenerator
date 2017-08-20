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
public enum E_Channel_Type implements E_Type {
    VX_CHANNEL_0,
    VX_CHANNEL_1,
    VX_CHANNEL_2,
    VX_CHANNEL_3,
    VX_CHANNEL_R,
    VX_CHANNEL_G,
    VX_CHANNEL_B,
    VX_CHANNEL_A,
    VX_CHANNEL_Y,
    VX_CHANNEL_U,
    VX_CHANNEL_V;
    
    public static E_Type getChannelType(String s){
        switch (s) {
            
            case "VX_CHANNEL_0":
                return VX_CHANNEL_0;
            case "VX_CHANNEL_1":
                return VX_CHANNEL_1;
            case "VX_CHANNEL_2":
                return VX_CHANNEL_2;
            case "VX_CHANNEL_3":
                return VX_CHANNEL_3;
            case "VX_CHANNEL_R":
                return VX_CHANNEL_R;
            case "VX_CHANNEL_G":
                return VX_CHANNEL_G;
            case "VX_CHANNEL_B":
                return VX_CHANNEL_B;
            case "VX_CHANNEL_A":
                return VX_CHANNEL_A;
            case "VX_CHANNEL_Y":
                return VX_CHANNEL_Y;
            case "VX_CHANNEL_U":
                return VX_CHANNEL_U;
            case "VX_CHANNEL_V":
                return VX_CHANNEL_V;
            
            default:
                return null;
        }
    }
}
