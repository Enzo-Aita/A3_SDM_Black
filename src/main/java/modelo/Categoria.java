package modelo;

import java.io.Serializable;

/**
 * Categoria de produto Contém os atributos para identificação: nome, embalagem
 * e tamanho
 */
public class Categoria implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String nome;
    private String embalagem;
    private String tamanho;

    /**
     * Construtor padrão
     */
    public Categoria() {
        this(0, "", "", "");
    }

    /**
     * Construtor completo
     *
     * @param id Código de identificação
     * @param nome Nome da categoria
     * @param embalagem Tipo de embalagem
     * @param tamanho Tamanho da embalagem
     */
    public Categoria(int id, String nome, String embalagem, String tamanho) {
        this.id = id;
        this.nome = nome;
        this.embalagem = embalagem;
        this.tamanho = tamanho;
    }

    /**
     * Construtor Básico para categoria
     *
     * @param idCategoria Código identificação da categoria
     * @param nomeCategoria Nome da categoria
     */
    public Categoria(int idCategoria, String nomeCategoria) {
        this.id = idCategoria;
        this.nome = nomeCategoria;
    }

    /**
     * @return Retorna o ID da categoria
     */
    public int getId() {
        return id;
    }

    /**
     * @param id Seta o ID da categoria
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return Retorna o Nome da categoria
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome Seta o nome da categoria
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return Retorna o tipo de embalagem
     */
    public String getEmbalagem() {
        return embalagem;
    }

    /**
     * @param embalagem Seta o tipo de embalagem
     */
    public void setEmbalagem(String embalagem) {
        this.embalagem = embalagem;
    }

    /**
     * @return Retorna o tamanho da embalagem
     */
    public String getTamanho() {
        return tamanho;
    }

    /**
     * @param tamanho Seta o tamanho da embalagem
     */
    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    @Override
    public String toString() {
        return "Categoria{"
                + "id=" + id
                + ", nome='" + nome + '\''
                + ", embalgem='" + embalagem + '\''
                + ",tamanho='" + tamanho + '\''
                + '}';
    }
}
