/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Arrays;

/**
 *
 * @author Daniel
 */
public class Language {
    
    //static Locale locale = Locale.FRANCE; //Test Login screen translation if user location is France
    static Locale locale = Locale.getDefault();
    static Locale us = Locale.US;
    
    public static Locale getLocale(){
        return locale;
    }
    
    public static String translate(String orig){
        String result = "";
        if (locale.equals(us)){
            //System.out.println("LANGUAGE CASE 1 " + locale);
            return orig;
        }
        else if (orig.contains(" ")){
            //System.out.println("LANGUAGE CASE 2 " + locale);
            ResourceBundle rb = ResourceBundle.getBundle("utilities/Nat", locale);
            String[] words = orig.split(" ");
            for (String s: words){
                result += rb.getString(s) + " ";
            }
            return result;
        }else{
            //System.out.println("LANGUAGE CASE 3 " + locale);
            ResourceBundle rb = ResourceBundle.getBundle("utilities/Nat", locale);
            return rb.getString(orig);
        }
    }
    
}
