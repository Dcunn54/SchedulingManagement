/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 * Class for handling creation and functionality of State objects
 * @author Daniel
 */
public class State{
    
    protected String name;
    protected int ID;
    protected int countryID;
    
    /**
     * State constructor
     * @param name
     * @param ID
     * @param countryID 
     */
    public State (String name, int ID, int countryID){
        this.name = name;
        this.ID = ID;
        this.countryID = countryID;
    }

    /**
     * Returns State/Province name as a String, overrides standard toString
     * @return 
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns State ID
     * @return 
     */
    public int getID() {
        return ID;
    }

    /**
     * Returns State Country ID
     * @return 
     */
    public int getCountryID() {
        return countryID;
    }
    
    /**
     * Returns State/Province name as a String, overrides standard toString
     * @return 
     */
    @Override
    public String toString(){
        return this.name;
    }
    
}
