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
public class UserKernel extends Kernel{
    private String UserKernelName;

    public UserKernel(E_Kernels_Name e_Kernels_Name, String userKernelName, int kernelId) {
        super(e_Kernels_Name, kernelId);
        UserKernelName= userKernelName;                
    }

    public UserKernel() {
        super();
    }

    /**
     * @return the UserKernelName
     */
    public String getUserKernelName() {
        return UserKernelName;
    }

    /**
     * @param UserKernelName the UserKernelName to set
     */
    public void setUserKernelName(String UserKernelName) {
        this.UserKernelName = UserKernelName;
    }
}
