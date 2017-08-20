/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BE;

import java.util.ArrayList;
import BE.Kernel;

/**
 *
 * @author elyasaf
 */

public class Node {
    
    private Kernel kernel;
    private ArrayList<Node> parents; 
    private ArrayList<Node> children;
    
    private int Level;

    public Node() {
        parents = new ArrayList<>();
        children = new ArrayList<>();
    }

    public Node(Kernel kernel) {
        this.kernel = kernel;
        parents = new ArrayList<>();
        children = new ArrayList<>();
    }

    
    
    
    
    /**
     * @return the parents
     */
    public ArrayList<Node> getParents() {
        return parents;
    }

    /**
     * @param parents the parents to set
     */
    public void setParents(ArrayList<Node> parents) {
        this.parents = parents;
    }
    
    /**
     * @return the children
     */
    public ArrayList<Node> getChildren() {
        return children;
    }

    /**
     * @param children the children to set
     */
    public void setChildren(ArrayList<Node> children) {
        this.children = children;
    }
    
     /**
     * @return the Level
     */
    public int getLevel() {
        return Level;
    }

    /**
     * @param Level the Level to set
     */
    public void setLevel(int Level) {
        this.Level = Level;
    }

    /**
     * @return the kernel
     */
    public Kernel getKernel() {
        return kernel;
    }

    /**
     * @param kernel the kernel to set
     */
    public void setKernel(Kernel kernel) {
        this.kernel = kernel;
    }
}
