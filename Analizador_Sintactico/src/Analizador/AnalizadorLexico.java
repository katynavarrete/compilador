/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizador;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *falta ver como tratamos el error ya sea modo panico aa$bb daria dos id aa y bb
 * o simplemente cortar porque ocurrio un fallo
 * falta corregir el numero de la linea
 * @author PC
 */
public class AnalizadorLexico 
{
    private static boolean error;
    private HashMap palabrasReservadas;
    private int linea;
    private int posicion = 1;
    private File archivo = null;
    int ind = 0;
    private  RandomAccessFile fr = null;;
    
    public AnalizadorLexico(String nombreArc) throws FileNotFoundException
    {
        palabrasReservadas = new HashMap();
        cargarPalabras();
        archivo = new File(nombreArc);
        fr = new  RandomAccessFile(archivo,"r");
         linea = 1;  

    }
    public void cerrarArchivo() throws IOException
    {
        /*System.out.println("CERRAR ARCHIVO");
        if( null != fr )
             fr.close(); */
        

    }
    public String pedirToken() throws IOException 
    {
        int letra1 = fr.read();
        char letra =(char)letra1;
        String token = "";
        error = false;
//        System.out.println(linea);
       
        if(letra1 != -1 && ind < fr.length())
        {
            
            
            switch(letra) 
            {
                case ';': 
                    token = "punto_y_coma#"+linea+"#"+letra;
                break;
                case '(': 
                    token = "parent_abre#"+linea+"#"+letra;
                break;
                case ')': 
                    token = "parent_cierra#"+linea+"#"+letra;
                break;
                case ',': 
                    token = "coma#"+linea+"#"+letra;
                break;
                case '.': 
                    token = "punto#"+linea+"#"+letra;
                break;
                case '+': 
                    token = "token_suma#"+linea+"#"+letra;
                break;
                case '-': 
                    token = "token_resta#"+linea+"#"+letra;
                break;
                case '*': 
                    token = "token_multi#"+linea+"#"+letra;
                break;
                case '/': 
                    token = "token_div#"+linea+"#"+letra;
                break;
                case '=':
                    token = "token_igual#"+linea+"#"+letra;
                break;
                case '<': 
                    char t = (char)fr.read();
                    if( t == '=')
                    {
                        token = "token_menorI#"+linea+"#<=";
                        ind++;
                    }
                    else
                    {
                        if( t == '>')
                        {
                            token = "token_distinto#"+linea+"#<>";
                            ind++;
                        }
                        else
                            token = "token_menor#"+linea+"#<";
                    }
                       
                break;
                case '>': 
                    if( (char)fr.read() == '=')
                    {
                        token = "token_mayorI#"+linea+"#>=";
                        ind++;
                    }
                    else
                    {
                        token = "token_mayor#"+linea+"#>";
                    }
                break;
                
                case ':': 
                    if((char)fr.read() == '=')
                    {
                        token = "asignacion#"+linea+"#:=";
                        ind++;
                    }
                    else
                    {
                        token = "dos_puntos#"+linea+"#:";
                    }
                break;
                case '{': 
                    int aux2 = linea;
                    int aux3 = posicion;
                    int aux = fr.read();
                    ind++;
                    while((char)aux != '}' && aux != -1)
                    {
                        if(aux == 10)
                        {
                           
                            linea++;
                            posicion=0;
                        }    
                        ind++;
                      //  System.out.println(aux);
                        aux = fr.read();
                        
                    } 
                    if(aux == -1)
                    {
                        System.out.println("ERROR LEXICO: comentario que comienza en la linea "+aux2+" no cerró");
                        token="error#"+linea;
                        this.cerrarArchivo();
                    }
                    else
                    {
                        if((char)aux == '}')
                        {
                      // System.out.println("encontro la llave que termina "+linea);
                        ind++;
                        token = this.pedirToken();
                        ind--;
                        //System.out.println("***********"+token+" "+letra1);
                        }
                    }
                   // ind++;
                break;
                case '}':
                    System.out.println("ERROR LEXICO: } en la linea "+linea);
                    error = true;
                    token="error#"+linea;
                    this.cerrarArchivo();
                break;
                case ' ': 
                    ind++;
                    token = this.pedirToken();
                    ind--;
                    //Si se lee un espacio no se realiza ninguna acción, pero se toma en cuenta que después de un espacio
                    //puede haber alguna palabra reservada, identificador, etc.
                break;
                    //Debido a que los números pueden estar conformados por más de un dígito, se invoca el método
                    //reconoceNum para reconocer todos los dígitos correspondientes
                case '0': 
                    token=reconoceNum();
                break;
                case '1': 
                    token=reconoceNum();
                break;
                case '2': 
                    token=reconoceNum();
                break;
                case '3': 
                    token=reconoceNum();
                break;
                case '4': 
                    token=reconoceNum();
                break;
                case '5': 
                    token=reconoceNum();
                break;
                case '6': 
                    token=reconoceNum();
                break;
                case '7': 
                    token=reconoceNum();
                break;
                case '8': 
                    token=reconoceNum();
                break;
                case '9': 
                    token=reconoceNum();
                break;
                default:
                    if(letra1 != 13 && letra1 != 239 && letra1 != 187 && letra1 != 191 && letra1!=10)
                    //Si no se lee un 'enter', cuyo ASCII es 13, se invoca al método que reconoce palabras, ya que 
                    //no se ha cambiado de línea
                    {
                        //System.out.println("entro a reconocer con "+letra);
                        token=reconocerPalabras();
                    }
                    else
                    {
                        //Mientras se lea 'enter', se incrementa el índice i, 
                        //se lee el siguientecaracter,
                        //se incrementa la linea, indicando que se va a leer lo que haya en la siguiente línea y
                        //se inicializa la posición en 0 porque no se ha leído ningun caracter
                        if(letra1 == 13 || letra1==10)
                        {
                            System.out.println(letra1);
                            ind++;
                            //letra1 = fr.read();
                            if(letra1==10)
                            {
                              
                                linea++;
                                                                
                            }
                            posicion = 0;
                            //if(ind < fr.length())
                                token = pedirToken();
                            ind--;
                           // fr.seek(ind);
                           
                            
                            //ind--;
                        }
                        if(letra1 == 239 || letra1 == 187 || letra1 == 191)
                        {
                            ind++;
                            token = pedirToken();
                            ind--;
                        // fr.seek(ind);
                        }
                         
                            
                    }
            }
        ind++;
        posicion++;
            
            if(ind < fr.length())
              fr.seek(ind); 
            
        
//Posiciona el puntero del archivo en el siguiente caracter a leer
        //letra1 = fr.read(); //Se lee y obtiene el ASCII del caracter
        //letra = (char) letra1; //Se castea el ASCII obtenido a char para poder hacer las comparaciones correspondientes
        }
        else
        {
            this.cerrarArchivo();
        }
        //System.out.println("token "+token);
        return token;         
    }
    
