/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizador;

import java.util.ArrayList;


/**
 *
 * @author PC
 */
public class Atributos 
{
    private String nombre;
    private String Alcance;
    private String tipo;
    private String tipoDato;
    private String tipoRetorno;
    //ver si es necesario que la parametros tenga el nombre de los parametros
    private ArrayList parametros;

    public Atributos(String elem) 
    {
        String [] arreglo = elem.split("#");
        this.nombre = arreglo[0];
        this.Alcance = arreglo[1];
        this.tipo = arreglo[2];
        if(tipo.equalsIgnoreCase("variable"))
            this.tipoDato = arreglo[3];
        else
        {
            if(!tipo.equalsIgnoreCase("nombre programa"))
            {
                this.parametros = new ArrayList();
                String[] par = arreglo[3].split("&");
                for (int i = 0; i < par.length; i++) 
                {
                    this.parametros.add(par[i]);
                }
           
                if(tipo.equalsIgnoreCase("funcion"))
                {
                    this.tipoRetorno = arreglo [4];
                }
            }    
        }  
    }
    
    public String getNombre() 
    {
        return nombre;
    }

    public String getAlcance()
    {
        return Alcance;
    }

    public String getTipo()
    {
        return tipo;
    }

    public String getTipoDato() 
    {
        return tipoDato;
    }

    public String getTipoRetorno() 
    {
        return tipoRetorno;
    }

    public ArrayList getParametros() 
    {
        return parametros;
    }
    public String getAtributos()
    {
        String cad = this.nombre+" "+this.Alcance+" "+this.tipo;
        if(tipo.equalsIgnoreCase("variable"))
            cad+=" "+this.tipoDato;
        else
        {
            if(!tipo.equalsIgnoreCase("nombre programa"))
            {
                for (int i = 0; i < parametros.size(); i++) 
                {
                    cad+=" "+parametros.get(i);
                }
           
                if(tipo.equalsIgnoreCase("funcion"))
                {
                    cad+=" "+ this.tipoRetorno;
                }
            }    
        } 
        return cad;
    }
    

}
