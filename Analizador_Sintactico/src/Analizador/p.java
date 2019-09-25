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
    public static void main(String[] args) 
    {
        String preanalisis = "token_integer";
         int pos = preanalisis.lastIndexOf('_');
         
         
                String nomTipo = preanalisis.substring(pos+1);
                System.out.println(nomTipo);
        Tabla_de_Simbolos t = new Tabla_de_Simbolos();
        t.insertarElem("a#1#variable#integer");
        t.insertarElem("b#1#variable#integer");
        t.insertarElem("c#1#variable#integer");
        t.insertarElem("d#1#variable#integer");
        //falta ver como manejar la cant de parametros
        
        t.insertarElem("f#1#funcion##boolean");
        
        //t.imprimir();
//        System.out.println("------------------------------------------");
//        t.insertarTS();
//        t.insertarElem("a#1#variable#integer");
//        t.insertarElem("b#1#variable#integer");
//        t.insertarElem("c#1#variable#integer");
//        t.insertarElem("d#1#variable#integer");
//        
//        //t.insertarElem("c#1#procedimiento#a&v&c");
//        t.imprimir();
//        System.out.println("------------------------------------------");
//        t.eliminarTS();
//        t.imprimir();
    }
}
