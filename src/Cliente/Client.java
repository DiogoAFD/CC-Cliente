    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Diogo Duarte
 */

public class Client {

    private Socket clientSck;
    private BufferedReader in;
    //private PrintWriter out;
    private byte[] pdu;
    private DatagramSocket udp = null;
    private OutputStream out;
    private PDU pdu2=new PDU();
    public Input input;
    public String ipCliente;
    public int portaUDP = PDU.getPort();
    public Connect cs;
    
    
    public Client(String ip) throws IOException {
        try {
            clientSck = new Socket(ip, 2000);
            udp= new DatagramSocket(portaUDP);
            pdu2=new PDU();
            cs=new Connect(clientSck);
        } catch (java.net.ConnectException a) {
            throw new IOException("Servidor não disponível");
        }
        out = clientSck.getOutputStream();
        in = new BufferedReader(new InputStreamReader(clientSck.getInputStream()));
    }

    public void close() throws IOException {
        in.close();
        out.close();
        clientSck.close();
    }

    private String[] mySplit(String mensagem) {
        String[] str;
        str = mensagem.split(",");
        return str;
    }
    
    public boolean response(String mensagem) throws myException {
        String[] str = mySplit(mensagem);
        //char codigo = str[2].charAt(0);
        String codigo = str[0];
        System.out.println("codigo"+str[0]);
        boolean resposta = false;
        switch(codigo){
            case "1":
                resposta = responseRegister(str);
                break;
            default:
                throw new myException("Não foi possível efectuar a operação. Tente Novamente");
        }
        return resposta;
    }

    private boolean responseRegister(String[] mensagem) throws myException {
        boolean resposta = false;
        
        switch (mensagem[1]) {
            case "ok":
                resposta = true;
                break;
            case "ip ja existe":
                throw new myException("Não foi possivel efectuar o registo. IP já existente");
            default:
                throw new myException("Não foi possível efectuar a operação. Tente Novamente");
        }
        return resposta;
    }
    
    public boolean register(String name, String pass) throws myException, IOException {
        byte[] pduAux = null;
        String sResposta = "";
        pduAux=pdu2.registar(name,pass,ipCliente,portaUDP);
        out.write(pduAux);
        try {
            sResposta = in.readLine();
        } catch (IOException ex) {
            throw new myException("Não foi possível obter resposta do servidor");
        } finally {
            return response(sResposta);
        }
    }
    
    
    public void responderPedido(byte[] pdu,int portaUDP,String ip,String id) throws IOException{
        
        byte[] pduAux = null;
        int i;
        String nomeMusica = "", banda = "", extensao="";
        
          for (i = 7; (char) pdu[i] != ','; i++) {
            nomeMusica += (char) pdu[i];
        }
        // faz i++ ao inicio para avancar o ,
        for (i++; (char) pdu[i] != ','; i++) {
            banda += (char) pdu[i];
        }
        for (i++; (char) pdu[i] != '\0'; i++) {
            extensao += (char) pdu[i];
        }
        
        String pedido =new String(nomeMusica+","+banda+","+extensao);
        int resposta=0;
        
        System.out.println("Um cliente fez o seguinte pedido: "+pedido);
        System.out.println("Voce contem esse ficheiro? S ou N");
        String resp=input.lerString();
        if(resp.equals("S")||resp.equals("s")) {resposta=1; pduAux=pdu2.responderPedido(resposta, id, ip, portaUDP);}
        if(resp.equals("N")||resp.equals("n")) {resposta=0;  pduAux=pdu2.responderPedido(resposta, id, ip, portaUDP);}
        
        
        
    
    
    }
    
    /** esta função vai retornar uma lista com os ips dos users que contem esse ficheiro para depois testar 
    a conexão com todas as portas udps*/
    
    //CONSULTREQUEST DO CLIENTE PARA O SERVIDOR
    public ArrayList<Integer> pedirFile(String nome, String banda, String extensao,int ident) throws myException, IOException{
        int i;
        ArrayList<Integer> aux= new ArrayList<>(); 
        
        byte[] recebe = null;
        String resp="";
        
       pdu=new PDU().pedirFicheiro(nome, banda, extensao,ident);
       out.write(pdu);
       
       
       while((recebe=cs.readPDU())!= null){
           
            for (i = 7; (char) recebe[i] != ','; i++) {
            resp += (char) recebe[i];
            
        }
            if(Integer.getInteger(resp)==1){
                String ptU ="";
                for (i++; (char) recebe[i] != ','; i++);
                for (i++; (char) recebe[i] != ','; i++);
                
                for (i++; (char) recebe[i] != '\0'; i++) {
                    ptU+= (char) recebe[i];
                }
                
                int porta=Integer.getInteger(ptU);
                
                aux.add(porta);
            
            }
        
        
        }
       
      
       
       
       
       // vai receber ip a ip e adicionar ao arraylist
    
       return aux;
    }
    

    
    
 
 
    
    
    
    public void enviarFicheiro(int portdest) throws IOException{
        
        
        byte [] ficheiro= new byte[1000]; // vai ser substituido por uma funçao que faz a conversao
        DatagramPacket enviar= new DatagramPacket(ficheiro,ficheiro.length);
        udp.send(enviar);
    }
}