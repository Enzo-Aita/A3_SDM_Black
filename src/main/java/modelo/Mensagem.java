package modelo;

import java.io.Serializable;

/**
 * Classe para troca de mensagens entre cliente e servidor
 *
 *
 *
 */
public class Mensagem implements Serializable {

    private static final long serialVersionUID = 1L;

    private String operacao;
    private Object dados;
    private String status;

    /**
     * Construtor completo da mensagem
     *
     * @param operacao Tipo de operação a ser realizada
     * @param dados Dados associados à operação
     * @param status Status da operação
     */
    public Mensagem(String operacao, Object dados, String status) {
        this.operacao = operacao;
        this.dados = dados;
        this.status = status;
    }

    /**
     * Construtor sem status (usa "PENDENTE" como padrão)
     *
     * @param operacao Tipo de operação a ser realizada
     * @param dados Dados associados à operação
     */
    public Mensagem(String operacao, Object dados) {
        this(operacao, dados, "PENDENTE");
    }

    /**
     * @return Operação da mensagem
     */
    public String getOperacao() {
        return operacao;
    }

    /**
     * @param operacao Nova operação da mensagem
     */
    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    /**
     * @return Dados da mensagem
     */
    public Object getDados() {
        return dados;
    }

    /**
     * @param dados Novos dados da mensagem
     */
    public void setDados(Object dados) {
        this.dados = dados;
    }

    /**
     * @return Status da mensagem
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status Novo status da mensagem
     */
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Mensagem{operacao='" + operacao + "', status='" + status + "', dados=" + dados + "}";
    }
}
