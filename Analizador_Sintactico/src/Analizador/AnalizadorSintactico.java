/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizador;


import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;


/**
 *
 * 
 * @author PC
 */
 public class AnalizadorSintactico 
{
    public static String preanalisis;
    public static String[] linea;
    public static Tabla_de_Simbolos ts;
    public static Stack<Atributos> atributo;
    public static  PrintWriter mepa ;
    public static Manejador_etiquetas manejadorEtiq;
    public  static void main(String args[]) throws IOException
    {
       
      // if(args.length == 1)
       {
          
           ts = new Tabla_de_Simbolos();
           manejadorEtiq = new Manejador_etiquetas();
          //  AnalizadorLexico lexico = new AnalizadorLexico(args[0]);
            atributo = new Stack();
//            posParametro = new Stack();
        AnalizadorLexico lexico = new 
                AnalizadorLexico("C:\\Users\\PC\\Desktop\\laboratorio comp e int\\tp1\\sintactico\\compilador\\"
                        + "Analizador_Sintactico\\src\\Analizador\\test.pas");
        FileWriter fichero = null;
        mepa = null;
        fichero = new FileWriter("C:\\Users\\PC\\Desktop\\laboratorio comp e int\\tp1\\sintactico\\compilador\\"
                        + "Analizador_Sintactico\\src\\Analizador\\int.mepa");
        mepa = new PrintWriter(fichero);
        inicio(lexico);
            
        lexico.cerrarArchivo();
        if(fichero != null)
            fichero.close();
            
           
       }
      /** else
       {
           System.out.println("Falta el parametro del archivo");
       }
       */
    }
    public static void inicio(AnalizadorLexico lexico) throws IOException
    {
        
        //System.out.println("INICIO");
        linea =  lexico.pedirToken().split("#");
        preanalisis = linea[0];
        //System.out.println("token "+preanalisis);
        if(preanalisis.equalsIgnoreCase("token_program"))
	{
            match("token_program",lexico);
            ts.insertarElem((linea[2]+"#global#nombre programa"));
            match("id",lexico);
            mepa.println("INPP");
            match("punto_y_coma",lexico);
            if(preanalisis.equalsIgnoreCase("token_var"))   
		def_variable(lexico,"global");
            if(preanalisis.equalsIgnoreCase("token_function")  ||  preanalisis.equalsIgnoreCase("token_procedure"))
            {   
                mepa.println("DSVS l1");
                
            }
            
            while ( preanalisis.equalsIgnoreCase("token_function")  ||  preanalisis.equalsIgnoreCase("token_procedure"))
            {
                subprograma(lexico,"global");
            }
            sentencia_compuesta(lexico);
            match("punto",lexico);
	}
	else
	{
          //  System.out.println("ERROR EN EL INICIO");
                System.out.println("Error sintactico en la linea "+linea[1]+ " se esperaba PROGRAM");
               System.exit(0);
	}
        
    }
    
