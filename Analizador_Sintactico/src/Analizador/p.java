/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizador;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Stack;

/**
 *
 * @author PC
 */
public class p
{
    public static void main(String[] args) throws FileNotFoundException, IOException 
    {
       int indice = 1;
       Stack <int []> pila  = new Stack();
       String [] cond = {"if","if","if","else","else","else"};
        for (int i = 0; i < cond.length; i++)
        {
            if(cond[i].equalsIgnoreCase("if"))
            {
                int [] aux = new int[2];
                aux[0]=indice;
                aux[1]=indice+1;
                pila.push(aux);
                indice+=2;
            }
            else
            {
              
                
            }
        }
       
    }
}
