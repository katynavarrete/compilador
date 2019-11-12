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
    private Stack<String[]> pila;
    private Stack <String> posAsignacion;
    public Manejador_etiquetas()
    {
        this.etiquetaProYFun = new HashMap();
        this.pila = new Stack();
        posAsignacion = new Stack();
    }
    public String agregarFunPro(String nombre)
    {
        String aux=("l"+indice);
        this.etiquetaProYFun.put(nombre.toLowerCase(), aux);
        indice++;
        return aux;
    }
    public String getAsignacion()
    { 
        String asi="";
        if(!posAsignacion.isEmpty())
            asi=posAsignacion.peek();
        return asi;
    }
    public void setAsignacion(String pos)
    {
        this.posAsignacion.push(pos);
    }
    public void eliminarPosAsignacion()
    {
        if(!posAsignacion.isEmpty())
            this.posAsignacion.pop();
    }
    public String getEtiqueta(String nombre)
    {
        return (this.etiquetaProYFun.get(nombre.toLowerCase()));
    }
    public String generaEtiquetaCondicionalPrim()
    {
        String [] aux= new String[2];
        aux[0]= "l"+indice;
        aux[1]= "nulo";
        pila.push(aux);
        indice++;
        return aux[0];
    }
    public String generaEtiquetaCondicionalSig()
    {
        String etiqAux ="l"+ indice;
        pila.peek()[1]=etiqAux;
        indice++;
        return etiqAux;
    }
    public String generaEtiquetaCondicionalWhile()
    {
        String [] aux= new String[2];
        aux[0]= "l"+indice;
        aux[1]= "l"+(indice+1);
        pila.push(aux);
        indice+=2;
        return aux[0];
    }
    
    
    public String obtenerEtiquetaCondicionalPrim()
    {
       return ((pila.peek())[0]);
    }
    public String obtenerEtiquetaCondicionalSig()
    {
       return ((pila.peek())[1]);
    }
    public void eliminaEtiquetaCondicional()
    {
        if(!pila.isEmpty())
        {
            pila.pop();
        }
    }
    public String operadorLogico(int op)
    {
        String opLogico="";
        switch(op)
        {
            case 0:
                opLogico="CMME";
            break;
            case 1:
                opLogico="CMIG";
            break;
            case 2:
                opLogico="CMMA";
            break;
            case 3:
                opLogico="CMNI";
            break;
            case 4:
                opLogico="CMYI";
            break;
            case 5:
                opLogico="CMDG";
            break;
                        
        }
        return opLogico;
    }
    
}
