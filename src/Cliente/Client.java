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
import java.net.Socket;

/**
 *
 * @author Diogo Duarte
 */

public class Client {

    private Socket clientSck;
    private BufferedReader in;
    private PrintWriter out;
    private byte[] pdu;
    
    public Client(int porta, String ip) throws IOException {
        try {
            clientSck = new Socket(ip, porta);
        } catch (java.net.ConnectException a) {
            throw new IOException("Servidor não disponível");
        }
        out = new PrintWriter(clientSck.getOutputStream(), true);
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
        char codigo = str[2].charAt(0);
        boolean resposta = false;
        switch (codigo) {
            case '1':
                resposta = responseRegister(str[3]);
                break;

            default:
                throw new myException("Não foi possível efectuar a operação. Tente Novamente");
        }
        return resposta;
    }

    private boolean responseRegister(String mensagem) throws myException {
        boolean resposta = false;
        switch (mensagem) {
            case "ok":
                resposta = true;
                break;
            case "user ja existe":
                throw new myException("Não foi possivel efectuar o registo. Username já existente");
            default:
                throw new myException("Não foi possível efectuar a operação. Tente Novamente");
        }
        return resposta;
    }
    
    public boolean register(String name, String pass, String ip) throws myException, IOException {
        String sResposta = "";
        pdu = new PDU().registar(name,pass,ip);
        OutputStream out = clientSck.getOutputStream();
        out.write(pdu);
        /*try {
            sResposta = in.readLine();
        } catch (IOException ex) {
            throw new myException("Não foi possível obter resposta do servidor");
        } finally {
            return response(sResposta);
        }*/
        return true;
    }
}