    public void cargarPalabras() 
    {
        palabrasReservadas.put("program","token_program#");
        palabrasReservadas.put("var","token_var#" );
        palabrasReservadas.put("function","token_function#" );
        palabrasReservadas.put("procedure","token_procedure#" );
        palabrasReservadas.put("begin","token_begin#" );
        palabrasReservadas.put("end","token_end#");
        palabrasReservadas.put("then","token_then#" );
        palabrasReservadas.put("else","token_else#" );
        palabrasReservadas.put("while","token_while#" );
        palabrasReservadas.put("do","token_do#" );
        palabrasReservadas.put("read","token_read#");
        palabrasReservadas.put("write","token_write#" );
        palabrasReservadas.put("boolean","token_boolean#");
        palabrasReservadas.put("integer","token_integer#");
        palabrasReservadas.put("true","token_true#");
        palabrasReservadas.put("false","token_false#");
        palabrasReservadas.put("and","token_and#" );
        palabrasReservadas.put("or","token_or#");
        palabrasReservadas.put("not","token_not#");
        
        
    }
    private String reconocerPalabras() throws IOException
    {
        //Este método retorna el índice posicionado en el último caracter que leyó
      //  System.out.println("indice "+ind);
        fr.seek(ind);
        
        int letra1 = fr.read();
        char letra = (char)letra1;
       // System.out.println("letras "+letra1+" "+letra);
        String palabra ="";
        int ant = -1;
        boolean seguir = true;
        String token="";
        //Verifica que el caracter leído no sea un operador operacional, un símbolo de separación o terminación, o un enter.
        while(seguir && letra != ' ' &&  letra != '>' &&  letra != '<' && letra != '+' && letra != '-'
                && letra != '*' && letra != '/'
                && letra != '=' && letra != ':' && letra != '(' && letra!= '{' &&

                            letra != ')' && letra != ';' && letra != ',' && letra != '.' && letra1 != -1 && letra1 != 13 && letra1 != 10 ) 
        {
            //System.out.println("letra " + letra);
            if(perteneceAlfabeto(letra))
            {
               // System.out.println("letra en el alfabeto "+letra+ "letra1 "+letra1);
                
                palabra += letra;
            }
            else
            {
                
                if(letra1 == 195)
                {
                    System.out.println("error lexico : linea "+linea+" ñ ");
                    token="error#"+linea;
                   // System.out.println("error ñ"+token);
                    ind++;
                    this.cerrarArchivo();
                }
                else
                {
                    System.out.println("error lexico : linea "+linea+" letra 56454 "+letra1);
                    token="error#"+linea;
                    this.cerrarArchivo();
                }    
                seguir = false; 
                error = true;
                
            }
            ind++;
            ant = letra1;
            letra1 = fr.read();
            letra = (char)letra1;
            
        }
       
        if(!error && palabrasReservadas.containsKey(palabra.toLowerCase()))
        {
            token = (String)palabrasReservadas.get(palabra.toLowerCase())+linea+"#"+palabra;
        }
        else
        {
            if(!palabra.equalsIgnoreCase("") && !error)
            {
                token = "id#"+linea+"#"+palabra;
             
            }
        }
        ind--;
       
        return token;
    } 
    
