/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import java.nio.ByteBuffer;

/**
 *
 * @author Miguel
 */
public class PDU {

    public PDU() {
    }

    public byte[] registar(String porta, String ip) {

        ByteBuffer bb;
        String aux = porta + ',' + ip;
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
}
