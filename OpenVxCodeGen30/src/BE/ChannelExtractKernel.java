/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BE;

/**
 *
 * @author natan
 */
public class ChannelExtractKernel extends Kernel{
    private E_Channel_Type channel;

    public ChannelExtractKernel(){
        super();
    }
    public ChannelExtractKernel( E_Kernels_Name name, int number,E_Channel_Type channel) {
        super(name, number);
        this.channel = channel;
    }

    /**
     * @return the channel
     */
    public E_Channel_Type getChannel() {
        return channel;
    }

    /**
     * @param channel the channel to set
     */
    public void setChannel(E_Channel_Type channel) {
        this.channel = channel;
    }
    
    
    
}
