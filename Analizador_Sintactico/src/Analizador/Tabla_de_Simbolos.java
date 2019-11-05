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
public class Tabla_de_Simbolos 
{
     private Stack <HashMap> pila;
     private Stack <String> alcance;
    
    public Tabla_de_Simbolos()
    {
       pila = new Stack();
       alcance = new Stack();
       
    }
    public Atributos verificarElem(String elem)
    {
        Atributos atributo = null;
        HashMap aux;
        int i = pila.size()-1;
        while(i >= 0 && atributo == null )
        {
          aux= pila.get(i);
            if(aux.containsKey(elem.toLowerCase()))
            {
                atributo = (Atributos)aux.get(elem.toLowerCase());
            }
            i--;
        }
        return atributo;
    }
    public boolean insertarElem(String elem)
    {
        boolean exito = true;
        String [] arreglo = elem .split("#");
        String nombre = arreglo[0].toLowerCase();
        if(pila.empty())
        {
            pila.push(new HashMap());
            alcance.push("global");
        }
        HashMap aux = pila.peek();
        if(!aux.containsKey(nombre.toLowerCase()))
        {
            Atributos atributo = new Atributos(elem);
            aux.put(nombre, atributo);
          //  this.imprimir();
        }
        else
        {
            exito=false;
            System.out.print("ERROR SEMANTICO: SE QUIERE DECLARAR "+nombre+ " LA CUAL ESTA DECLARADA"  );
                
            
        }
     //  System.out.println("exito "+exito+"  " +elem);
     //imprimir();
        return exito;
    }
    public void insertarTS(String alcance)
    {
        pila.push(new HashMap());
        this.alcance.push(alcance);
        
    }
    public void eliminarTS()
    {
        if(!pila.empty())
            pila.pop();
        if(!alcance.empty())
            alcance.pop();
    }
    public String alcanceActual()
    {
       return alcance.peek();
    }
     
    public void imprimir()
    {
        System.out.println(alcance.peek());
        HashMap map = pila.peek();
        map.forEach((k,v)-> System.out.println(((Atributos)v).getAtributos()));
        System.out.println("------------------------------");
                
    }
    
}
