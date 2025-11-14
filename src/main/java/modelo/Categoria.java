package modelo;

import dao.CategoriaDao;
import java.util.ArrayList;


/**
 * Representa uma categoria
 * 
 * <p>
 * contém as informações: Nome, embalagem e tamanho
 * Encapsula operações de CRUD por um {@link CategoriaDao}
 * </p>
 */
public class Categoria {

    private int id;
    private String nome;
    private String embalagem;
    private String tamanho;
    private CategoriaDao dao;

    /** Construtor padrão */
    public Categoria() {
        this(0, "", "", "");
    }

    /**
     * Construtor completo
     *
     * @param id ID da categoria
     * @param nome Nome da categoria
     * @param embalagem Tipo de embalagem da categoria
     * @param tamanho Tamanho da categoria
     */
    public Categoria(int id, String nome, String embalagem, String tamanho) {
        this.id = id;
        this.nome = nome;
        this.embalagem = embalagem;
        this.tamanho = tamanho;
        this.dao = new CategoriaDao();
    }

    /**
     * Construtor com só ID e nome
     *
     * @param idCategoria ID da categoria
     * @param nomeCategoria Nome da categoria
     */
    public Categoria(int idCategoria, String nomeCategoria){
        this.id = idCategoria;
        this.nome = nomeCategoria;
    }
    
    /** @return Retorna o ID da categoria */
    public int getId() {
        return id;
    }

    /** @param id Seta um novo ID para a categoria */
    public void setId(int id) {
        this.id = id;
    }

    /** @return Retorna o nome da categoria */
    public String getNome() {
        return nome;
    }

    /** @param nome Seta um novo nome para a categoria */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /** @return Retorna o tipo de embalagem da categoria */
    public String getEmbalagem() {
        return embalagem;
    }

    /** @param embalagem Seta um novo tipo de embalagem para a categoria */
    public void setEmbalagem(String embalagem) {
        this.embalagem = embalagem;
    }

    /** @return Retorna o tamanho da categoria */
    public String getTamanho() {
        return tamanho;
    }

    /** @param tamanho Seta um novo tamanho para a categoria */
    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    /** @return Retorna o Dao utilizado pela classe */
    public CategoriaDao getDao() {
        return dao;
    }

    /**
     * Seta um novo Dao para operar com o BD
     * @param dao Instância de {@link CategoriaDao}
     */
    public void setDao(CategoriaDao dao) {
        this.dao = dao;
    }


    /**
     * Retorna a lista de categorias cadastradas
     *
     * @return lista de objetos {@link Categoria}
     */
    public ArrayList<Categoria> getMinhaLista() {
        return dao.getMinhaLista();
    }

    /**
     * Insere uma nova categoria no BD
     *
     * @param nome Nome da categoria
     * @param embalagem Tipo de embalagem
     * @param tamanho Tamanho da categoria
     * @return Retorna true caso a operação seja bem-sucedida
     */
    public boolean insertCategoriaBD(String nome, String embalagem, String tamanho) {
        int id = this.maiorID() + 1;
        Categoria objeto = new Categoria(id, nome, embalagem, tamanho);
        dao.insertCategoriaBD(objeto);
        return true;
    }

    /**
     * Deleta uma categoria do BD
     *
     * @param id ID da categoria a ser excluída
     * @return Retorna true caso a categoria seja excluída
     */
    public boolean deleteCategoriaBD(int id) {
        dao.deleteCategoriaBD(id);
        return true;
    }

/**
 * Atualiza uma categoria cadastrada
 *
 * @param id ID da categoria
 * @param nome Novo nome para a categoria
 * @param embalagem Nova embalagem para a categoria
 * @param tamanho Novo tamanho para a categoria
 * @return Retorna true caso a atualização seja bem-sucedida
 */    
public boolean updateCategoriaBD(int id, String nome, String embalagem, String tamanho) {
        Categoria objeto = new Categoria(id, nome, embalagem, tamanho);
        return dao.updateCategoriaBD(objeto);
    }
    
    /**
     * Carrega uma categoria do BD
     *
     * @param id ID da categoria
     * @return Retorna o objeto {@link Categoria} correspondente
     */
    public Categoria carregaCategoria(int id) {
        return dao.carregaCategoria(id);
    }


    /**
     * Obtém o maior ID no BD
     *
     * @return maior ID encontrado
     */
    public int maiorID() {
        return dao.maiorID();
    }
}
