/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author Diogo Duarte
 */
public class ClientMain {

    public static void main(String[] args) throws IOException {
        int port;
        String ip;
        if (args.length < 2) {
            port = 2000;//2000 por omissao
            ip = "localhost";
            System.out.println("Atribuída porta 2000 no localhost.");
        } else {
            ip = args[1];
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Erro a ler a porta. Atribuída porta 2000.");
                port = 2000;
            }
        }
        try {
            Client u = new Client(port,ip);
            Interface ui = new Interface(u);
            ui.start();
            u.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        /*
        System.out.print("#####");
        String ip = "192.168.1.65";
        String port = "2000";
        Socket clientSocket = new Socket(ip, 2000);

        byte[] pdu = new PDU().registar(port, ip);

        OutputStream out = clientSocket.getOutputStream();
        out.write(pdu);*/

    }
}