    //una posibilidad para manejar el alcance es que se lo mande como parametro 
     public static  void def_variable(AnalizadorLexico lexico,String alcance) throws IOException
    {
        ArrayList aux = new ArrayList();
        String cad = "";
        //System.out.println("DEF_VARIABLE");
        if(preanalisis.equalsIgnoreCase("token_var"))
	{
            match("token_var",lexico);
           // System.out.println(preanalisis);
            while (preanalisis.equalsIgnoreCase("id")) 
            {
                aux.add(linea[2]);
		match("id",lexico);
                mepa.println("RMEM 1");
		while (preanalisis.equalsIgnoreCase("coma"))
		{
                    match("coma",lexico);
                    aux.add(linea[2]);
                    match("id",lexico);
                    mepa.println("RMEM 1");
		}
		match("dos_puntos",lexico);
                int pos = preanalisis.lastIndexOf('_');
                String nomTipo = preanalisis.substring(pos+1);
                int i;
                for ( i = 0; i < aux.size(); i++) 
                {
                 //   System.out.println(aux.get(i));
                    boolean p =ts.insertarElem((aux.get(i))+"#"+alcance+"#variable#"+nomTipo);
                  //  System.out.println(p);
                    if(p == false) 
                    {
                        
                       System.out.println(" en la linea "+linea[1]);
                       System.exit(0);
                    }
                   // cad+= aux.get(i)+"?"+nomTipo+"&";
                }
               
                aux.removeAll(aux);
		tipo(lexico); 
                match("punto_y_coma",lexico);
            }
        
	}
	else
	{
          //  System.out.println("ERROR EN EL DEF_VARIABLE");
            System.out.println("Error sintactico en la linea "+linea[1]+"se espera VAR y se recibe "+linea[2]);
           System.exit(0);
	}
   }
     public static  void subprograma(AnalizadorLexico lexico,String alcancePadre) throws IOException
    {
        //System.out.println("SUBPROGRAMA");
        String nombre="";
        ArrayList parametros = new ArrayList();
        
        switch(preanalisis) 
        {
            case "token_procedure":
                match("token_procedure",lexico); 
                nombre = linea[2];
                
                String alcance = "local en "+nombre;
                match("id",lexico); 
                mepa.println(manejadorEtiq.agregarFunPro(nombre)+" ENPR 1");
               //aca agregar un insertar en la TS
                
                //parametros de la funcion
                if(preanalisis.equalsIgnoreCase("parent_abre") )
                {
                    match("parent_abre",lexico);
                    while (preanalisis.equalsIgnoreCase("id"))
                    { 
                        parametros.add(linea[2]);
                        match("id",lexico);
                   
                        
                        while (preanalisis.equalsIgnoreCase("coma"))
                        {
                            match("coma",lexico);
                            parametros.add(linea[2]);
                            match("id",lexico);
                        }
                        match("dos_puntos",lexico);
                        int pos = preanalisis.lastIndexOf("_");
                        String nomTipo = preanalisis.substring(pos+1);
                        String cadena ="";
                        int i = 0;
                        for ( i = 0; i < parametros.size(); i++) 
                        {
                            cadena+= parametros.get(i)+"@"+nomTipo+"&";
                        }
                        //cadena+= parametros.get(i)+"?"+nomTipo;
                        parametros.removeAll(parametros);
                        tipo(lexico);
                        while(preanalisis.equalsIgnoreCase("punto_y_coma"))
                        {
                            match("punto_y_coma",lexico);
                            parametros.add(linea[2]);
                            match("id",lexico);
                            while (preanalisis.equalsIgnoreCase("coma"))
                            {
                                match("coma",lexico);
                                parametros.add(linea[2]);
                                match("id",lexico);
                            }
                            match("dos_puntos",lexico);
                            pos = preanalisis.lastIndexOf("_");
                            nomTipo = preanalisis.substring(pos+1);
                            for ( i = 0; i < parametros.size()-1; i++) 
                            {
                                cadena+= parametros.get(i)+"@"+nomTipo+"&";
                            }
                            cadena+= parametros.get(i)+"@"+nomTipo;
                            parametros.removeAll(parametros);
                            
                            tipo(lexico);
                        }
                        if(!ts.insertarElem(nombre+"#"+alcancePadre+"#procedimiento#"+cadena))
                        {
                            System.out.println(" en la linea "+linea[1]);
                            System.exit(0);
                        }
                        ts.insertarTS(nombre);
                        
                        String [] parAux = cadena.split("&");
                        for (int j = 0; j < parAux.length; j++) 
                        {
                           
                          //  System.out.println(parAux[j]);
                            int pos1 = parAux[j].lastIndexOf("@");
                           
                            String nomV = parAux[j].substring(0, pos1);
                            String tipoDato = parAux[j].substring(pos1+1);
                          //  System.out.println("nom "+nomV+"    "+tipoDato);
                            if(!ts.insertarElem(nomV+"#"+alcance+"#variable#"+tipoDato))
                            {
                                System.exit(0);
                            }
                        }
                        match("parent_cierra",lexico);  
                    }
                }
                else
                {
                    ts.insertarElem(nombre+"#"+alcancePadre+"#procedimiento");
                    ts.insertarTS(nombre);
                    
                }
                match("punto_y_coma",lexico);
                
                            
                //aca ya se tiene la info para procedure
                if( preanalisis.equalsIgnoreCase("token_var"))
                    def_variable(lexico,alcance);
                while ( preanalisis.equalsIgnoreCase("token_function") || preanalisis.equalsIgnoreCase("token_procedure")) 
                {
                    subprograma(lexico,alcance);
                }
                if(preanalisis.equalsIgnoreCase("token_if") || preanalisis.equalsIgnoreCase("token_read") || 
                    preanalisis.equalsIgnoreCase("token_write") || preanalisis.equalsIgnoreCase("token_while")  || 
		    preanalisis.equalsIgnoreCase("id")) 
                {
                    sentencia_simple(lexico);
                    if(preanalisis.equalsIgnoreCase("punto_y_coma")) 
                        match("punto_y_coma",lexico);
                }   	
                else
                { 
                    sentencia_compuesta(lexico); 
                    match("punto_y_coma",lexico);
                }
                ts.eliminarTS();
               
            break;

            case  "token_function":
                match("token_function",lexico); 
                alcance = "local en "+linea[2];
                nombre = linea[2];
		match("id",lexico); 
                mepa.println(manejadorEtiq.agregarFunPro(nombre)+" ENPR 1");
                String cadena ="";
                boolean conParametros=false;
		if(preanalisis.equalsIgnoreCase("parent_abre") ) 
		{
                    conParametros=true;
                    match("parent_abre",lexico);
                   
                    while (preanalisis.equalsIgnoreCase("id")) 
                    { 
                        parametros.add(linea[2]);
                        match("id",lexico);
                        while (preanalisis.equalsIgnoreCase("coma"))
                        {
                            match("coma",lexico);
                            parametros.add(linea[2]);
                            match("id",lexico);
                        }
                        match("dos_puntos",lexico);
                        int pos = preanalisis.lastIndexOf("_");
                        String nomTipo = preanalisis.substring(pos+1);

                        int i = 0;
                        for ( i = 0; i < parametros.size(); i++) 
                        {
                            cadena+= parametros.get(i)+"@"+nomTipo+"&";
                        }
                        //cadena+= parametros.get(i)+"?"+nomTipo;
                        parametros.removeAll(parametros);
                        tipo(lexico);
                        while(preanalisis.equalsIgnoreCase("punto_y_coma"))
                        {
                            match("punto_y_coma",lexico);
                            parametros.add(linea[2]);
                            match("id",lexico);
                            while (preanalisis.equalsIgnoreCase("coma"))
                            {
                                match("coma",lexico);
                                parametros.add(linea[2]);
                                match("id",lexico);
                            }
                            match("dos_puntos",lexico);
                            pos = preanalisis.lastIndexOf("_");
                            nomTipo = preanalisis.substring(pos+1);
                            i = 0;
                            for ( i = 0; i < parametros.size(); i++) 
                            {
                                cadena+= parametros.get(i)+"@"+nomTipo+"&";
                            }
                        //cadena+= parametros.get(i)+"?"+nomTipo;
                            parametros.removeAll(parametros);
                            
                            tipo(lexico);
                        }
                     match("parent_cierra",lexico);      
                    } 					          
                    
		}
                
                match("dos_puntos",lexico);
                int pos = preanalisis.lastIndexOf("_");
                String nomTipo = preanalisis.substring(pos+1);
               
             //   System.out.println("");
                if(conParametros )
                {
                    if( !ts.insertarElem(nombre+"#"+alcancePadre+"#funcion#"+cadena+"#"+nomTipo))
                    {
                        System.out.println(" --------- en la linea "+linea[1]);
                        System.exit(0);
                    }
                    
                }
                else
                {
                   if(!ts.insertarElem(nombre+"#"+alcancePadre+"#funcion#"+nomTipo))
                    {
                        ts.imprimir();
                        System.out.println("********* en la linea "+linea[1]);
                        System.exit(0);
                    }
                }
                    
                ts.insertarTS(nombre);
                
                String [] parAux = cadena.split("&");
                if(!parAux[0].equalsIgnoreCase(""))
                {
                    for (int j = 0; j < parAux.length; j++) 
                    {
                 
                        int pos1 = parAux[j].lastIndexOf("@");
                        
                        String nomV = parAux[j].substring(0, pos1);
                        String tipoDato = parAux[j].substring(pos1+1);
                    //  System.out.println("nom "+nomV+"    "+tipoDato);
                        if(!ts.insertarElem(nomV+"#"+alcance+"#variable#"+tipoDato))
                        {
                           
                            System.exit(0);
                        }
                    }
                }    
               // ts.imprimir();
                tipo(lexico);
		match("punto_y_coma",lexico);
		if( preanalisis.equalsIgnoreCase("token_var"))
                    def_variable(lexico,alcance);
                while ( preanalisis.equalsIgnoreCase("token_function") || preanalisis.equalsIgnoreCase("token_procedure")) 
                {
                    subprograma(lexico,alcance);
                }
                if(preanalisis.equalsIgnoreCase("token_if") || preanalisis.equalsIgnoreCase("token_read") || 
		   preanalisis.equalsIgnoreCase("token_write") || preanalisis.equalsIgnoreCase("token_while")
		 ||  preanalisis.equalsIgnoreCase("id")) 
		{
                    sentencia_simple(lexico);
                    if(preanalisis.equalsIgnoreCase("punto_y_coma")) 
                        match("punto_y_coma",lexico);
		}	
		else
		{ 
                    sentencia_compuesta(lexico); 
                    
                    match("punto_y_coma",lexico);
                   
		}
                    
                    ts.eliminarTS();
                    
		break;
                default: 
                {
          //          System.out.println("ERROR EN EL SUBPROGRAMA");
                    System.out.println("Error de sintaxis en la linea "+linea[1]
                            +" se espera procedure o fuction y se recibe "+linea[2]);
                   System.exit(0);
                }    
		break;
	}	
    }

