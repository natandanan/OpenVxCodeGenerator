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
public class DBParameter {
    String Name;
    private int Location;
    private ArrayList<E_Type> optionalTypes;
    private E_IO IO;
    private Boolean optional;

    public DBParameter() {
    }

    
    public DBParameter(String name) {
        Name= name;
    }

    
    public DBParameter(String name,int location, ArrayList<E_Type> OpTypes, E_IO io) {
        Name= name;
        Location= location;
        optionalTypes= OpTypes;
        IO= io;
        this.optional = false;
    }

    public DBParameter(String name,int location, ArrayList<E_Type> OpTypes, E_IO io,Boolean optinal) {
        Name= name;
        Location= location;
        optionalTypes= OpTypes;
        IO= io;
        this.optional = optinal;
    }
    /**
     * @return the name
     */
    public String getName() {
        return Name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.Name = name;
    }

    


    /**
     * @return the optionalTypes
     */
    public ArrayList<E_Type> getOptionalTypes() {
        return optionalTypes;
    }

    /**
     * @param OptionalTypes the optionalTypes to set
     */
    public void setOptionalTypes(ArrayList<E_Type> optionalTypes) {
        this.optionalTypes = optionalTypes;
    }


    /**
     * @return the location
     */
    public int getLocation() {
        return Location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(int location) {
        this.Location = location;
    }

    /**
     * @return the IO
     */
    public E_IO getIO() {
        return IO;
    }

    /**
     * @param IO the IO to set
     */
    public void setIO(E_IO IO) {
        this.IO = IO;
    }
    
}
