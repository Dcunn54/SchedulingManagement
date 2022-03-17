/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 * Class handling creation and functionality of Country objects
 * @author Daniel
 */
public class Country {
    
    protected String name;
    protected int ID;
    
    /**
     * Country constructor
     * @param name
     * @param ID 
     */
    public Country(String name, int ID){
        this.name = name;
        this.ID = ID;
    }

    /**
     * Returns Country name
     * @return 
     */
    public String getName() {
        return name;
    }

    /**
     * Returns Country ID
     * @return 
     */
    public int getID() {
        return ID;
    }
    
    /**
     * Returns Country name as a String, overrides standard toString
     * @return 
     */
    @Override
    public String toString(){
        return this.name;
    }
    
}