    public static void match(String terminal, AnalizadorLexico lexico) throws IOException
    {
        
        if(terminal.equalsIgnoreCase(preanalisis))
        {
            String aux = lexico.pedirToken();
            linea=aux .split("#");
            preanalisis = linea[0];
          //  System.out.println(preanalisis+" "+linea[2]);
            if(terminal.equalsIgnoreCase("punto"))
            {
                if(preanalisis.equalsIgnoreCase(""))
                    System.out.println("NO SE DECTARON ERRORES :)");
                else
                    System.out.println("ERROR SINTACTICO EN LA LINEA "+linea[1]
                            +" se encontraron lineas de codigo luego de un punto");
            }
            else
            {
                if(preanalisis.equalsIgnoreCase("error") )
                {
               
                    lexico.cerrarArchivo();
                    
                    System.exit(0);
                
                }
              /*  else
                {
                    if(preanalisis.equalsIgnoreCase(""))
                    {
                        System.out.println("ERROR SINTACTICO se espera "+terminal+" "+lexico.getLinea());
                         System.exit(0);
                    }
                }*/
            }  
        }
        else
        {
            if(terminal.equalsIgnoreCase("token_end"))
            {
                System.out.println("Error sintactico en la linea "+linea[1] + " se espera una expresion y se recibe  end");
            }
            else
            {
                if(preanalisis.equalsIgnoreCase(""))
                   {
                        System.out.println("ERROR SINTACTICO se espera "+terminal+" "+lexico.getLinea());
                         System.exit(0);
                    }
                else
                {
                    System.out.println("Error sintactico en la linea "+linea[1] + " se espera "+terminal+" y se recibe "+linea[2]);
            
                }
            }
            lexico.cerrarArchivo();
           System.exit(0);
        }
    }

     public static   void sentencia_simple(AnalizadorLexico lexico) throws IOException 
    {
        //System.out.println("SENTENCIA_SIMPLE");
        switch(preanalisis) 
        {
            case  "token_if":
                sent_condicional(lexico);
                
            break;
            case  "token_write":
                sent_E_S(lexico);
            break;
            case  "token_read":
                sent_E_S(lexico);
            break;
            case  "token_while":
		sent_repetitiva(lexico);
            break;
            case "id":
                Atributos aux = ts.verificarElem(linea[2]);
                if(aux != null)
                {   
                    //System.out.println(atributo.toString());
                    atributo.push(aux);
                 //   posParametro.push(0);
                    //System.out.println("id "+linea[2]);
                    
                    match("id",lexico);
                    
                    sentencia_simple1(lexico);
                    
                }
                else
                {
                    System.out.println("ERROR SEMANTICO se esta usando "+ linea[2]+" no definida, en la linea "+linea[1]);
                    System.exit(0);
                }
            break;
            default: 
            //    System.out.println("ERROR EN EL SENTENCIA_SIMPLE");
                System.out.println("Error de sintaxis en la linea "+linea[1] 
                        +" se espera UN IF O WRITE O READ O WHILE O UN ID y se recibe "+linea[2] );
               System.exit(0);
            break;
        }
    }


     //este es para id () llama a funion o a procediemto
     //conciderar que hay funciones o procedimientos que no tienen parametros
     //o id:= asignacion
     
     public static   void sentencia_simple1(AnalizadorLexico lexico) throws IOException 
    {
        //System.out.println("SENTENCIA_SIMPLE1");
	switch(preanalisis) 
        {
            case "parent_abre":
                
                if(!atributo.peek().getTipo().equalsIgnoreCase("procedimiento"))
                {
                    System.out.println("ERROR SEMANTICO SE ESTA USANDO LA " +atributo.peek().getTipo()+" "+
                            atributo.peek().getNombre()+
                            " COMO SI FUERA UN PROCEDIMIENTO EN LA LINEA "+linea[1]);
                     System.exit(0);
                }
                else{
                match("parent_abre",lexico); 
                  
                if(!atributo.isEmpty() && atributo.peek().getTipo().equalsIgnoreCase("procedimiento") && 
                        atributo.peek().getCantParametros()>0)
                {    
                   
                    String tipoExp = expresion(lexico);
                    int posAux=0;
                    String tipoPara = "";
                    
                 //  System.out.println(atributo.peek().getCantParametros());
                 // if(tipoPara.equalsIgnoreCase(tipoExp))
                    { 
                        while (preanalisis.equalsIgnoreCase("coma"))
                        {   
                            if(posAux < (atributo.peek()).getCantParametros())
                            {
                                String parametro =(String) atributo.peek().getParametros().get(posAux);
                               // System.out.println(parametro);
                                int pos = parametro.indexOf("@");
                                 tipoPara = parametro.substring(pos+1);
                                 
                                if(!tipoPara.equalsIgnoreCase(tipoExp))
                                {
                                    System.out.println("ERROR SEMANTICO EN LA LINEA "+linea[1]+" "+atributo.peek().getNombre()+" requeria un paratemetro de tipo "
                                    +tipoPara+" y se recibio un "+tipoExp);
                                    System.exit(0);
                                }
                              
                                match("coma",lexico);
                                tipoExp = expresion(lexico);
                                
                                posAux++;
                            }
                            else
                            {
                                System.out.println("ERROR SEMANTICO para  "+
                                atributo.peek().getNombre()+ " se requieren "+atributo.peek().getCantParametros()+
                                " parametros en la linea "+linea[1] );
                                System.exit(0);
                            }
                        }
                        if( atributo.peek().getCantParametros()>0  )
                        {
                            if(posAux < atributo.peek().getCantParametros())
                            {
                                String parametro =(String) atributo.peek().getParametros().get(posAux);
                                int pos = parametro.indexOf("@");
                                tipoPara = parametro.substring(pos+1);
                                posAux++;
                            }    
                            else
                            {
                                System.out.println("ERROR SEMANTICO EN LA LINEA "+linea[1]+" "+atributo.peek().getNombre()+" requeria "
                                +atributo.peek().getCantParametros());
                                System.exit(0);
                            }
                           
                        }
                        if(!tipoPara.equalsIgnoreCase(tipoExp))
                        {
                            System.out.println("ERROR SEMANTICO EN LA LINEA "+linea[1]+" "+atributo.peek().getNombre()+" requeria un paratemetro de tipo "
                            +tipoPara+" y se recibio un "+tipoExp);
                            System.exit(0);
                        }
                        
                       
                        if(posAux == (atributo.peek().getCantParametros()))
                        {
                            match("parent_cierra",lexico);
                            
                            atributo.pop();
                        }
                        else
                        {
                            System.out.println("ERROR SEMANTICO se utilizaron "+ (posAux)+
                            " parametros  y se requerian "+atributo.peek().getCantParametros()+" en la linea "+linea[1]);
                            System.exit(0);
                        }
                    }
                
                }  
                else
                {
                    if(!atributo.peek().getTipo().equalsIgnoreCase("procedimiento"))
                        System.out.println("ERROR SEMANTICO SE QUIERE USAR UNA VARIABLE COMO FUNCION O PROCEDIMIENTO EN LA LINEA "+linea[1]);
                    else
                        System.out.println("ERROR SEMANTICO EN LA LINEA "+linea[1]+" EL PROCEDIMIENTO "+
                                atributo.peek().getNombre()+" no requiere parametros");
                    System.exit(0);
                }
        }    
            break;
            case "asignacion":
                
                if(!atributo.isEmpty() && 
                   ((atributo.peek().getTipo().equalsIgnoreCase("funcion") && 
                        !atributo.peek().getNombre().equalsIgnoreCase(ts.alcanceActual()))||
                    atributo.peek().getTipo().equalsIgnoreCase("procedimiento") ))
                {
                    //System.out.println(atributo.peek().getAtributos());
                   
                    System.out.println("ERROR SEMANTICO en la linea "+linea[1]+" se le esta haciendo una asignacion a " +
                            atributo.peek().getTipo());
                    System.exit(0);
                }
		match("asignacion",lexico); 
              //  puedeF = true;
		String tipoExp = expresion(lexico);
                if(atributo.peek().getTipo().equalsIgnoreCase("variable")&&
                  !tipoExp.equalsIgnoreCase(atributo.peek().getTipoDato()))
                {
                    if(tipoExp.equalsIgnoreCase(""))
                    {
                        System.out.println("ERROR SEMANTICO en la linea "+linea[1]
                                +" SE QUIERE ASIGNAR A UNA VARIABLE DE TIPO "
                        +atributo.peek().getTipoDato()+" UN PROCEDIMIENTO ");
                        System.exit(0);
                    }
                    System.out.println("ERROR SEMANTICO en la linea "+linea[1]+" SE QUIERE ASIGNAR A UNA VARIABLE DE TIPO "
                    +atributo.peek().getTipoDato()+" UNA EXPRESION DE TIPO "+tipoExp);
                    System.exit(0);
                }
              //  puedeF=false;
                atributo.pop();
               // posParametro.pop();
            break;
            default:
                
                if((atributo.peek().getTipo().equalsIgnoreCase("funcion")||atributo.peek().getTipo().equalsIgnoreCase("procedimiento"))
                        &&
                        atributo.peek().getCantParametros()!= 0)
                
                {
                     System.out.println("ERROR SEMANTICO para  "+
                                atributo.peek().getNombre()+ " se requieren "+atributo.peek().getCantParametros()+
                             " parametros y no se recibe ninguno ");
                        System.exit(0);
                }   
                if(!atributo.peek().getTipo().equalsIgnoreCase("procedimiento"))
                {
                    System.out.println("ERROR SEMANTICO SE ESTA USANDO LA " +atributo.peek().getTipo()
                           +" " + atributo.peek().getNombre()+
                            " COMO SI FUERA UN PROCEDIMIENTO EN LA LINEA "+linea[1]);
                     
                    System.exit(0);
                }
                
            
            break;
	}
       
    }


