/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizador;

import java.util.HashMap;

/**
 *
 * @author PC
 */
public class p
{
    public static void main(String[] args) {
        HashMap h = new HashMap();
        
        h.put("02", "hola");
        h.put("03", "chao");
        h.put("01", "tres");
        
       
        
        boolean existe = false;
        int i = 0;
        while(!existe)
        {
            System.out.println("existe ");
            if(i== 5)
                existe=true;
            i++;
        }
    }
}