    private String reconoceNum() throws IOException 
    {
        //El método retorna el índice del último dígito leídos
        fr.seek(ind);
        int aux = fr.read();
        char dig = (char) aux;
        String num = "";
        String token;
        while((dig=='0'||dig=='1'||dig=='2'||dig=='3'|| dig=='4'||dig=='5'||dig=='6'||dig=='7'||dig=='8'||dig=='9')&&aux !=-1 && aux != 13)
        {
            num += dig;
            aux= fr.read();
            dig=(char)aux;
            ind++;
        }
        token = "token_num#"+linea+"#"+num;
        ind--;
        return token;
    }
    
    public static boolean perteneceAlfabeto(char caracter)
    {
        boolean pertenece = false;
        int l = (int)caracter;
        if((    (l >= 65 && l <= 90) ||(l >= 97 && l <= 122)
                || Character.isDigit(caracter) || caracter == ';' || caracter == ','
                || caracter == '.' || caracter == '_' || caracter == '{' || caracter == '}' || caracter == '[' || caracter == ']'
                || caracter == '*' || caracter == '/' || caracter == '+' || caracter == '-' || caracter == ':'
                || caracter == '<' || caracter == '>' || caracter == '=' || caracter == '(' || caracter == ')') && (int)caracter != 195 &&
                (int)caracter != 177)
        {
            pertenece = true; 
        }
       
        return pertenece;
    }
    
    
}
