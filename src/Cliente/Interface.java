package Cliente;

import java.io.IOException;

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


    /*
    public String secToFormat(long segundos) {
        String startTime = "00:00:00";
        int h = (int) floor(segundos / 3600);
        int m = (int) floor(segundos / 60) - h * 60;
        int s = (int) (segundos - h * 3600 - m * 60);
        String newtime = h + ":" + m + ":" + s;

        return newtime;
    }

    protected void solViagem() throws myException {

        int x_0, y_0, x, y;
        String[] resposta = null;

        System.out.print("Insira a posição em que se encontra: \n");
        System.out.print("x = ");
        x_0 = Input.lerInt();
        System.out.print("y = ");
        y_0 = Input.lerInt();
        System.out.print("Insira a posição do destino: \n");
        System.out.print("x = ");
        x = Input.lerInt();
        System.out.print("y = ");
        y = Input.lerInt();
        int codViagem;
        String chegou;
        System.out.println("A aguardar um condutor disponivel...");
        resposta = c.solViagem(user, x_0, y_0, x, y);
        
        if (resposta != null) {
            codViagem = Integer.parseInt(resposta[2]);

            String newtime = secToFormat(Integer.parseInt(resposta[5]));
            String newtime2 = secToFormat(Integer.parseInt(resposta[6]));
            System.out.println("Código da Viagem: " + resposta[2] + "\nMatricula: " + resposta[3] + "\nModelo: " + resposta[4]
                    + "\nTempo estimado de espera(HH:MM:SS): " + newtime + "\nDuração estimada da viagem(HH:MM:SS): "
                    + newtime2 + "\nPreço Estimado: " + resposta[7] + "€");
            System.out.println("A aguardar resposta do condutor...");
            if (c.chegouPartidaPassageiro(codViagem)) {
                System.out.println("Chegou ao local de partida, para confirmar pressione enter");
                do {
                    chegou = Input.lerString();
                } while (!chegou.equals(""));
                c.chegouPartidaRespostaPassageiro(codViagem);
            }
            System.out.println("A aguardar resposta do condutor...");
            String[] chegouDestino = c.chegouDestinoPassageiro(codViagem);
            
            if (chegouDestino != null) {
                float preco = Float.parseFloat(chegouDestino[2]);
                System.out.println("Viagem concluída. Custo: " + preco);
                System.out.println("Para confirmar pressione enter");

                do {
                    chegou = Input.lerString();
                } while (!chegou.equals(""));
                c.chegouDestinoRespostaPassageiro(codViagem);
            }
        } else {
            System.err.println("Não foi possivel solicitar viagem");
        }
   
    }

    protected void anunDisp() throws myException {

        float preco;
        String chegou;
        int codigoViagem;

        String[] anunDispMostra = null;

        System.out.print("Insira a posição em que se encontra o condutor: \n");
        System.out.print("x = ");
        x = Input.lerInt();
        System.out.print("y = ");
        y = Input.lerInt();
        System.out.println("Insira a matricula do veiculo");
        mat = Input.lerString();
        System.out.println("Insira o modelo do veiculo");
        mod = Input.lerString();
        System.out.println("A aguardar um passageiro disponivel...");
        anunDispMostra = c.anunDisp1(user, mat, mod, x, y);
        
        if (anunDispMostra != null) {
            codigoViagem = Integer.parseInt(anunDispMostra[2]);
            System.out.println("Já foi atribuida uma viagem!\nCódigo de Viagem: " + anunDispMostra[2] + "\nNome do Passageiro: "
                    + anunDispMostra[3] + "\nCoordenadas do local de partida: (" + anunDispMostra[4] + "," + anunDispMostra[5]
                    + ")\nCoordenadas da do local de destino : (" + anunDispMostra[6] + "," + anunDispMostra[7] + ")");

            System.out.println("Quando o condutor tiver chegado ao local de partida pressione enter");
            do {
                chegou = Input.lerString();
            } while (!chegou.equals(""));
            c.chegouPartidaCondutor(codigoViagem);
            System.out.println("A aguardar resposta do passageiro...");
            if (c.chegouPartidaRespostaCondutor(codigoViagem)) {
                System.out.println("Quando o condutor tiver chegado ao local de destino pressione enter");
                do {
                    chegou = Input.lerString();
                } while (!chegou.equals(""));
                
                System.out.print("Insira o preco do transporte: \n");
                preco = Input.lerFloat();
                System.out.println("A aguardar resposta do passageiro...");
                c.chegouDestinoCondutor(codigoViagem, preco);
                if (!c.chegouDestinoRespostaCondutor(codigoViagem)) {
                    System.err.println("Ocorreu um erro!");
                    menuPrincipal();
                } else {
                    x = Integer.parseInt(anunDispMostra[6]);
                    y = Integer.parseInt(anunDispMostra[7]);
                }
            } else {
                System.err.println("Ocorreu um erro!");
                menuPrincipal();
            }
        } else {
            System.err.println("Ocorreu um erro!");
            menuPrincipal();
        }
        this.menuAnunDisp();
    }

    protected void anunDispMesmoCarro() throws myException {
     
        float preco;
        String chegou;
        int codigoViagem;

        String[] anunDispMostra = null;

        System.out.print("Insira a posição em que se encontra o condutor: \n");
        System.out.print("x = ");
        x = Input.lerInt();
        System.out.print("y = ");
        y = Input.lerInt();
        System.out.println("A aguardar um passageiro disponivel...");
        anunDispMostra = c.anunDisp1(user, mat, mod, x, y);
        
        if (anunDispMostra != null) {
            codigoViagem = Integer.parseInt(anunDispMostra[2]);
            System.out.println("Anuncio de disponibilidade realizado com sucesso\n");
            System.out.println("Já foi atribuida uma viagem!\nCódigo de Viagem: " + anunDispMostra[2] + "\nNome do Passageiro: "
                    + anunDispMostra[3] + "\nCoordenadas do local de partida: (" + anunDispMostra[4] + "," + anunDispMostra[5]
                    + ")\nCoordenadas da do local de destino : (" + anunDispMostra[6] + "," + anunDispMostra[7] + ")");

            System.out.println("Quando o condutor tiver chegado ao local de partida pressione enter\n");
            do {
                chegou = Input.lerString();
            } while (!chegou.equals(""));
            c.chegouPartidaCondutor(codigoViagem);
            System.out.println("A aguardar resposta do passageiro...");
            if (c.chegouPartidaRespostaCondutor(codigoViagem)) {
                System.out.println("Quando o condutor tiver chegado ao local de destino pressione enter\n");
                do {
                    chegou = Input.lerString();
                } while (!chegou.equals(""));

                System.out.print("Insira o preco do transporte: \n");
                preco = Input.lerFloat();
                System.out.println("A aguardar resposta do passageiro...");
                c.chegouDestinoCondutor(codigoViagem, preco);
                if (!c.chegouDestinoRespostaCondutor(codigoViagem)) {
                    System.err.println("Ocorreu um erro!");
                    menuPrincipal();
                }else{
                    x = Integer.parseInt(anunDispMostra[6]);
                    y = Integer.parseInt(anunDispMostra[7]);
                }
            } else {
                System.err.println("Ocorreu um erro!");
                menuPrincipal();
            }
        } else {
            System.err.println("Ocorreu um erro!");
            menuPrincipal();
        }
        this.menuAnunDisp();
    }

    protected void anunDispMesmoLocal() throws myException {
        float preco;
        String chegou;
        int codigoViagem;

        String[] anunDispMostra = null;
        System.out.println("A aguardar um passageiro disponivel...");
        anunDispMostra = c.anunDisp1(user, mat, mod, x, y);
        if (anunDispMostra != null) {
            codigoViagem = Integer.parseInt(anunDispMostra[2]);
            System.out.println("Anuncio de disponibilidade realizado com sucesso\n");
            System.out.println("Já foi atribuida uma viagem!\nCódigo de Viagem: " + anunDispMostra[2] + "\nNome do Passageiro: "
                    + anunDispMostra[3] + "\nCoordenadas do local de partida: (" + anunDispMostra[4] + "," + anunDispMostra[5]
                    + ")\nCoordenadas da do local de destino : (" + anunDispMostra[6] + "," + anunDispMostra[7] + ")");

            System.out.println("Quando o condutor tiver chegado ao local de partida pressione enter\n");
            do {
                chegou = Input.lerString();
            } while (!chegou.equals(""));
            c.chegouPartidaCondutor(codigoViagem);
            System.out.println("A aguardar resposta do passageiro...");
            if (c.chegouPartidaRespostaCondutor(codigoViagem)) {
                System.out.println("Quando o condutor tiver chegado ao local de destino pressione enter\n");
                do {
                    chegou = Input.lerString();
                } while (!chegou.equals(""));

                System.out.print("Insira o preco do transporte: \n");
                preco = Input.lerFloat();
                System.out.println("A aguardar resposta do passageiro...");
                c.chegouDestinoCondutor(codigoViagem, preco);
                if (!c.chegouDestinoRespostaCondutor(codigoViagem)) {
                    System.err.println("Ocorreu um erro!");
                    menuPrincipal();
                }else{
                    x = Integer.parseInt(anunDispMostra[6]);
                    y = Integer.parseInt(anunDispMostra[7]);
                }
            } else {
                System.err.println("Ocorreu um erro!");
                menuPrincipal();
            }
        } else {
            System.err.println("Ocorreu um erro!");
            menuPrincipal();
        }
        this.menuAnunDisp();
    }

    protected void menuAnunDisp() throws myException {
        do {
            menuAnunDisp.executa();
            switch (menuAnunDisp.getOpcao()) {
                case 1:
                    anunDispMesmoLocal();
                    break;
                case 2:
                    anunDispMesmoCarro();
                    break;
            }
        } while (menuAnunDisp.getOpcao() != 0);
    }
    */
   

}
