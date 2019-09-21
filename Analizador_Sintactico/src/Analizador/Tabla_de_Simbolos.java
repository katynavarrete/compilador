/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author PC
 */
public class Tabla_de_Simbolos 
{
    private HashMap<String, ArrayList> tabla;
    
    public Tabla_de_Simbolos()
    {
        tabla = new HashMap();
        
    }
    public void insertar(String elem)
    {
        boolean exito = false;
        String [] arreglo = elem .split("#");
        if(!tabla.containsKey(arreglo[0]))
        {
            Tipo tipo = new Tipo(elem);
            ArrayList nueva = new ArrayList();
            nueva.add(tipo);
            tabla.put(arreglo[0],nueva);
        }
        else
        {
            ArrayList aux = tabla.get(arreglo[0]);
            boolean existe= false;
            int j = 0;
            while(!existe && j < aux.size())
            {
                String alc = ((Tipo)aux.get(j)).getAlcance();
                if(arreglo[1].equalsIgnoreCase(alc))
                {
                    existe = true;
                }
                j++;
            }
            if(!existe)
            {
                Tipo tipo = new Tipo(elem);
                aux.add(tipo);
            }
        }
    }
}
