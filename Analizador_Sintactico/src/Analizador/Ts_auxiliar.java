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
public class Ts_auxiliar 
{
    private String alcance;
    private int pos;
    private int nivel;
    private HashMap<String,Atributos> diccionario;

    public Ts_auxiliar(String alcance, int nivel) 
    {
        this.alcance = alcance;
        this.nivel = nivel;
        diccionario = new HashMap();
        pos=0;
    }

    public String getAlcance()
    {
        return alcance;
    }
    public int getNivel() 
    {
        return nivel;
    }
    public int getPos() 
    {
        return pos;
    }
    public Atributos getAtributos(String clave)
    {
        return diccionario.get(clave.toLowerCase());
    }
    public boolean verificarElem(String clave)
    {
        boolean existe= false;
        if(diccionario.containsKey(clave.toLowerCase()))
        {
            existe = true;
        }
        return existe;
    }
    public boolean insertarElem(String elem)
    {
        boolean exito = true;
        
        String [] arreglo = elem .split("#");
        String nombre = arreglo[0].toLowerCase();
        String aux=""+nivel;
                
        if(!diccionario.containsKey(nombre.toLowerCase()) || nombre.equalsIgnoreCase(alcance))
        {
            
            if(arreglo[2].equalsIgnoreCase("variable") )
            {
                if( arreglo.length == 5)
                {
                    aux+=","+arreglo[4];
                }
                else
                {
                    aux+= ","+pos;
                    pos++;
                    
                }
            }
            
            Atributos atributo = new Atributos(elem, (aux));
           
            diccionario.put(nombre, atributo);
          //  this.imprimir();
        }
        else
        {
            exito=false;
            System.out.print("ERROR SEMANTICO: SE QUIERE DECLARAR "+nombre+ " LA CUAL ESTA DECLARADA "  );
                
            
        }
     return exito;
    }
    
    public void imprimir()
    {
      
      
        diccionario.forEach((k,v)-> System.out.println(((Atributos)v).getAtributos()));
        System.out.println("------------------------------");
                
    }
    
}