     public static   void sentencia_compuesta(AnalizadorLexico lexico) throws IOException 
    {
        //System.out.println("SENTENCIA_COMPUESTA");
        if(preanalisis.equalsIgnoreCase("token_begin"))
	{
            
            boolean seguir = true;
            match("token_begin",lexico); 
            while((preanalisis.equalsIgnoreCase("token_if")  ||  preanalisis.equalsIgnoreCase("token_read")  ||  
	       preanalisis.equalsIgnoreCase("token_write")  ||  preanalisis.equalsIgnoreCase("token_while")  ||
	       preanalisis.equalsIgnoreCase("id") || preanalisis.equalsIgnoreCase("token_begin"))&&seguir)
            {
                if(preanalisis.equalsIgnoreCase("token_begin"))
                    sentencia_compuesta(lexico);
                else
                    sentencia_simple(lexico);
                if(preanalisis.equalsIgnoreCase("punto_y_coma"))
                    
                    match("punto_y_coma",lexico);
                else
                   seguir = false;
            }
          /**
           * como mandar los msj de errores para cuando hay expresion sin ; seguida de otra exp
           * 
           */
          if(!seguir && !preanalisis.equalsIgnoreCase("token_end"))
          {
              if(preanalisis.equalsIgnoreCase("punto_y_coma"))
              {
                  System.out.println("ERROR SINTACTICO EN LA LINEA "+linea[1]+" se espera expresion o un end");
                  System.exit(0);
              }
              else
              {
                  System.out.println("ERROR SINTACTICO EN LA LINEA "+linea[1]+" se espera ; o  end");
                  System.exit(0);
              }
                  
          }    
          else
          {
              
              match("token_end",lexico);
              
          }
	}
        else
        {
          //  System.out.println("ERROR EN SENTENCIA COMPUESTA");
            System.out.println("Error de sintaxis en la linea "+linea[1] +" se espera BEGIN y se recibe "+linea[2]);
            System.exit(0);
        }    
}

    public static   void sent_condicional(AnalizadorLexico lexico) throws IOException 
    {
        
       
        if(preanalisis.equalsIgnoreCase("token_if"))
        {
            String tipoExp;
            match( "token_if",lexico);
            tipoExp = expresion(lexico);
         
            if(!tipoExp.equalsIgnoreCase("boolean"))
            {
                System.out.println("ERROR SEMANTICO SE ESPERABA UNA EXPESION BOOLEANA PARA LA SENTENCIA IF EN LA LINEA "+linea[1]);
                System.exit(0);
            }
            match("token_then",lexico);
            mepa.println("DSVF "+manejadorEtiq.generaEtiquetaCondicional());
            
            sent_condicional1(lexico);
        }
        else
        {
          //  System.out.println("ERROR EN EL SENT_CONDICIONAL");
	    System.out.println("Error de sintaxis en la linea "+linea[1] +" se espera IF y se recibe "+linea[2]);
           System.exit(0);
        }  
    }

     public static   void sent_condicional1(AnalizadorLexico lexico) throws IOException 
    {
        //System.out.println("SENT_CONDICIONAL1");
        if ( preanalisis.equalsIgnoreCase("token_if")  ||  preanalisis.equalsIgnoreCase("token_read")  || 
             preanalisis.equalsIgnoreCase("token_write")  ||  preanalisis.equalsIgnoreCase("token_while") || 
             preanalisis.equalsIgnoreCase("id") ) 
        {
            sentencia_simple(lexico);
            if(preanalisis.equalsIgnoreCase("token_else") )
            {
               manejadorEtiq.generaEtiquetaCondicional();
            }
            mepa.println("DSVS "+manejadorEtiq.obtenerEtiquetaCondicional());
            sent_condicional2(lexico);
        }
        else
        {
            if(preanalisis.equalsIgnoreCase("token_begin"))
            {
                
                sentencia_compuesta(lexico);
                if(preanalisis.equalsIgnoreCase("token_else") )
                {
                   manejadorEtiq.generaEtiquetaCondicional();
                }
                mepa.println("DSVS "+manejadorEtiq.obtenerEtiquetaCondicional());
                sent_condicional3(lexico);
            }
            else
            {
          //      System.out.println("ERROR EN sent_condicional1");
                System.out.println("Error de sintaxis en la linea "+linea[1] 
                        +" se espera IF, READ, WRITE,WHILE O BEGIN y se recibe "+linea[2]);
               System.exit(0);
            }
        }
	
    }


