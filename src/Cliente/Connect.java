/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Diogo Duarte
 */
public class Connect {

    private Socket cs;
    private InputStream in;
    private PrintWriter out;
    byte[] pdu;

    public Connect(Socket socket) throws IOException {
        this.cs = socket;
        this.in = cs.getInputStream();
        this.out = new PrintWriter(cs.getOutputStream(),true);
        this.pdu = new byte[100];
    }
    
    public byte[] readPDU() throws IOException{
        in.read(pdu);
        return pdu;
    }

    public InputStream getIn() {
        return in;
    }

    public PrintWriter getOut() {
        return out;
    }
    
    
    
    public void sendMessage(String msg){
        out.println(msg);
    }
}
