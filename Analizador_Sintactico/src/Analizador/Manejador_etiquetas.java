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
public class Manejador_etiquetas 
{
    private HashMap<String,String> etiquetaProYFun;
    private int indice = 2;
    private Stack<String> pila;
    
    public Manejador_etiquetas()
    {
        this.etiquetaProYFun = new HashMap();
        this.pila = new Stack();
        
    }
    public String agregarFunPro(String nombre)
    {
        String aux=("l"+indice);
        this.etiquetaProYFun.put(nombre.toLowerCase(), aux);
        indice++;
        return aux;
    }
    public String getEtiqueta(String nombre)
    {
        return (this.etiquetaProYFun.get(nombre.toLowerCase()));
    }
    public String generaEtiquetaCondicional()
    {
        String aux= "l"+indice;
        pila.push(aux);
        indice++;
        return aux;
    }
    public String obtenerEtiquetaCondicional()
    {
       return pila.pop();
    }
    
}
