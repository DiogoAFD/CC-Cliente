package Cliente;

import java.io.IOException;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Diogo Duarte
 *
 */
public class Interface {

    public Menu menuLogReg, menuMain, menuAnunDisp;
    private String pass, nome;
    private int id;
    private Client c;

    public Interface(Client c) {
        this.c = c;
    }

    public void start() throws IOException {
        carregarMenus();

        do {
            menuLogReg.executa();
            switch (menuLogReg.getOpcao()) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
            }
        } while (menuLogReg.getOpcao() != 0);
    }
    
    
    protected void menuPrincipal() throws IOException {
        do {
            menuMain.executa();
            switch (menuMain.getOpcao()) {
                case 1:
                    consultReq();
                    break;
                case 2:
                    enviarFich();
                    break;
                case 3: 
                    consultResponse();
                    break;
            }
        }while (menuMain.getOpcao() != 0);
    }
    
    
    private String[] mySplit(String mensagem) {
        String[] str;
        str = mensagem.split(",");
        return str;
    }
    
    
    
    protected void login() throws IOException {
        
        String resposta="";
        System.out.print("Nome: ");
        nome = Input.lerString();
        System.out.print("Password: ");
        pass = Input.lerString();
        try {
            resposta=c.login(nome,pass);
        } catch (myException s) {
            System.err.println(s.getMessage());
        }
        
        String[] msg_split = mySplit(resposta);
        if(msg_split[1].equals("OK")){
            id=Integer.parseInt(msg_split[2]);
            System.out.println("\nLogin realizado com sucesso");
            this.menuPrincipal();
        }
        else{
            System.out.println("\nLogin não efectuado");
        }
  
    }
    
    protected void register() throws IOException {

        boolean register = false;

        try {
            System.out.print("Nome: ");
            nome = Input.lerString();
            System.out.print("Password: ");
            pass = Input.lerString();
            register = c.register(nome, pass);
        } catch (myException s) {
            System.err.println(s.getMessage());
        }

        if (register) {
            System.out.println("\nRegisto efectuado com sucesso");
            start();
        }
    }
    
    protected void consultResponse() throws IOException{
        c.responderPedido(c.getPdu(), c.getPortaUDP(), nome, pass);
    
    }
    
    protected void consultReq()throws IOException{
            System.out.print("Nome da música: ");
            String nome_musica = Input.lerString();
            System.out.print("Banda/Artista: ");
            String banda_artista = Input.lerString();
            System.out.print("Extensão: ");
            String extensao = Input.lerString();
            ArrayList<Integer> resposta=new ArrayList<>();
            
        try {
            resposta = c.pedirFile(nome_musica, banda_artista,extensao,id);
        } catch (myException s) {
           System.err.println(s.getMessage());
        }
    
     }
    
    protected void enviarFich() throws IOException{
        
     //  c.responderPedido(pdu, id, pass, pass);
    }

    
    
    
    protected void carregarMenus() {

        String[] logReg = {"LOGIN",
            "REGISTER"};

        String[] main = {"CONSULT_REQUEST", "ENVIAR FICHEIRO" };

        menuLogReg = new Menu(logReg);
        menuMain = new Menu(main);
    }


}
