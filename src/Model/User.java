/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 * Class for handling creation and functionality of User objects
 * @author Daniel
 */
public class User {
    
    protected int ID;
    protected String userName;
    
    /**
     * User constructor
     * @param ID
     * @param userName 
     */
    public User(int ID, String userName){
        this.ID = ID;
        this.userName = userName;
    }
    
    /**
     * Returns User ID
     * @return 
     */
    public int getID(){
        return this.ID;
    }
    
    /**
     * Returns User name as a String, overrides standard toString
     * @return 
     */
    @Override
    public String toString(){
        return this.userName;
    }
}
