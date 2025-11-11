package modelo;

import dao.CategoriaDao;
import java.util.ArrayList;

public class Categoria {

    private int id;
    private String nome;
    private String embalagem;
    private String tamanho;
    private CategoriaDao dao;

    public Categoria() {
        this(0, "", "", "");
    }

    public Categoria(int id, String nome, String embalagem, String tamanho) {
        this.id = id;
        this.nome = nome;
        this.embalagem = embalagem;
        this.tamanho = tamanho;
        this.dao = new CategoriaDao();
    }

    public Categoria(int idCategoria, String nomeCategoria){
        this.id = idCategoria;
        this.nome = nomeCategoria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmbalagem() {
        return embalagem;
    }

    public void setEmbalagem(String embalagem) {
        this.embalagem = embalagem;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public CategoriaDao getDao() {
        return dao;
    }

    public void setDao(CategoriaDao dao) {
        this.dao = dao;
    }



    public ArrayList<Categoria> getMinhaLista() {
        return dao.getMinhaLista();
    }

    public boolean insertCategoriaBD(String nome, String embalagem, String tamanho) {
        int id = this.maiorID() + 1;
        Categoria objeto = new Categoria(id, nome, embalagem, tamanho);
        dao.insertCategoriaBD(objeto);
        return true;
    }

    public boolean deleteCategoriaBD(int id) {
        dao.deleteCategoriaBD(id);
        return true;
    }
    
public boolean updateCategoriaBD(int id, String nome, String embalagem, String tamanho) {
        Categoria objeto = new Categoria(id, nome, embalagem, tamanho);
        return dao.updateCategoriaBD(objeto);
    }

    public Categoria carregaCategoria(int id) {
        return dao.carregaCategoria(id);
    }

    public int maiorID() {
        return dao.maiorID();
    }
}
