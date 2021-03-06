/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;
import java.nio.ByteBuffer;

/**
 *
 * @author Miguel
 */

public class PDU {

    public PDU(){
        
    }

       public byte[] registar(String name, String pass, String ip,int portaUDP) {
        ByteBuffer bb;

        String aux = name + ',' + pass + ',' + ip + ',' + portaUDP;
        byte[] k = aux.getBytes();
        int tam = 9 + k.length;
        byte[] ret = new byte[tam];
        byte[] aux2 = new byte[7];

        bb = ByteBuffer.wrap(ret);
        

        
        aux2[0] = 1;
        aux2[1] = 0;
        aux2[2] = 1;
        aux2[3] = 0;
        aux2[4] = 0;
        aux2[5] = 0;
        aux2[6] = 0;
            
        
        bb.put(aux2);
        

        bb.put(k);
        

        return ret;
    }
    
    
    public byte[] pedirFicheiro(String nomeMusica, String banda, String extensao,int ident){
        ByteBuffer bb;
        String identificador= Integer.toString(ident);
        String aux = nomeMusica + ',' + banda + ',' + extensao +','+identificador;
        byte[] k = aux.getBytes();
        int tam = 9 + k.length;
        byte[] ret = new byte[tam];
        byte[] aux2 = new byte[7];

        bb = ByteBuffer.wrap(ret);
        
        aux2[0] = 1; // bit por defeito
        aux2[1] = 0;
        aux2[2] = 2; // Consult_Request
        aux2[3] = 0;
        aux2[4] = 0;
        aux2[5] = 0;
        aux2[6] = 0;

        bb.put(aux2);
        bb.put(k);
        return ret;
        
    }
    
    public byte[] login(String name, String pass) {
        ByteBuffer bb;

        String aux = name + ',' + pass;
        byte[] k = aux.getBytes();
        int tam = 9 + k.length;
        byte[] ret = new byte[tam];
        byte[] aux2 = new byte[7];

        bb = ByteBuffer.wrap(ret);

        
        aux2[0] = 1;
        aux2[1] = 0;
        aux2[2] = 0; //LOGIN
        aux2[3] = 0;
        aux2[4] = 0;
        aux2[5] = 0;
        aux2[6] = 0;
            
        
        bb.put(aux2);
        

        bb.put(k);
        

        return ret;
    }
    
    // 0 ou 1
    public byte[] responderPedido(int resposta, String id, String ip, int portaUDP){
      ByteBuffer bb;
      
      if(resposta==1){
        
        String resp= Integer.toString(resposta);
        String porta= Integer.toString(portaUDP);
        String result = resp+',' +id+',' +ip+','+porta;
        byte[] k = result.getBytes();
        int tam = 9 + k.length;
        byte[] ret = new byte[tam];
        byte[] aux2 = new byte[7];

        bb = ByteBuffer.wrap(ret);
        
        aux2[0] = 1; // bit por defeito
        aux2[1] = 0;
        aux2[2] = 3; // Consult_Responde
        aux2[3] = 0;
        aux2[4] = 0;
        aux2[5] = 0;
        aux2[6] = 0;

        bb.put(aux2);
        bb.put(k);
        return ret;
      }
      else{
          
           String resp= Integer.toString(resposta);
        
        
        byte[] k = resp.getBytes();
        int tam = 9 + k.length;
        byte[] ret = new byte[tam];
        byte[] aux2 = new byte[7];

        bb = ByteBuffer.wrap(ret);
        
        aux2[0] = 1; // bit por defeito
        aux2[1] = 0;
        aux2[2] = 3; // Consult_Responde
        aux2[3] = 0;
        aux2[4] = 0;
        aux2[5] = 0;
        aux2[6] = 0;

        bb.put(aux2);
        bb.put(k);
        return ret;
      
      }
      
    }
    
    public byte[] testeConexao(long tempo){
        
        ByteBuffer bb;
        String info = String.valueOf(tempo) + '\0';

        byte[] k = info.getBytes();
        int tam = 9 + k.length;
        byte[] ret = new byte[tam];
        byte[] aux2 = new byte[7];

        bb = ByteBuffer.wrap(ret);

        aux2[0] = 1;
        aux2[1] = 0;
        aux2[2] = 5;
        aux2[3] = 0;
        aux2[4] = 0;
        aux2[5] = 0;
        aux2[6] = 0;

        bb.put(aux2);
        bb.put(k);
        return ret;
    }
    
    //porta udp vai ser envia para o cliente que vai enviar para este poder enviar para a respetiva porta
    public byte[] pedirEnvio(String nomeMusica, String banda, String extensao,int portaUDP){
        
            ByteBuffer bb;
        String info = nomeMusica+","+banda+","+extensao+","+String.valueOf(portaUDP);

        byte[] k = info.getBytes();
        int tam = 9 + k.length;
        byte[] ret = new byte[tam];
        byte[] aux2 = new byte[7];

        bb = ByteBuffer.wrap(ret);

        aux2[0] = 1;
        aux2[1] = 0;
        aux2[2] = 6;
        aux2[3] = 0;
        aux2[4] = 0;
        aux2[5] = 0;
        aux2[6] = 0;

        bb.put(aux2);
        bb.put(k);
        return ret;
    
    
    
    }
    
    // recebe o path para o ficheiro
    public byte[] trataFicheiro(String ficheiro) throws IOException{
        
        ByteBuffer bb;
        File initialFile = new File(ficheiro);
        FileInputStream targetStream = new FileInputStream(initialFile);
        int filesize=targetStream.available();
        
         byte [] data= new byte[filesize+7];
         
         data[0]=1;
         data[1]=0;
         data[2]=7;
         //opçoes UDP
         data[3] = 0;
         data[4] = 0;
         data[5] = 0;
         data[6] = 0;
         
         for(int i=7;i<filesize+7;i++){
         
         data[i]=(byte)targetStream.read();
         }
         
         bb=ByteBuffer.wrap(data);
        
        return data;
    
    }
    
    
    
    public static int getPort(){

        Random random = new Random();
        int number = random.nextInt((65534 - 49152) + 1) + 49152;
        return number;
    }
}