 public static   void sent_condicional2(AnalizadorLexico lexico) throws IOException 
{
        //System.out.println("SENT_CONDICIONAL2");
	switch(preanalisis)
	{
		
		case  "token_else" :
                        
			match("token_else",lexico);
                        mepa.println(manejadorEtiq.obtenerEtiquetaCondicional()+" NADA");
                        
			sent_condicional4(lexico);
		break;
		
	}
}


 public static   void sent_condicional4(AnalizadorLexico lexico) throws IOException{

       // System.out.println("SENT_CONDICIONAL4");
	if ( preanalisis.equalsIgnoreCase("token_if")  ||  preanalisis.equalsIgnoreCase("token_read")  || 
	      preanalisis.equalsIgnoreCase("token_write")  ||  preanalisis.equalsIgnoreCase("token_while") ||
	      preanalisis.equalsIgnoreCase("id")) 
	{
		sentencia_simple(lexico);
		
	}
	else
	{
	    if(preanalisis.equalsIgnoreCase("token_begin"))
	    {
                sentencia_compuesta(lexico);
		//match("punto_y_coma",lexico);
	    }
            else
            {
         //       System.out.println("ERROR EN SENT_CONDICIONAL4");
                System.out.println("Error de sintaxis en la linea " +linea[1]
                        +"se espera IF, READ, WRITE, WHILE, ID O BEGIN y se recibe "+linea[2]);
               System.exit(0);
            }
            
        }
}





 public static   void sent_condicional3(AnalizadorLexico lexico) throws IOException{

      //  System.out.println("SENT_CONDICIONAL3");
	if(preanalisis.equalsIgnoreCase("punto_y_coma"))
        {
		//match("punto_y_coma",lexico);
               
        }
	else
	{
	       if(preanalisis.equalsIgnoreCase("token_else"))
	       {
		match("token_else",lexico);
                mepa.println(manejadorEtiq.obtenerEtiquetaCondicional()+" NADA");
                manejadorEtiq.generaEtiquetaCondicional();
		sent_condicional4(lexico);
	       }
               else
               {
        //           System.out.println("ERROR EN SENT_CONDICIONAL3");
		    System.out.println("Error de sintaxis  en la linea "+linea[1]
                            +" se espera ; O ELSE y se recibe "+linea[2]);
                   System.exit(0);
               }
               
        }
}



 public static void sent_repetitiva(AnalizadorLexico lexico) throws IOException 
 {
    // System.out.println("SENT_REPETITIVA");
       if(preanalisis.equalsIgnoreCase("token_while"))
       {
	match("token_while",lexico);
	String tipoExp = expresion(lexico);
        if(!tipoExp.equalsIgnoreCase("boolean"))
        {
            System.out.println("ERROR SEMANTICO SE ESPERABA UNA EXPRESION DE TIPO BOOLEAN PARA LA SENTENCIA WHILE EN LA LINEA "+linea[1]);
        }
	match("token_do",lexico);
	if ( preanalisis.equalsIgnoreCase("token_if")  ||  preanalisis.equalsIgnoreCase("token_read")  || 
	      preanalisis.equalsIgnoreCase("token_write")  ||  preanalisis.equalsIgnoreCase("token_while") ||
	      preanalisis.equalsIgnoreCase("id")) 
	{
		sentencia_simple(lexico);
		if (preanalisis.equalsIgnoreCase("punto_y_coma")) 
			match("punto_y_coma",lexico);
	}
	else
	{	
		sentencia_compuesta(lexico);
		 match("punto_y_coma",lexico); 
	}
        }
       else
       {
      //     System.out.println("ERROR EN SENT_REPETITIVA");
	       System.out.println("Error de sintaxis en la linea "+linea[1]+" se espera WHILE y se recibe "+linea[2]);
              System.exit(0);
       }       
}


 public static   void  sent_E_S(AnalizadorLexico lexico) throws IOException 
{
    //System.out.println("SENT_E_S");
	if (preanalisis.equalsIgnoreCase("token_write") ) 
	{
		match("token_write",lexico);
		match("parent_abre",lexico); 
                if(preanalisis.equalsIgnoreCase("id"))
                {
                    Atributos a= ts.verificarElem(linea[2]);
                    if(a != null && a.getTipo().equalsIgnoreCase("procedimiento"))
                    {
                        System.out.println("ERROR SEMANTICO SE QUIERE IMPRIMIR UN PROCEDIMIENTO EN LA LINEA "+linea[1]);
                        System.exit(0);
                    }
                }
		expresion(lexico);
		match("parent_cierra",lexico);
	}
	else
	{
	       if(preanalisis.equalsIgnoreCase("token_read"))
               {
                    match("token_read",lexico);  
                    match("parent_abre",lexico); 
                    String aux = linea[2];
                    match("id",lexico);
                    
                    Atributos a= ts.verificarElem(aux);
                    if(a!=null)
                    {
                        if(!a.getTipo().equalsIgnoreCase("variable"))
                        {
                            System.out.println("ERROR SEMANTICO SE COLOCO UNA FUNCION O PROCEDIMIENTO EN UN READ EN LA LINEA "
                            +linea[1]);
                            System.exit(0);
                        }
                        
                    }
                    else
                    {
                        System.out.println("ERROR SEMANTICO SE QUIERE USAR "
                                +aux+" EN LA LINEA "+linea[1]+" LA CUAL NO ESTA DECLARADA");
                        System.exit(0);
                    }
                   match("parent_cierra",lexico);
	       }
               else
               {
                   
      //          System.out.println("ERROR EN SENT_CONDICIONAL4");
                System.out.println("Error de sintaxis en la linea "+linea[1] 
                        +" se espera WRITE O READ y se recibe "+linea[2]);
               System.exit(0);
               }
        }       
}


 public static String expresion(AnalizadorLexico lexico) throws IOException {
    String aux= "", aux2;
     if(preanalisis.equalsIgnoreCase("token_true")  ||  preanalisis.equalsIgnoreCase("token_false")  
             ||  preanalisis.equalsIgnoreCase("token_resta") ||         	
             preanalisis.equalsIgnoreCase("token_num")	||  preanalisis.equalsIgnoreCase("id") 
             ||  preanalisis.equalsIgnoreCase("parent_abre") ||     
             preanalisis.equalsIgnoreCase("token_not"))
     {
	aux = exp1(lexico); 
       
	aux2 = expresion1(lexico);
        if(!aux2.equalsIgnoreCase("") && !aux.equalsIgnoreCase("boolean"))
            {
                System.out.println("ERROR SEMANTICO SE ESPERA UN TIPO DE DATO BOOLEAN PARA REALIZAR LA OPERACION OR EN LA LINEA "
                            +linea[1]);
                    System.exit(0);
            }
        
     }
     else
     {
      //   System.out.println("ERROR EN EXPRESION");
	       System.out.println("Error de sintaxis en la linea "+linea[1]
                       +" se espera TRUE, FALSE, -, NUM, ID, ( o NOT y se recibe "+linea[2]);
              System.exit(0);
     }
     return aux;
 }     


