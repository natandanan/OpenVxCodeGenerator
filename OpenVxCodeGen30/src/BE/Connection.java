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
public class Connection {
    public Kernel father;
    public ArrayList<Kernel> children;

    public Connection() {
        // the father initialize depends on the actual kernel 
        children = new ArrayList<>();
    }
    
    public Connection(Kernel parent,Kernel child) {
        father= (parent instanceof Kernel)? new Kernel() : 
                (parent instanceof ThresholdKernel)? new ThresholdKernel() : new ConvertDepthKernel(); 
        father = new Kernel();
        children = new ArrayList<>();
        father = parent;
        children.add(child);
    }
    
    public Connection(Kernel parent,ArrayList<Kernel> myChildren) {
        father = (parent instanceof Kernel)? new Kernel() : 
                (parent instanceof ThresholdKernel)? new ThresholdKernel() : new ConvertDepthKernel(); 
        father = parent;
        children = myChildren;
    }
}