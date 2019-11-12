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
    private Stack <Ts_auxiliar> pila;
    private int nivel = 0;
    
    public Tabla_de_Simbolos()
    {
       pila = new Stack();
   
    }
    public Atributos verificarElem(String elem)
    {
        Atributos atributo = null;
        Ts_auxiliar aux;
        int i = pila.size()-1;
        while(i >= 0 && atributo == null )
        {
          aux= pila.get(i);
            if(aux.verificarElem(elem.toLowerCase()) && !aux.getAlcance().equalsIgnoreCase(elem))
            {
                atributo = (Atributos)aux.getAtributos(elem);
            }
            i--;
        }
        
        return atributo;
    }
    public Atributos verificarElemFun(String elem)
    {
        Atributos atributo = null;
        Ts_auxiliar aux;
        int i = pila.size()-1;
        while(i >= 0 && atributo == null )
        {
          aux= pila.get(i);
            if(aux.verificarElem(elem.toLowerCase()))
            {
                atributo = (Atributos)aux.getAtributos(elem);
            }
            i--;
        }
        
        return atributo;
    }
    
    public boolean insertarElem(String elem)
    {
       boolean exito;
        if(pila.empty())
        {
            pila.push(new Ts_auxiliar("global",0));
            
        }
        Ts_auxiliar aux = pila.peek();
        exito = aux.insertarElem(elem);
       
       
        return exito;
    }
    public void insertarTS(String alcance)
    {
        nivel++;
        pila.push(new Ts_auxiliar(alcance,nivel));

        
    }
    public void eliminarTS()
    {
        if(!pila.empty())
        {
            pila.pop();
            nivel--;
        }    
        
        
    }
    public String alcanceActual()
    {
       return pila.peek().getAlcance();
    }
     
    public void imprimir()
    {
        for (int i = pila.size(); i >-1; i--) 
        {
            pila.peek().imprimir();
        }
                
    }
    public int getNivelActual()
    {
        return pila.peek().getNivel();
    }
    
}