 public static String expresion1(AnalizadorLexico lexico) throws IOException  
 {
        String aux="",aux2;
	if (preanalisis.equalsIgnoreCase("token_or") ) 
	{
            match("token_or",lexico); 
            aux = exp1(lexico); 
            aux2 = expresion1(lexico);
            if(!aux2.equalsIgnoreCase("") && !aux.equalsIgnoreCase("boolean"))
            {
                System.out.println("ERROR SEMANTICO SE ESPERA UN TIPO DE DATO BOOLEAN PARA REALIZAR LA OPERACION OR EN LA LINEA "
                            +linea[1]);
                    System.exit(0);
            }
	}
        return aux;
}

 public static String exp1(AnalizadorLexico lexico) throws IOException  
 {
        String aux="", aux2;
        if(preanalisis.equalsIgnoreCase("token_not")  ||  preanalisis.equalsIgnoreCase("token_true")
             ||  preanalisis.equalsIgnoreCase("token_false")  ||preanalisis.equalsIgnoreCase("token_resta")
             ||  preanalisis.equalsIgnoreCase("token_num") ||  preanalisis.equalsIgnoreCase("id")
             || preanalisis.equalsIgnoreCase("parent_abre") )
        {
            aux = exp2(lexico); 
           
            aux2 = exp11(lexico);
            if(!aux2.equalsIgnoreCase("") && !aux.equalsIgnoreCase("boolean"))
            {
                System.out.println("ERROR SEMANTICO SE ESPERA UN TIPO DE DATO BOOLEAN PARA REALIZAR LA OPERACION AND EN LA LINEA "
                            +linea[1]);
                    System.exit(0);
            }
        }
        else
        {
     //    System.out.println("ERROR EN EXP1");
            System.out.println("Error de sintaxis en la linea "+linea[1]
                 +" se espera NOT, TRUE, FALSE, -, NUM, ID O ( y se recibe "+linea[2]);
            System.exit(0);
        }
        return aux; 
}


 public static String exp11(AnalizadorLexico lexico) throws IOException  {
//System.out.println("EXP11");
        String aux = "";
	if (preanalisis.equalsIgnoreCase("token_and")) 
	{
		match( "token_and",lexico);
		aux = exp2(lexico); 
                if(!aux.equalsIgnoreCase("boolean"))
                {
                    System.out.println("ERROR SEMANTICO SE ESPERA UN TIPO DE DATO BOOLEAN PARA REALIZAR LA OPERACION AND EN LA LINEA "
                            +linea[1]);
                    System.exit(0);
                }
		exp11 (lexico);
	}
	return aux;
}


 public static String exp2(AnalizadorLexico lexico) throws IOException  
 {
        String aux="";
        
	if (preanalisis.equalsIgnoreCase("token_not"))  
	{
		match("token_not",lexico);
		aux = exp3(lexico);
                if(!aux.equalsIgnoreCase("boolean"))
                {
                    System.out.println("ERROR SEMANTICO SE ESPERABA UN BOOLEAN PARA LA OPERACION NOT Y SE RECIBE "+aux+" en la linea "+linea[1]);
                    System.exit(0);
                }
                
	}
	else 
	{
	      if(preanalisis.equalsIgnoreCase("token_true")  ||  preanalisis.equalsIgnoreCase("token_false")  || 
		 preanalisis.equalsIgnoreCase("token_resta")  ||  preanalisis.equalsIgnoreCase("token_num") || 
                 preanalisis.equalsIgnoreCase("id")  ||  preanalisis.equalsIgnoreCase("parent_abre"))
		
                aux = exp3(lexico);
         
              else
              {
     //            System.out.println("ERROR EN EXP2");
		     System.out.println("Error de sintaxis en la linea "+linea[1]
                             +" se espera NOT, TRUE, FALSE,- NUM,ID O ( y se recibe "+linea[2]);
                    System.exit(0);
              }
        }
        return aux;
}

 public static String exp3(AnalizadorLexico lexico) throws IOException
 {
    String aux="",aux2="";
    if(preanalisis.equalsIgnoreCase("token_true")  ||  preanalisis.equalsIgnoreCase("token_false")  ||
        preanalisis.equalsIgnoreCase("token_resta")  || preanalisis.equalsIgnoreCase("token_num") ||
             preanalisis.equalsIgnoreCase("id")  ||
             preanalisis.equalsIgnoreCase("parent_abre"))
    {
        aux = exp4(lexico);
        
        aux2 = exp31(lexico,aux);
        if(aux2.equalsIgnoreCase(""))
        {
            aux2 = aux;
        }
    }    
    else
    {
       //  System.out.println("ERROR EN EXP3");
        System.out.println("Error de sintaxis  en la linea "+linea[1]
                       +" se espera TRUE, FALSE, -, NUM, ID O ( y se recibe "+linea[2]);
        System.exit(0);
    }
    return aux2;
 }    


 public static String  exp31 (AnalizadorLexico lexico, String aux1) throws IOException  
 {
    String aux = "", aux2;
    boolean soloEnt = true;
    if (preanalisis.equalsIgnoreCase("token_mayor")  ||  preanalisis.equalsIgnoreCase("token_menor") ||  
        preanalisis.equalsIgnoreCase("token_igual") ||  preanalisis.equalsIgnoreCase("token_mayorI") ||
        preanalisis.equalsIgnoreCase("token_menorI") ||  preanalisis.equalsIgnoreCase("token_distinto"))
    {
       
           
        if (preanalisis.equalsIgnoreCase("token_menor"))
            match("token_menor",lexico);
        if (preanalisis.equalsIgnoreCase("token_igual"))
        {
            match("token_igual",lexico);
            soloEnt = false;
        }    
        if (preanalisis.equalsIgnoreCase("token_mayor"))
            match("token_mayor",lexico);
        if(preanalisis.equalsIgnoreCase("token_menorI"))
            match("token_menorI",lexico);
        if(preanalisis.equalsIgnoreCase("token_mayorI"))
            match("token_mayorI",lexico);
        if(preanalisis.equalsIgnoreCase("token_distinto"))
            match("token_distinto",lexico);
 
        aux = exp4(lexico);
        if(!aux.equalsIgnoreCase(aux1))
        {
            System.out.println("ERROR SEMANTICO SE COMPARAN DATOS DE DISTINTO TIPO EN LA LINEA "+linea[1]);
            System.exit(0);
        }
        else
        {
            
            if(soloEnt && !aux.equalsIgnoreCase("integer"))
            {
                System.out.println("ERROR SEMANTICO SE COMPARAN DATOS DE DISTINTO TIPO EN LA LINEA "+linea[1]);
                System.exit(0);
            }
        }
        
        if(!aux.equalsIgnoreCase(""))
        {
            aux = "boolean";
        }
        aux2 = exp31(lexico,aux);
    }
    
   return aux;
 }        

