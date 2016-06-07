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
import java.io.PrintWriter;
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
    private PDU pdu2;

    
    public Client(int porta, String ip) throws IOException {
        try {
            clientSck = new Socket(ip, porta);
            udp= new DatagramSocket(porta);
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
    
    public boolean register(String name, String pass, String ip) throws myException, IOException {
        byte[] pduAux = null;
        String sResposta = "";
        System.out.println("cheguei12");
        pdu2.registar(name,pass,ip,pduAux);
        System.out.println("cheguei13");
        out.write(pduAux);
        try {
            sResposta = in.readLine();
        } catch (IOException ex) {
            throw new myException("Não foi possível obter resposta do servidor");
        } finally {
            return response(sResposta);
        }
    }
    
    /** esta função vai retornar uma lista com os ips dos users que contem esse ficheiro para depois testar 
    a conexão com todos os ips e ver qual é mais rapido
    */
    
    //CONSULTREQUEST DO CLIENTE PARA O SERVIDOR
    public ArrayList<String> pedirFile(String nome, String banda, String extensao) throws myException, IOException{
        
        ArrayList<String> aux= new ArrayList<>(); 
        
       pdu=new PDU().pedirFicheiro(nome, banda, extensao);
       OutputStream out = clientSck.getOutputStream();
       out.write(pdu);
       
       // vai receber ip a ip e adicionar ao arraylist
    
       return aux;
    }
    

    
 
    /*
    public void responderPedido(String resposta,String ip) throws IOException{
        pdu=new PDU().responderPedido(resposta); // no responderpedido temos de passar mais argumentos
        OutputStream out = clientSck.getOutputStream();
        if(resposta.equals("S")){
            out.write(pdu);
            byte[] aux = ip.getBytes(); // envia o ip que sera adicionado ao arraylist 
            out.write(aux);
        }
        if(resposta.equals("N")){
            out.write(pdu);
        }
    
    }*/
    
    
    
    public void enviarFicheiro(int portdest) throws IOException{
        
        
        byte [] ficheiro= new byte[1000]; // vai ser substituido por uma funçao que faz a conversao
        DatagramPacket enviar= new DatagramPacket(ficheiro,ficheiro.length);
        udp.send(enviar);
    }
}