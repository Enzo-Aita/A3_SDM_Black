package server;

import controller.EstoqueController;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import modelo.Mensagem;

/**
 * Servidor principal do sistema de estoque Responsável por aceitar conexões de
 * clientes e processar operações
 *
 */
public class EstoqueServer {

    private ServerSocket serverSocket;
    private EstoqueController estoqueController;
    private boolean running;

    /**
     * Construtor do servidor de estoque
     */
    public EstoqueServer() {
        this.estoqueController = new EstoqueController();
        this.running = false;
    }

    /**
     * Método principal para iniciar o servidor
     *
     * @param args Argumentos da linha de comando
     */
    public static void main(String[] args) {
        EstoqueServer server = new EstoqueServer();
        server.start(12345);
    }

    /**
     * Inicia o servidor na porta especificada
     *
     * @param port Porta onde o servidor irá escutar
     */
    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            running = true;
            System.out.println("INICIANDO SERVIDOR DE ESTOQUE");
            System.out.println("Servidor de estoque iniciado na porta " + port);
            System.out.println("Banco: db_produtos");
            System.out.println("Aguardando conexões de clientes...");

            while (running) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress().getHostAddress());

                new ClientHandler(clientSocket, estoqueController).start();
            }
        } catch (IOException e) {
            System.err.println("Erro no servidor: " + e.getMessage());
        }
    }

    /**
     * Para o servidor
     */
    public void stop() {
        running = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Erro ao parar servidor: " + e.getMessage());
        }
    }

    /**
     * Handler para cada cliente conectado
     */
    private static class ClientHandler extends Thread {

        private Socket clientSocket;
        private EstoqueController controller;
        private ObjectInputStream in;
        private ObjectOutputStream out;

        /**
         * Construtor do handler do cliente
         *
         * @param socket Socket do cliente
         * @param controller Controlador de estoque
         */
        public ClientHandler(Socket socket, EstoqueController controller) {
            this.clientSocket = socket;
            this.controller = controller;
        }

        /**
         * Processa mensagens do cliente
         */
        public void run() {
            try {
                out = new ObjectOutputStream(clientSocket.getOutputStream());
                in = new ObjectInputStream(clientSocket.getInputStream());

                while (true) {
                    Mensagem mensagem = (Mensagem) in.readObject();
                    Mensagem resposta = controller.processarOperacao(mensagem);
                    out.writeObject(resposta);
                    out.flush();
                }
            } catch (EOFException e) {
                // Cliente desconectado normalmente
            } catch (Exception e) {
                System.err.println("Erro no handler: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.err.println("Erro ao fechar socket: " + e.getMessage());
                }
            }
        }
    }
}