 public static String exp4(AnalizadorLexico lexico) throws IOException  
{
    String aux ="",aux2;
    if(preanalisis.equalsIgnoreCase("token_true")  ||  preanalisis.equalsIgnoreCase("token_false")  ||
       preanalisis.equalsIgnoreCase("token_resta") || preanalisis.equalsIgnoreCase("token_num") ||
       preanalisis.equalsIgnoreCase("id")  ||  preanalisis.equalsIgnoreCase("parent_abre") )
    {
        aux = termino(lexico);
        aux2 = exp41(lexico);
        
        if(!aux2.equalsIgnoreCase("") && !aux.equalsIgnoreCase("integer"))
        {
               System.out.println("ERROR SEMANTICO se quiere realizar una operacion de "+aux2+" entre "+aux+
                       " y un entero en la linea "+linea[1]);
               System.exit(0);
        }
     }
    else
    {
    //    System.out.println("ERROR EN EXP4");
	       System.out.println("Error de sintaxis  en la linea "+linea[1]
                       + " se espera TRUE, FALSE,-, NUM, ID O ( y se recibe "+linea[2]);
              System.exit(0);
    }
    return aux;
}

 public static String exp41(AnalizadorLexico lexico) throws IOException  
{
    String retorno = "", aux;
    if (preanalisis.equalsIgnoreCase("token_suma")) 
	{
            retorno = "suma";
            match("token_suma",lexico); 
            aux = termino(lexico);
            if(!aux.equalsIgnoreCase("integer"))
            {
                System.out.println("ERROR SEMANTICO se esperaba un entero para realizar una suma en la linea "+linea[1]);
                System.exit(0);
            }
            exp41(lexico); 
	}
	else
	{
            if (preanalisis.equalsIgnoreCase("token_resta")) 
            {
                retorno = "resta";
		match("token_resta",lexico); 
		aux = termino(lexico);
                if(!aux.equalsIgnoreCase("integer"))
                {
                    System.out.println("ERROR SEMANTICO se esperaba un entero para realizar una resta en la linea "+linea[1]);
                    System.exit(0);
                }
		exp41(lexico);
            }
	}
    return retorno;
}


 public static String termino(AnalizadorLexico lexico) throws IOException  
{
    String aux1="", aux2;
    if(preanalisis.equalsIgnoreCase("token_true")  ||  preanalisis.equalsIgnoreCase("token_false")  ||
       preanalisis.equalsIgnoreCase("token_resta")  ||preanalisis.equalsIgnoreCase("token_num")	||
       preanalisis.equalsIgnoreCase("id")  ||  preanalisis.equalsIgnoreCase("parent_abre"))
    {
        aux1 = factor(lexico); 

	aux2 = termino1(lexico);
        
        if(!aux2.equalsIgnoreCase(""))
        {
            if(!aux1.equalsIgnoreCase("integer"))
            {
                    System.out.println("ERROR SEMANTICO se quiere realizar una operacion de "+aux2+" entre "+aux1+
                            "  y un entero en la linea "+linea[1]);
                System.exit(0);
            }
            else
            {
                aux1 = "integer";
            }
        }
    }
    else
    {
       // System.out.println("ERROR EN TERMINO");
	System.out.println("Error de sintaxis en la linea " +linea[1]
                +" se espera TRUE, FALSE, -, NUM, ID,( y se recibe " + linea[2]);
       System.exit(0);
    }
    return aux1;
}    


 public static String termino1(AnalizadorLexico lexico) throws IOException 
{
    String aux = "", retorno = "";
    if (preanalisis.equalsIgnoreCase("token_multi"))
    {
	match("token_multi",lexico); 
	aux = factor(lexico); 
        if(!aux.equalsIgnoreCase("integer"))
        {
            System.out.println("ERROR SEMANTICO se esperaba un entero para realizar la multiplicacion en la linea "+linea[1]);
            System.exit(0);
        }
        retorno = "multiplicacion";
	termino1(lexico); 
    }    
    else
    {
	if (preanalisis.equalsIgnoreCase("token_div")) 
        {
            match("token_div",lexico);
            aux = factor(lexico);
            if(!aux.equalsIgnoreCase("integer"))
            {
                System.out.println("ERROR SEMANTICO se esperaba un entero para realizar la division en la linea "+linea[1]);
                System.exit(0);
            }
            retorno = "division";
            termino1(lexico);
	}
    }
    return retorno;
}


 public static String factor(AnalizadorLexico lexico) throws IOException  
{
    
    String tipoExp = "";
   
    switch(preanalisis) 
    {
	case  "token_true":
            tipoExp = "boolean";
            match("token_true",lexico);
            mepa.println("APCT 1");
	break;
	case  "token_false":
            tipoExp = "boolean";
            match("token_false",lexico);
            mepa.println("APCT 0");
	break;
        case  "token_resta":
            match("token_resta",lexico); 
            factor1(lexico);
            mepa.println("UMEN");
            
           tipoExp = "integer";
	break;
	case "id":
            Atributos aux = (ts.verificarElem(linea[2]));
            if(aux != null)
            {   
          
                match("id",lexico);
                if(aux.getTipo().equalsIgnoreCase("funcion"))
                {
                    
                    atributo.push(aux);
                   // posParametro.push(0);
                   if(!preanalisis.equalsIgnoreCase("parent_abre") && atributo.peek().getCantParametros()!=0)
                   {
                       System.out.println("ERROR SEMANTICO LA FUNCION "+atributo.peek().getNombre()
                               +" REQUIERE "+atributo.peek().getCantParametros()+" parametros");
                       System.exit(0);
                     
                   }
                    
                }
                if(!aux.getTipo().equalsIgnoreCase("procedimiento"))
                {
                   
                    factor2(lexico);
                    if(aux.getTipo().equalsIgnoreCase("funcion"))
                    {
                        tipoExp = aux.getTipoRetorno();
                    }    
                    else
                    {
                        if(aux.getTipo().equalsIgnoreCase("variable"))
                        {
                            tipoExp = aux.getTipoDato();
                            mepa.println("APVL "+aux.getPosMemoria());
                            
                        }
                    }
                }
                else
                {
                    System.out.println("ERROR SEMANTICO SE QUIERE USAR UN PROCEDIMIENTO COMO UNA FUNCION O VARIABLE EN LA LINEA "
                    +linea[1]);
                    System.exit(0);
                }
                        
            }
            else
            {
                System.out.println("ERROR SEMANTICO "+linea[2]+" en la linea "+linea[1]);
                System.exit(0);
            }
	break;
	case  "token_num":
            tipoExp = "integer";
            mepa.println("APCT "+linea[2]);
            match("token_num",lexico);
	break;
	case "parent_abre" :
            match("parent_abre",lexico);
            tipoExp = expresion(lexico);
           // System.out.println(tipoExp);
            match("parent_cierra",lexico);
	break;
	default: 
            System.out.println("Error de sintaxis en la linea "+linea[1]
                    +" se espera: TRUE, FALSE, -, NUM, ID,( y se recibe "+linea[2]);
            System.exit(0);
            break;
    }
    
    return tipoExp;
}


