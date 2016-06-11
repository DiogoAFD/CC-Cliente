    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
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
            ipCliente = clientSck.getLocalAddress().getHostAddress();
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
    
    public String login(String name, String pass) throws myException, IOException {
        byte[] pduAux = null;
        String sResposta = "";
        pduAux=pdu2.login(name,pass);
        out.write(pduAux);
        try {
            sResposta = in.readLine();
        } catch (IOException ex) {
            throw new myException("Não foi possível obter resposta do servidor");
        } finally {
            return sResposta;
        }
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
       
       System.out.println("Aguarde por favor! Estamos a procura... :)");
       
       // vai receber ip a ip e adicionar ao arraylist
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
       
      
       
       return aux;
    }
    /*
    public int melhorConexao(ArrayList<Integer> portas) throws SocketException, IOException{
    
           int melhorPorta=0;
           
           for(Integer i:portas){
           //Crio o socket para testar conexao
           DatagramSocket aux = new DatagramSocket(i);
           
           while(true){

                byte[] buffer = new byte[256];
                
                System.out.println("À espera...");
                
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                aux.receive(packet);
                
                byte[] receive = packet.getData();
           
           }
    
    
    
    }
    }
*/
    
    public void enviarFicheiro(int portdest, String filename) throws IOException{
        
        DatagramSocket udpAux= new DatagramSocket(portdest);
        byte[] pduAux=new PDU().trataFicheiro(filename);
        
        DatagramPacket pacote = new DatagramPacket(pduAux, 0, portdest);
        
        udpAux.send(pacote);
        
        
       
    }
    
    public void receberFicheiro(int portaEnvio, String filename, int sizeFile) throws SocketException, FileNotFoundException, IOException{
        
        try{
        byte[] data =new byte[sizeFile+7];
        DatagramSocket udpAux= new DatagramSocket(portaEnvio); // conecta se com a pessoa que lhe vai mandar o ficheiro
        FileOutputStream  FOS = new FileOutputStream(filename); // cria o ficheiro com o nome passado como argumento
        
        DatagramPacket pacote= new DatagramPacket(data, sizeFile+7);
        
       udpAux.receive(pacote); // recebe o pdu do cliente que recebe
       
       byte[] ficheiro = new byte[sizeFile];
       // retiramos o cabeçalho do pdu e ficamos so com a parte referente ao ficheiro
       for(int i =0;i<sizeFile;i++){
       
           ficheiro[i]=data[i+7];
       
       }
       // escrevemos no ficheiro
       FOS.write(ficheiro);
       // fechamos o ficheiro
       FOS.close();
            
        
        
        
        
        }catch(java.net.SocketException a){
            System.out.println(a.toString());
        }
    
    
    }

    public int getPortaUDP() {
        return portaUDP;
    }

    public byte[] getPdu() {
        return pdu;
    }

    public PDU getPdu2() {
        return pdu2;
    }

    public DatagramSocket getUdp() {
        return udp;
    }
    
   
}