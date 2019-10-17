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
    
    public Tabla_de_Simbolos()
    {
       pila = new Stack();
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
        }
        HashMap aux = pila.peek();
        if(!aux.containsKey(nombre))
        {
            Atributos atributo = new Atributos(elem);
            aux.put(nombre, atributo);
          //  this.imprimir();
        }
        else
        {
            /*
                cambiar de lugar los imprimir de los errores para que puedan mostrar
                la linea
            */
            if(!((Atributos)aux.get(nombre)).getTipo().equalsIgnoreCase(arreglo[2])  )
            {
                System.out.println("Error semantico se quiere usar un atributo como "+((Atributos)aux.get(nombre)).getTipo()+
                        " y como "+arreglo[2]);
                exito = false;
            }
            else
            {
                if(((Atributos)aux.get(nombre)).getTipo().equalsIgnoreCase("variable")
                        &&!((Atributos)aux.get(nombre)).getTipoDato().equalsIgnoreCase(arreglo[3]))
                {
                   System.out.println("Error semantico se quiere usar una misma variable como "+((Atributos)aux.get(nombre)).getTipoDato()+
                            " y como "+arreglo[3]);
                    exito = false;
                }
            }
                
            
        }
        return exito;
    }
    public void insertarTS()
    {
        pila.push(new HashMap());
    }
    public void eliminarTS()
    {
        if(!pila.empty())
            pila.pop();
    }
     
    public void imprimir()
    {
        HashMap map = pila.peek();
        map.forEach((k,v)-> System.out.println(((Atributos)v).getAtributos()));
        System.out.println("------------------------------");
                
    }
    
}
