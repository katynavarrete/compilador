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
    private String posMemoria= "";
    //ver si es necesario que la parametros tenga el nombre de los parametros
    private ArrayList parametros;

    public Atributos(String elem, String posM) 
    {
        String [] arreglo = elem.split("#");
        this.nombre = arreglo[0];
        posMemoria = posM;
        //System.out.println(elem);
        this.Alcance = arreglo[1];
        this.tipo = arreglo[2];
        if(tipo.equalsIgnoreCase("variable"))
            this.tipoDato = arreglo[3];
        else
        {
            if(!tipo.equalsIgnoreCase("nombre programa"))
            {
             
                this.parametros = new ArrayList();
                if((arreglo.length > 4 && tipo.equalsIgnoreCase("funcion")) || (arreglo.length > 3 && tipo.equalsIgnoreCase("procedimiento")) )
                {
                    String[] par = arreglo[3].split("&");
                    for (int i = 0; i < par.length; i++) 
                    {
                        this.parametros.add(par[i]);
                    }
                }
                if(tipo.equalsIgnoreCase("funcion"))
                {
                    if(arreglo.length > 4)
                        this.tipoRetorno = arreglo[4];
                    else
                        this.tipoRetorno = arreglo[3];
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
    public String getPosMemoria()
    {
        // tiene el nivel y las pos en memoria
        return posMemoria;
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
        cad+=" "+ posMemoria;
        return cad;
    }
    public int getCantParametros()
    {
        int cant = -1;
        if(this.parametros != null)
            cant = this.parametros.size();
        return cant;
    }
    

}
