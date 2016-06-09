/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import java.io.IOException;


/**
 *
 * @author Diogo Duarte
 */
public class ClientMain {
    
    public static void main(String[] args) throws IOException {
        try {
            Client u = new Client("localhost");
            Interface ui = new Interface(u);
            ui.start();
            u.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}

    /*public static void main(String[] args) throws IOException {
        int portTCP;
        int portUDP;
        String ip;
        if (args.length < 2) {
            portTCP = 2000;//2000 por omissao
            portUDP=2001;
            ip = "localhost";
            System.out.println("Atribuída porta 2000 no localhost.");
        } else {
            ip = args[2];
            try {
                portTCP = Integer.parseInt(args[0]);
                portUDP = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Erro a ler a porta. Atribuída porta 2000.");
                portTCP = 2000;
                portUDP=2001;
            }
        }
        try {
            Client u = new Client(portTCP,portUDP,ip);
            Interface ui = new Interface(u);
            ui.start();
            u.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        /*
        System.out.print("#####");
        String ip = "192.168.1.65";
        int aux = random.nextInt((100000 - 49152)+1)+49152;
        String port = "aux"+"";
        Socket clientSocket = new Socket(ip, 2000);

        byte[] pdu = new PDU().registar(port, ip);

        OutputStream out = clientSocket.getOutputStream();
        out.write(pdu);*/
