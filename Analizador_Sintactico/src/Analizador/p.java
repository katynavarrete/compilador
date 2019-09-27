/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizador;

import java.util.HashMap;
import java.util.Stack;

/**
 *
 * @author PC
 */
public class p
{
    public static void main(String[] args) 
    {
       Stack pila = new Stack();
       pila.push(5);
       pila.push(7);
       pila.push(9);
       
        System.out.println(pila.get(0));
         System.out.println(pila.get(1));
          System.out.println(pila.get(2));
          System.out.println(pila.size());
    }
}
