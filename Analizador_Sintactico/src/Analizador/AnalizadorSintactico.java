/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizador;


import java.io.FileReader;
import java.io.IOException;


/**
 *
 * @author PC
 */
 public class AnalizadorSintactico 
{
     public static String preanalisis;
     public static String[] linea;
     public  static void main(String args[]) throws IOException
    {
     //  if(args.length == 1)
       {
           
            //AnalizadorLexico lexico = new AnalizadorLexico(args[0]);
            AnalizadorLexico lexico = new 
AnalizadorLexico("C:\\Users\\PC\\Desktop\\laboratorio comp e int\\tp1\\sintactico\\compilador\\Analizador_Sintactico\\src\\Analizador\\ejemplo_2.pas");
            inicio(lexico);
            lexico.cerrarArchivo();
           
       }
      /** else
       {
           System.out.println("Falta el parametro del archivo");
       }*/
    }
    public static void inicio(AnalizadorLexico lexico) throws IOException
    {
        //System.out.println("INICIO");
        linea =  lexico.pedirToken().split("#");
        preanalisis = linea[0];
        System.out.println("token "+preanalisis);
        if(preanalisis.equalsIgnoreCase("token_program"))
	{
            match("token_program",lexico);
            match("id",lexico);
            match("punto_y_coma",lexico);
            if(preanalisis.equalsIgnoreCase("token_var"))   
		def_variable(lexico);
            //sigue aca
            while ( preanalisis.equalsIgnoreCase("token_function")  ||  preanalisis.equalsIgnoreCase("token_procedure"))
            {
                subprograma(lexico);
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
     public static  void def_variable(AnalizadorLexico lexico) throws IOException
    {
        //System.out.println("DEF_VARIABLE");
        if(preanalisis.equalsIgnoreCase("token_var"))
	{
            match("token_var",lexico);
           // System.out.println(preanalisis);
            while (preanalisis.equalsIgnoreCase("id")) 
            { 
		match("id",lexico);
		while (preanalisis.equalsIgnoreCase("coma"))
		{
                    match("coma",lexico);
                    match("id",lexico);
		}
		match("dos_puntos",lexico);
               
		tipo(lexico); 
		match("punto_y_coma",lexico);
            }
	}
	else
	{
          //  System.out.println("ERROR EN EL DEF_VARIABLE");
            System.out.println("Error sintactico en la linea "+linea[1]+"se espera VAR y se recibe "+preanalisis);
           System.exit(0);
	}
   }
     public static   void subprograma(AnalizadorLexico lexico) throws IOException
    {
        //System.out.println("SUBPROGRAMA");
        switch(preanalisis) 
        {
            case "token_procedure":
                match("token_procedure",lexico); 
                match("id",lexico); 
                if(preanalisis.equalsIgnoreCase("parent_abre") )
                {
                    match("parent_abre",lexico);
                    while (preanalisis.equalsIgnoreCase("id"))
                    { 
                        match("id",lexico);
                   
                        
                        while (preanalisis.equalsIgnoreCase("coma"))
                        {
                            match("coma",lexico);
                            match("id",lexico);
                        }
                        match("dos_puntos",lexico);
                        tipo(lexico);
                        while(preanalisis.equalsIgnoreCase("punto_y_coma"))
                        {
                            match("punto_y_coma",lexico);
                            match("id",lexico);
                            while (preanalisis.equalsIgnoreCase("coma"))
                            {
                                match("coma",lexico);
                                match("id",lexico);
                            }
                            match("dos_puntos",lexico);
                            tipo(lexico);
                        }
                        match("parent_cierra",lexico);  
                    }
                }
                match("punto_y_coma",lexico);
                if( preanalisis.equalsIgnoreCase("token_var"))
                    def_variable(lexico);
                while ( preanalisis.equalsIgnoreCase("token_function") || preanalisis.equalsIgnoreCase("token_procedure")) 
                {
                    subprograma(lexico);
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
            break;

            case  "token_function":
                match("token_function",lexico); 
		match("id",lexico); 
		if(preanalisis.equalsIgnoreCase("parent_abre") ) 
		{
                    match("parent_abre",lexico);
                   
                    while (preanalisis.equalsIgnoreCase("id")) 
                    { 
                        match("id",lexico);
                        while (preanalisis.equalsIgnoreCase("coma"))
                        {
                            match("coma",lexico);
                            match("id",lexico);
                        }
                        match("dos_puntos",lexico);
                        tipo(lexico);
                        while(preanalisis.equalsIgnoreCase("punto_y_coma"))
                        {
                            match("punto_y_coma",lexico);
                            match("id",lexico);
                            while (preanalisis.equalsIgnoreCase("coma"))
                            {
                                match("coma",lexico);
                                match("id",lexico);
                            }
                            match("dos_puntos",lexico);
                            tipo(lexico);
                        }
                     match("parent_cierra",lexico);      
                    } 					          
                    
		}
                match("dos_puntos",lexico);
                tipo(lexico);
		match("punto_y_coma",lexico);
		if( preanalisis.equalsIgnoreCase("token_var"))
                    def_variable(lexico);
                while ( preanalisis.equalsIgnoreCase("token_function") || preanalisis.equalsIgnoreCase("token_procedure")) 
                {
                    subprograma(lexico);
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
		break;
                default: 
                {
          //          System.out.println("ERROR EN EL SUBPROGRAMA");
                    System.out.println("Error de sintaxis en la linea "+linea[1]+" se espera procedure o fuction y se recibe "+preanalisis);
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
           
            if(terminal.equalsIgnoreCase("punto"))
            {
                if(preanalisis.equalsIgnoreCase(""))
                    System.out.println("NO SE DECTARON ERRORES SINTACTICO ");
                else
                    System.out.println("ERROR SINTACTICO EN LA LINEA "+linea[1]+" se encontraron lineas de codigo luego de un punto");
            }
            else
            {
                if(preanalisis.equalsIgnoreCase("error") ||preanalisis.equalsIgnoreCase("") )
                {
               
                    lexico.cerrarArchivo();
                    System.exit(0);
                
                }
            }  
        }
        else
        {
            System.out.println("Error sintactico en la linea "+linea[1] + " se espera "+terminal+" y se recibe "+preanalisis);
            //System exit corta la ejecuion del main  0 es de manera exitosa y 1 significa que ubo algun error 
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
		match("id",lexico);
		sentencia_simple1(lexico);
            break;
            default: 
            //    System.out.println("ERROR EN EL SENTENCIA_SIMPLE");
                System.out.println("Error de sintaxis en la linea "+linea[1] 
                        +" se espera UN IF O WRITE O READ O WHILE O UN ID y se recibe "+preanalisis );
               System.exit(0);
            break;
        }
    }


     public static   void sentencia_simple1(AnalizadorLexico lexico) throws IOException 
    {
        //System.out.println("SENTENCIA_SIMPLE1");
	switch(preanalisis) 
        {
            case "parent_abre":
                match("parent_abre",lexico); 
		expresion(lexico);
		while (preanalisis.equalsIgnoreCase("coma"))
		{ 
                    match("coma",lexico);
                    expresion(lexico);
		}
		match("parent_cierra",lexico);
            break;
            case "asignacion":
		match("asignacion",lexico); 
		expresion(lexico);
            break;
	}
    }


     public static   void sentencia_compuesta(AnalizadorLexico lexico) throws IOException 
    {
        //System.out.println("SENTENCIA_COMPUESTA");
        if(preanalisis.equalsIgnoreCase("token_begin"))
	{
            match("token_begin",lexico); 
            if(preanalisis.equalsIgnoreCase("token_if")  ||  preanalisis.equalsIgnoreCase("token_read")  ||  
	       preanalisis.equalsIgnoreCase("token_write")  ||  preanalisis.equalsIgnoreCase("token_while")  ||
	       preanalisis.equalsIgnoreCase("id"))
            {
                sentencia_simple(lexico);
            }
            else
            {
                sentencia_compuesta(lexico);
            }
            while(preanalisis.equalsIgnoreCase("punto_y_coma"))
            {
		match("punto_y_coma",lexico);
		if(preanalisis.equalsIgnoreCase("token_if")  ||  preanalisis.equalsIgnoreCase("token_read")  || 
                    preanalisis.equalsIgnoreCase("token_write")  ||  preanalisis.equalsIgnoreCase("token_while")  || 
                    preanalisis.equalsIgnoreCase("id"))
		{
                    sentencia_simple(lexico);
		}
		else
		{
                    if(preanalisis.equalsIgnoreCase("token_begin"))
                        sentencia_compuesta(lexico);
		}
            }
           // sentencia_simple(lexico);
            if (preanalisis.equalsIgnoreCase("punto_y_coma"))  
                match("punto_y_coma",lexico);
            match("token_end",lexico);
	}
        else
        {
          //  System.out.println("ERROR EN SENTENCIA COMPUESTA");
            System.out.println("Error de sintaxis en la linea "+linea[1] +" se espera BEGIN y se recibe "+preanalisis);
            System.exit(0);
        }    
}

     public static   void sent_condicional(AnalizadorLexico lexico) throws IOException 
    {
        //System.out.println("SENT_CONDICIONAL");
        if(preanalisis.equalsIgnoreCase("token_if"))
        {
            match( "token_if",lexico);
            expresion(lexico);
            match("token_then",lexico); 
            sent_condicional1(lexico);
        }
        else
        {
          //  System.out.println("ERROR EN EL SENT_CONDICIONAL");
	    System.out.println("Error de sintaxis en la linea "+linea[1] +" se espera IF y se recibe "+preanalisis);
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
            sent_condicional2(lexico);
        }
        else
        {
            if(preanalisis.equalsIgnoreCase("token_begin"))
            {
                sentencia_compuesta(lexico);
                sent_condicional3(lexico);
            }
            else
            {
          //      System.out.println("ERROR EN sent_condicional1");
                System.out.println("Error de sintaxis en la linea " +" FALTA IF, READ, WRITE,WHILE O BEGIN");
               System.exit(0);
            }
        }
	
    }


 public static   void sent_condicional2(AnalizadorLexico lexico) throws IOException 
{
        //System.out.println("SENT_CONDICIONAL2");
	switch(preanalisis)
	{
		case "punto_y_coma" :
			match("punto_y_coma",lexico);
		break;
		case  "token_else" :
			match("token_else",lexico);
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
		sent_condicional5(lexico);
	}
	else
	{
	    if(preanalisis.equalsIgnoreCase("token_begin"))
	    {
                sentencia_compuesta(lexico);
		match("punto_y_coma",lexico);
	    }
            else
            {
         //       System.out.println("ERROR EN SENT_CONDICIONAL4");
                System.out.println("Error de sintaxis en la linea " + linea[1]+"FALTA IF, READ, WRITE, WHILE, ID O BEGIN");
               System.exit(0);
            }
            
        }
}


 public static   void sent_condicional5(AnalizadorLexico lexico) throws IOException
{
    //System.out.println("SENT_CONDICIONAL5");
    if(preanalisis.equalsIgnoreCase("punto_y_coma"))
	match("punto_y_coma",lexico);
}


 public static   void sent_condicional3(AnalizadorLexico lexico) throws IOException{

      //  System.out.println("SENT_CONDICIONAL3");
	if(preanalisis.equalsIgnoreCase("punto_y_coma"))
		match("punto_y_coma",lexico);
	else
	{
	       if(preanalisis.equalsIgnoreCase("token_else"))
	       {
		match("token_else",lexico);
		sent_condicional4(lexico);
	       }
               else
               {
        //           System.out.println("ERROR EN SENT_CONDICIONAL3");
		    System.out.println("Error de sintaxis  en la linea " +" FALTA ; O ELSE");
                   System.exit(0);
               }
               
        }
}



 public static   void sent_repetitiva(AnalizadorLexico lexico) throws IOException {
    // System.out.println("SENT_REPETITIVA");
       if(preanalisis.equalsIgnoreCase("token_while"))
       {
	match("token_while",lexico);
	expresion(lexico);
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
	       System.out.println("Error de sintaxis en la linea " +" FALTA WHILE");
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
		expresion(lexico);
		match("parent_cierra",lexico);
	}
	else
	{
	       if(preanalisis.equalsIgnoreCase("token_read"))
	      {
		match("token_read",lexico);  
		match("parent_abre",lexico); 
		match("id",lexico);
		match("parent_cierra",lexico);
	       }
               else
               {
                   
      //          System.out.println("ERROR EN SENT_CONDICIONAL4");
                System.out.println("Error de sintaxis en la linea " +" FALTA WRITE O READ");
               System.exit(0);
               }
        }       
}


 public static   void expresion(AnalizadorLexico lexico) throws IOException {
    // System.out.println("EXPRESION");
     if(preanalisis.equalsIgnoreCase("token_true")  ||  preanalisis.equalsIgnoreCase("token_false")  ||  preanalisis.equalsIgnoreCase("token_resta") ||         	
             preanalisis.equalsIgnoreCase("token_num")	||  preanalisis.equalsIgnoreCase("id")   ||  preanalisis.equalsIgnoreCase("parent_abre") ||     
             preanalisis.equalsIgnoreCase("token_not"))
     {
	exp1(lexico); 
	expresion1(lexico);
     }
     else
     {
      //   System.out.println("ERROR EN EXPRESION");
	       System.out.println("Error de sintaxis en la linea " +" FALTA TRUE, FALSE, -, NUM, ID, ( o NOT");
              System.exit(0);
     }
 }     


 public static   void expresion1(AnalizadorLexico lexico) throws IOException  {
    // System.out.println("EXPRESION1");
	if (preanalisis.equalsIgnoreCase("token_or") ) 
	{
		match("token_or",lexico); 
		exp1(lexico); 
		expresion(lexico);
	}
}

 public static   void exp1(AnalizadorLexico lexico) throws IOException  {
//System.out.println("EXP1");
     if(preanalisis.equalsIgnoreCase("token_not")  ||  preanalisis.equalsIgnoreCase("token_true")
             ||  preanalisis.equalsIgnoreCase("token_false")  ||preanalisis.equalsIgnoreCase("token_resta")
             ||  preanalisis.equalsIgnoreCase("token_num") ||  preanalisis.equalsIgnoreCase("id")
             || preanalisis.equalsIgnoreCase("parent_abre") )
    {
	exp2(lexico); 
	exp11(lexico);
     }
     else
     {
     //    System.out.println("ERROR EN EXP1");
         System.out.println("Error de sintaxis en la linea " +" FALTA NOT, TRUE, FALSE, -, NUM, ID O (");
	System.exit(0);
     }
         
}


 public static   void exp11(AnalizadorLexico lexico) throws IOException  {
//System.out.println("EXP11");
	if (preanalisis.equalsIgnoreCase("token_and")) 
	{
		match( "token_and",lexico);
		exp2(lexico); 
		exp11 (lexico);
	}
	
}


 public static   void exp2(AnalizadorLexico lexico) throws IOException  {
//System.out.println("EXP2");
	if (preanalisis.equalsIgnoreCase("token_not"))  
	{
		match("token_not",lexico);
		exp3(lexico);
	}
	else 
	{
	      if(preanalisis.equalsIgnoreCase("token_true")  ||  preanalisis.equalsIgnoreCase("token_false")  || 
		 preanalisis.equalsIgnoreCase("token_resta")  ||  preanalisis.equalsIgnoreCase("token_num") || 
                 preanalisis.equalsIgnoreCase("id")  ||  preanalisis.equalsIgnoreCase("parent_abre"))
		
		exp3(lexico);
              else
              {
     //            System.out.println("ERROR EN EXP2");
		     System.out.println("Error de sintaxis en la linea " +" FALTA NOT, TRUE, FALSE,- NUM,ID 0 (");
                    System.exit(0);
              }
        }      
}


 public static   void exp3(AnalizadorLexico lexico) throws IOException{
//System.out.println("EXP3");
     if(preanalisis.equalsIgnoreCase("token_true")  ||  preanalisis.equalsIgnoreCase("token_false")  ||
        preanalisis.equalsIgnoreCase("token_resta")  ||	preanalisis.equalsIgnoreCase("token_num") || 
             preanalisis.equalsIgnoreCase("id")  ||
             preanalisis.equalsIgnoreCase("parent_abre"))
     {
	exp4(lexico);
	exp31(lexico);
     }
     else
     {
       //  System.out.println("ERROR EN EXP3");
	       System.out.println("Error de sintaxis  en la linea " +" FALTA TRUE, FALSE, -, NUM, ID O (");
              System.exit(0);
     }
 }     


 public static   void exp31 (AnalizadorLexico lexico) throws IOException  {
//System.out.println("EXP31");
	if (preanalisis.equalsIgnoreCase("token_mayor")  ||  preanalisis.equalsIgnoreCase("token_menor") ||  
	     preanalisis.equalsIgnoreCase("token_igual") ||  preanalisis.equalsIgnoreCase("token_mayorI") ||
		preanalisis.equalsIgnoreCase("token_menorI") ||  preanalisis.equalsIgnoreCase("token_distinto")) 
	{
		if (preanalisis.equalsIgnoreCase("token_menor"))  
			match("token_menor",lexico);
		if (preanalisis.equalsIgnoreCase("token_igual")) 
			match("token_igual",lexico); 
		if (preanalisis.equalsIgnoreCase("token_mayor"))
			match("token_mayor",lexico);
		if(preanalisis.equalsIgnoreCase("token_menorI"))
			match("token_menorI",lexico);
		if(preanalisis.equalsIgnoreCase("token_mayorI"))
			match("token_mayorI",lexico);
		if(preanalisis.equalsIgnoreCase("token_distinto"))
			match("token_ditinto",lexico);
		exp4(lexico);
		exp31(lexico);
        }
        
 }        

 public static   void exp4(AnalizadorLexico lexico) throws IOException  
{
  //  System.out.println("EXP4");
    if(preanalisis.equalsIgnoreCase("token_true")  ||  preanalisis.equalsIgnoreCase("token_false")  ||
       preanalisis.equalsIgnoreCase("token_resta") || preanalisis.equalsIgnoreCase("token_num") ||
       preanalisis.equalsIgnoreCase("id")  ||  preanalisis.equalsIgnoreCase("parent_abre") )
    {
        termino(lexico);
	exp41(lexico);
     }
    else
    {
    //    System.out.println("ERROR EN EXP4");
	       System.out.println("Error de sintaxis  en la linea " + " FALTA TRUE, FALSE,-, NUM, ID O (");
              System.exit(0);
    }
    
}

 public static   void exp41(AnalizadorLexico lexico) throws IOException  
{
    //System.out.println("EXP41");
    if (preanalisis.equalsIgnoreCase("token_suma")) 
	{
		match("token_suma",lexico); 
		termino(lexico); 
		exp41(lexico); 
	}
	else
	{
            if (preanalisis.equalsIgnoreCase("token_resta")) 
            {
		match("token_resta",lexico); 
		termino(lexico);
		exp41(lexico);
            }
	}
}


 public static   void termino(AnalizadorLexico lexico) throws IOException  
{
    //System.out.println("TERMINO");
    if(preanalisis.equalsIgnoreCase("token_true")  ||  preanalisis.equalsIgnoreCase("token_false")  ||
       preanalisis.equalsIgnoreCase("token_resta")  ||preanalisis.equalsIgnoreCase("token_num")	||
       preanalisis.equalsIgnoreCase("id")  ||  preanalisis.equalsIgnoreCase("parent_abre"))
    {
        factor(lexico); 
	termino1(lexico); 
    }
    else
    {
       // System.out.println("ERROR EN TERMINO");
	System.out.println("Error de sintaxis en la linea " +linea[1]+" FALTA TRUE, FALSE, -, NUM, ID,( " + preanalisis);
       System.exit(0);
    }
}    


 public static   void termino1(AnalizadorLexico lexico) throws IOException 
{
    //System.out.println("TERMINO1");
    if (preanalisis.equalsIgnoreCase("token_multi"))
    {
	match("token_multi",lexico); 
	factor(lexico); 
	termino1(lexico); 
    }    
    else
    {
	if (preanalisis.equalsIgnoreCase("token_div")) 
        {
            match("token_div",lexico);
            factor(lexico);
            termino1(lexico);
	}
    }
}


 public static   void factor(AnalizadorLexico lexico) throws IOException  
{
   // System.out.println("FACTOR " + preanalisis);
    switch(preanalisis) 
    {
	case  "token_true":
            match("token_true",lexico);
	break;
	case  "token_false":
            match("token_false",lexico);
	break;
	case  "token_resta":
            match("token_resta",lexico); 
            factor1(lexico);
	break;
	case "id":
            match("id",lexico);
            factor2(lexico);
	break;
	case  "token_num":
            match("token_num",lexico);
	break;
	case "parent_abre" :
            match("parent_abre",lexico);
            expresion(lexico);
            match("parent_cierra",lexico);
	break;
	default: 
      //      System.out.println("ERROR EN FACTOR");
            System.out.println("Error de sintaxis en la linea "+linea[1]+" FALTA: TRUE, FALSE, -, NUM, ID,(");
           System.exit(0);
            break;
    }
}


 public static   void factor1(AnalizadorLexico lexico) throws IOException  
{
    //System.out.println("FACTOR1");
    switch(preanalisis) 
    {
	case  "token_num":
            match("token_num",lexico);
	break;
	case "id":
            match("id",lexico);
	break;
	case "parent_abre" :
            match("parent_abre",lexico);
            expresion(lexico); 
            match("parent_cierra",lexico);
	break;
	default:
          // System.out.println("ERROR EN factor1");
           System.out.println("Error de sintaxis en la linea "+linea[1]+" FALTA  NUM, ID,(");
          System.exit(0);
           break;
    }

}
 public static   void factor2(AnalizadorLexico lexico) throws IOException
{
    //System.out.println("FACTOR2");
    if(preanalisis.equalsIgnoreCase("parent_abre"))
    {
	match("parent_abre",lexico);
	expresion(lexico);
	while(preanalisis.equalsIgnoreCase("coma"))
	{
            match("coma",lexico);
            expresion(lexico);
	}
	match("parent_cierra",lexico);
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
            System.out.println("Error de sintaxis en la linea " +linea[1]+" FALTA EL TIPO");
           System.exit(0);
            break;
    }
}
}