 public static void factor1(AnalizadorLexico lexico) throws IOException  
{
    switch(preanalisis) 
    {
	case  "token_num":
            mepa.println("APCT "+linea[2]);
            match("token_num",lexico);
	break;
	case "id":
            Atributos aux =ts.verificarElem(linea[2]);
            if(aux!= null )
            {
                if(aux.getTipo().equalsIgnoreCase("variable")&& !aux.getTipoDato().equalsIgnoreCase("integer"))
                {
                    System.out.println("ERROR SEMANTICO SE ESPERABA UN INTEGER PARA LA RESTA Y SE USO "+aux.getTipoDato());
                    System.exit(0);
                }
                else
                {
                    if(aux.getTipo().equalsIgnoreCase("funcion")&& !aux.getTipoRetorno().equalsIgnoreCase("integer"))
                    {
                        System.out.println("ERROR SEMANTICO SE ESPERABA UN INTEGER PARA LA RESTA Y SE USO LA FUNCION "
                                +linea[2]+" LA CUAL RETORNA UN "+aux.getTipoRetorno()+" EN LA LINEA "+linea[1]);
                        System.exit(0);
                    }
                    if(aux.getTipo().equalsIgnoreCase("procedimiento"))
                    {
                        System.out.println("ERROR SEMANTICO SE ESPERABA UN INTEGER PARA LA RESTA Y SE USO EL PROCEDIMIENTO "
                                +linea[2]+" EN LA LINEA "+linea[1]);
                    System.exit(0);
                    }
                }
                match("id",lexico);
               
            } 
            else
            {
                System.out.println("ERROR SEMANTICO SE QUIERE USAR "+linea[2]+" LA CUAL NO ESTA DECLARADA");
                System.exit(0);
            }
           
	break;
	case "parent_abre" :
            match("parent_abre",lexico);
            String tipoExp= expresion(lexico); 
            if(!tipoExp.equalsIgnoreCase("integer"))
            {
                System.out.println("ERROR SEMANTICO SE ESPERA UNA EXPRESION DE TIPO INTEGER Y SE USA EXPRESION DE TIPO "
                        +tipoExp+" EN LA LINEA "+linea[1]);
                System.exit(0);
            }
            match("parent_cierra",lexico);
	break;
	default:
          // System.out.println("ERROR EN factor1");
            System.out.println("Error de sintaxis en la linea "+linea[1]+" se espera  NUM, ID,( y se recibe "+linea[2]);
            System.exit(0);
        break;
           
    }

}
public static void factor2(AnalizadorLexico lexico) throws IOException
{
    
    
    if(preanalisis.equalsIgnoreCase("parent_abre"))
    {
	match("parent_abre",lexico);
        if(!atributo.isEmpty() && 
                (!atributo.peek().getTipo().equalsIgnoreCase("variable")) && atributo.peek().getCantParametros()>0)
        {
            
            String tipoExp=expresion(lexico);
            
            String tipoPara = "";
            int posAux=0;
            { 
                while (preanalisis.equalsIgnoreCase("coma"))
                {   
                    if(posAux < (atributo.peek()).getCantParametros())
                    {
                        String parametro =(String) atributo.peek().getParametros().get(posAux);
                           
                        int pos = parametro.indexOf("@");
                        tipoPara = parametro.substring(pos+1);
                        if(!tipoPara.equalsIgnoreCase(tipoExp))
                        {
                            System.out.println("ERROR SEMANTICO EN LA LINEA "+linea[1]+" "
                                    +atributo.peek().getNombre()+" requeria un paratemetro de tipo "
                            +tipoPara+" y se recibio un "+tipoExp);
                            System.exit(0);
                        }
                              
                        match("coma",lexico);
                        tipoExp = expresion(lexico);
                            
                        posAux++;
                    }
                    else
                    {
                        System.out.println("ERROR SEMANTICO para  "+
                        atributo.peek().getNombre()+ " se requieren "+atributo.peek().getCantParametros()+
                        " parametros en la linea "+linea[1] );
                        System.exit(0);
                    }
                }
                if(atributo.peek().getCantParametros() > 0 )
                {
                    if(posAux < atributo.peek().getCantParametros())
                    {
                        String parametro =(String) atributo.peek().getParametros().get(posAux);
                        int pos = parametro.indexOf("@");
                        tipoPara = parametro.substring(pos+1);
                    //    posAux++;
                    }
                    else
                    {
                        System.out.println("ERROR SEMANTICO EN LA LINEA "+linea[1]+" "+atributo.peek().getNombre()+" requeria "
                                +atributo.peek().getCantParametros());
                        System.exit(0);
                    }
                    
                          
                }
               
                if(!tipoPara.equalsIgnoreCase(tipoExp))
                {
                    System.out.println("ERROR SEMANTICO EN LA LINEA "+linea[1]+" "+atributo.peek().getNombre()+" requeria un paratemetro de tipo "
                    +tipoPara+" y se recibio un "+tipoExp);
                    System.exit(0);
                }
                else
                {
                    posAux++;
                }
                       
                if(posAux == (atributo.peek().getCantParametros()))
                {
                    match("parent_cierra",lexico);
                        
                    atributo.pop();
                }
                else
                {
                    System.out.println("ERROR SEMANTICO se utilizaron "+ (posAux)+
                    " parametros  y se requerian "+atributo.peek().getCantParametros()+" en la linea "+linea[1]);
                    System.exit(0);
                }
            }
                 /*   else
                    {
                        System.out.println("ERROR SEMANTICO EN LA LINEA "+linea[1]+" "+atributo.peek().getNombre()+" requeria un paratemetro de tipo "
                        +tipoPara+" y se recibio un "+tipoExp);
                        System.exit(0);
                    }*/
        }
        else
        {
            if(atributo.peek().getTipo().equalsIgnoreCase("variable"))
            {
                System.out.println("ERROR SEMANTICO QUIERE USAR LA VARIABLE "+ atributo.peek().getNombre()+
                        " COMO UNA FUNCION O PROCEDIMIENTO EN LA LINEA "+linea[1]);
               
            }
            else
                System.out.println("ERROR SEMANTICO EN LA LINEA "+linea[1]+" "+
                       atributo.peek().getNombre()+" no requiere parametros" );
            System.exit(0);   
        }
    }
}

 public static   void tipo(AnalizadorLexico lexico) throws IOException  
{
    //System.out.println("TIPO");
    switch(preanalisis) 
    {
	case  "token_integer":
            match("token_integer",lexico);
	break;
	case  "token_boolean":
            match("token_boolean",lexico);
	break;
	default:
      //      System.out.println("ERROR EN tipo");
            System.out.println("Error de sintaxis en la linea " +linea[1]+" se espera un TIPO y se recibe "+linea[2]);
           System.exit(0);
            break;
    }
}

}

