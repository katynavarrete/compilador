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
        
       /* char c ='ñ';
        System.out.println(Character.BYTES);*/
        File archivo = new File("C:\\Users\\PC\\Desktop\\laboratorio comp e int\\tp1\\sintactico\\compilador"
                + "\\Analizador_Sintactico\\src\\Analizador\\probando");
        RandomAccessFile fr = new  RandomAccessFile(archivo,"r");
        
        for (int i = 0; i < fr.length(); i++) 
        {
            int aux = fr.read();
            System.out.println((char)aux+" "+aux);
        }
        if(fr != null)
            fr.close();
                
      
    }
}
