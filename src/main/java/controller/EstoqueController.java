package controller;

import dao.CategoriaDAO;
import dao.MovimentaDAO;
import dao.ProdutoDAO;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.Operacoes;
import java.util.stream.Collectors;
import modelo.Mensagem;
import modelo.Produto;

/**
 * Controlador principal do sistema de estoque Responsável por processar
 * operações e coordenar os DAOs
 *
 * @author SeuNome
 * @version 1.0
 */
public class EstoqueController {

    private ProdutoDAO produtoDAO;
    private CategoriaDAO categoriaDAO;
    private MovimentaDAO movimentacaoDAO;

    /**
     * Construtor do controlador de estoque
     */
    public EstoqueController() {
        this.produtoDAO = new ProdutoDAO();
        this.categoriaDAO = new CategoriaDAO();
        this.movimentacaoDAO = new MovimentaDAO();
        this.produtoDAO.corrigirEstoqueMinMax();
    }

    /**
     * Processa operações recebidas do cliente
     *
     * @param mensagem Mensagem com operação e dados
     * @return Resposta da operação
     */
    public Mensagem processarOperacao(Mensagem mensagem) {
        try {
            switch (mensagem.getOperacao()) {
                case Operacoes.CADASTRAR_PRODUTO:
                    return cadastrarProduto((Produto) mensagem.getDados());

                case Operacoes.LISTAR_PRODUTOS:
                    return listarProdutos();

                case Operacoes.ATUALIZAR_PRODUTO:
                    return atualizarProduto((Produto) mensagem.getDados());

                case Operacoes.EXCLUIR_PRODUTO:
                    return excluirProduto((Integer) mensagem.getDados());

                case Operacoes.CADASTRAR_CATEGORIA:
                    return cadastrarCategoria((modelo.Categoria) mensagem.getDados());

                case Operacoes.LISTAR_CATEGORIAS:
                    return listarCategorias();

                case Operacoes.ATUALIZAR_CATEGORIA:
                    return atualizarCategoria((modelo.Categoria) mensagem.getDados());

                case Operacoes.EXCLUIR_CATEGORIA:
                    return excluirCategoria((Integer) mensagem.getDados());

                case Operacoes.OBTER_HISTORICO_MOVIMENTACOES:
                    return obterHistoricoMovimentacoes((Integer) mensagem.getDados());

                case Operacoes.REALIZAR_MOVIMENTACAO:
                    Map<String, Object> dadosMov = (Map<String, Object>) mensagem.getDados();
                    Object idObj = dadosMov.get("idProduto");
                    int idProduto;

                    if (idObj instanceof Integer) {
                        idProduto = (Integer) idObj;
                    } else if (idObj instanceof Long) {
                        idProduto = ((Long) idObj).intValue();
                    } else {
                        idProduto = ((Number) idObj).intValue();
                    }

                    return realizarMovimentacao(
                            idProduto,
                            (Integer) dadosMov.get("quantidade"),
                            (String) dadosMov.get("tipo")
                    );

                case Operacoes.RELATORIO_PRECOS:
                    return gerarRelatorioPrecos();

                case Operacoes.RELATORIO_BALANCO:
                    return gerarBalancoFisicoFinanceiro();

                case Operacoes.RELATORIO_ESTOQUE_MINIMO:
                    return gerarRelatorioEstoqueMinimo();

                case Operacoes.RELATORIO_ESTOQUE_MAXIMO:
                    return gerarRelatorioEstoqueMaximo();

                case Operacoes.RELATORIO_PRODUTOS_CATEGORIA:
                    return gerarRelatorioProdutosPorCategoria();

                case Operacoes.REAJUSTAR_PRECOS:
                    return reajustarPrecos((Produto) mensagem.getDados());

                default:
                    return new Mensagem("ERRO", "Operação não suportada: " + mensagem.getOperacao());
            }
        } catch (Exception e) {
            return new Mensagem("ERRO", "Erro ao processar operação: " + e.getMessage());
        }
    }

    /**
     * Lista todos os produtos
     *
     * @return Mensagem com lista de produtos
     */
    private Mensagem listarProdutos() {
        try {
            ArrayList<Produto> produtos = (ArrayList<Produto>) produtoDAO.getMinhaLista();
            return new Mensagem("LISTAR_PRODUTOS", produtos, "SUCESSO");
        } catch (Exception e) {
            return new Mensagem("LISTAR_PRODUTOS", "Erro ao listar produtos: " + e.getMessage(), "ERRO");
        }
    }

    /**
     * Cadastra um novo produto
     *
     * @param produto Produto a ser cadastrado
     * @return Mensagem de sucesso ou erro
     */
    private Mensagem cadastrarProduto(Produto produto) {
        try {
         
            produto.setId(0); 

            boolean sucesso = produtoDAO.insertProdutoBD(produto);

            if (sucesso) {
                return new Mensagem("CADASTRAR_PRODUTO", "Produto cadastrado com ID: " + produto.getId(), "SUCESSO");
            } else {
                return new Mensagem("CADASTRAR_PRODUTO", "Erro ao salvar no banco de dados", "ERRO");
            }
        } catch (Exception e) {
            return new Mensagem("CADASTRAR_PRODUTO", "Erro: " + e.getMessage(), "ERRO");
        }
    }

    /**
     * Atualiza um produto existente
     *
     * @param produto Produto com dados atualizados
     * @return Mensagem de sucesso ou erro
     */
    private Mensagem atualizarProduto(Produto produto) {
        try {
            if (produtoDAO.updateProdutoBD(produto)) {
                return new Mensagem("ATUALIZAR_PRODUTO", "Produto atualizado com sucesso", "SUCESSO");
            } else {
                return new Mensagem("ATUALIZAR_PRODUTO", "Erro ao atualizar produto", "ERRO");
            }
        } catch (Exception e) {
            return new Mensagem("ATUALIZAR_PRODUTO", "Erro ao atualizar produto: " + e.getMessage(), "ERRO");
        }
    }

    /**
     * Exclui um produto
     *
     * @param id ID do produto a ser excluído
     * @return Mensagem de sucesso ou erro
     */
    private Mensagem excluirProduto(int id) {
        try {
            if (produtoDAO.deleteProdutoBD(id)) {
                return new Mensagem("SUCESSO", "Produto excluído com sucesso");
            } else {
                return new Mensagem("ERRO", "Erro ao excluir produto");
            }
        } catch (Exception e) {
            return new Mensagem("ERRO", "Erro ao excluir produto: " + e.getMessage());
        }
    }

    /**
     * Lista todas as categorias
     *
     * @return Mensagem com lista de categorias
     */
    private Mensagem listarCategorias() {
        try {
            ArrayList<modelo.Categoria> categorias = (ArrayList<modelo.Categoria>) categoriaDAO.getMinhaLista();
            return new Mensagem("LISTAR_CATEGORIAS", categorias, "SUCESSO");
        } catch (Exception e) {
            return new Mensagem("LISTAR_CATEGORIAS", "Erro ao listar categorias: " + e.getMessage(), "ERRO");
        }
    }

    /**
     * Cadastra uma nova categoria
     *
     * @param categoria Categoria a ser cadastrada
     * @return Mensagem de sucesso ou erro
     */
    private Mensagem cadastrarCategoria(modelo.Categoria categoria) {
        try {
            if (categoriaDAO.insertCategoriaBD(categoria)) {
                return new Mensagem("CADASTRAR_CATEGORIA", "Categoria cadastrada com sucesso", "SUCESSO");
            } else {
                return new Mensagem("CADASTRAR_CATEGORIA", "Erro ao cadastrar categoria", "ERRO");
            }
        } catch (Exception e) {
            return new Mensagem("CADASTRAR_CATEGORIA", "Erro ao cadastrar categoria: " + e.getMessage(), "ERRO");
        }
    }

    /**
     * Atualiza uma categoria existente
     *
     * @param categoria Categoria com dados atualizados
     * @return Mensagem de sucesso ou erro
     */
    private Mensagem atualizarCategoria(modelo.Categoria categoria) {
        try {
            if (categoriaDAO.updateCategoriaBD(categoria)) {
                return new Mensagem("SUCESSO", "Categoria atualizada com sucesso");
            } else {
                return new Mensagem("ERRO", "Erro ao atualizar categoria");
            }
        } catch (Exception e) {
            return new Mensagem("ERRO", "Erro ao atualizar categoria: " + e.getMessage());
        }
    }

    /**
     * Exclui uma categoria
     *
     * @param id ID da categoria a ser excluída
     * @return Mensagem de sucesso ou erro
     */
    private Mensagem excluirCategoria(int id) {
        try {
            if (categoriaDAO.deleteCategoriaBD(id)) {
                return new Mensagem("SUCESSO", "Categoria excluída com sucesso");
            } else {
                return new Mensagem("ERRO", "Erro ao excluir categoria");
            }
        } catch (Exception e) {
            return new Mensagem("ERRO", "Erro ao excluir categoria: " + e.getMessage());
        }
    }

    /**
     * Realiza movimentação de estoque
     *
     * @param idProduto ID do produto
     * @param quantidade Quantidade a movimentar
     * @param tipo Tipo de movimentação (ENTRADA/SAIDA)
     * @return Mensagem com resultado da operação
     */
    private Mensagem realizarMovimentacao(int idProduto, int quantidade, String tipo) {
        try {
            Produto produto = buscarProdutoPorId(idProduto);
            if (produto == null) {
                return new Mensagem("ERRO", "Produto não encontrado com ID: " + idProduto);
            }

            int estoqueMinimoOriginal = produto.getEstoqueminimo();
            int estoqueMaximoOriginal = produto.getEstoquemaximo();

            if (tipo.equals("SAIDA") && produto.getQuantidade() < quantidade) {
                return new Mensagem("ERRO", "Estoque insuficiente para " + produto.getProduto());
            }

            int novaQuantidade;
            if (tipo.equals("ENTRADA")) {
                novaQuantidade = produto.getQuantidade() + quantidade;
            } else {
                novaQuantidade = produto.getQuantidade() - quantidade;
            }

            produto.setQuantidade(novaQuantidade);
            produto.setEstoqueminimo(estoqueMinimoOriginal);
            produto.setEstoquemaximo(estoqueMaximoOriginal);

            boolean atualizado = produtoDAO.updateProdutoBD(produto);

            if (atualizado) {
                movimentacaoDAO.registrarMovimentacao(
                        produto.getId(), quantidade, tipo, "Movimentação sistema"
                );
            }

            Map<String, Object> resposta = new HashMap<>();
            resposta.put("produto", criarMapProduto(produto));

            String status = "SUCESSO";

            if (tipo.equals("ENTRADA") && novaQuantidade > estoqueMaximoOriginal) {
                status = "AVISO_ESTOQUE_MAXIMO";
                resposta.put("alerta", "Quantidade acima do máximo permitido para: " + produto.getProduto());
            } else if (tipo.equals("SAIDA") && novaQuantidade < estoqueMinimoOriginal) {
                status = "AVISO_ESTOQUE_MINIMO";
                resposta.put("alerta", "Quantidade abaixo do mínimo para: " + produto.getProduto());
            }

            return new Mensagem(status, resposta);

        } catch (Exception e) {
            return new Mensagem("ERRO", "Erro na movimentação: " + e.getMessage());
        }
    }

    /**
     * Obtém histórico de movimentações de um produto
     *
     * @param idProduto ID do produto
     * @return Mensagem com histórico de movimentações
     */
    private Mensagem obterHistoricoMovimentacoes(int idProduto) {
        try {
            List<Map<String, Object>> historico = movimentacaoDAO.getHistoricoPorProduto(idProduto);

            List<Map<String, Object>> historicoFormatado = new ArrayList<>();
            for (Map<String, Object> mov : historico) {
                Map<String, Object> movFormatada = new HashMap<>();

                if (mov.get("data_hora") instanceof java.time.LocalDateTime) {
                    java.time.LocalDateTime dataHora = (java.time.LocalDateTime) mov.get("data_hora");
                    String dataFormatada = dataHora.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                    movFormatada.put("data_hora", dataFormatada);
                } else {
                    movFormatada.put("data_hora", mov.get("data_hora").toString());
                }

                movFormatada.put("tipo", mov.get("tipo"));
                movFormatada.put("quantidade", mov.get("quantidade"));
                movFormatada.put("observacao", mov.get("observacao") != null ? mov.get("observacao") : "");
                historicoFormatado.add(movFormatada);
            }

            return new Mensagem("SUCESSO", historicoFormatado);

        } catch (Exception e) {
            return new Mensagem("ERRO", "Erro ao obter histórico: " + e.getMessage());
        }
    }

    /**
     * Gera relatório de preços
     *
     * @return Mensagem com produtos ordenados por nome
     */
    private Mensagem gerarRelatorioPrecos() {
        try {
            ArrayList<Produto> produtos = (ArrayList<Produto>) produtoDAO.getMinhaLista();
            List<Produto> produtosOrdenados = produtos.stream()
                    .sorted(Comparator.comparing(Produto::getProduto))
                    .collect(Collectors.toList());
            return new Mensagem("SUCESSO", produtosOrdenados);
        } catch (Exception e) {
            return new Mensagem("ERRO", "Erro ao gerar relatório de preços: " + e.getMessage());
        }
    }

    /**
     * Gera balanço físico-financeiro
     *
     * @return Mensagem com itens e valor total do estoque
     */
    private Mensagem gerarBalancoFisicoFinanceiro() {
        try {
            ArrayList<Produto> produtos = (ArrayList<Produto>) produtoDAO.getMinhaLista();
            Map<String, Object> resultado = new HashMap<>();
            double valorTotalEstoque = 0;

            List<Map<String, Object>> itens = produtos.stream()
                    .sorted(Comparator.comparing(Produto::getProduto))
                    .map(produto -> {
                        double valorTotal = produto.getPreco() * produto.getQuantidade();
                        Map<String, Object> item = new HashMap<>();
                        item.put("produto", produto);
                        item.put("valorTotal", valorTotal);
                        return item;
                    })
                    .collect(Collectors.toList());

            valorTotalEstoque = itens.stream()
                    .mapToDouble(item -> (Double) item.get("valorTotal"))
                    .sum();

            resultado.put("itens", itens);
            resultado.put("valorTotalEstoque", valorTotalEstoque);

            return new Mensagem("SUCESSO", resultado);
        } catch (Exception e) {
            return new Mensagem("ERRO", "Erro ao gerar balanço: " + e.getMessage());
        }
    }

    /**
     * Gera relatório de estoque mínimo
     *
     * @return Mensagem com produtos abaixo do estoque mínimo
     */
    private Mensagem gerarRelatorioEstoqueMinimo() {
        try {
            ArrayList<Produto> produtos = (ArrayList<Produto>) produtoDAO.getMinhaLista();
            List<Produto> produtosAbaixoMinimo = produtos.stream()
                    .filter(p -> p.getQuantidade() < p.getEstoqueminimo())
                    .collect(Collectors.toList());

            return new Mensagem("SUCESSO", produtosAbaixoMinimo);
        } catch (Exception e) {
            return new Mensagem("ERRO", "Erro ao gerar relatório de estoque mínimo: " + e.getMessage());
        }
    }

    /**
     * Gera relatório de estoque máximo
     *
     * @return Mensagem com produtos acima do estoque máximo
     */
    private Mensagem gerarRelatorioEstoqueMaximo() {
        try {
            ArrayList<Produto> produtos = (ArrayList<Produto>) produtoDAO.getMinhaLista();
            List<Produto> produtosAcimaMaximo = produtos.stream()
                    .filter(p -> p.getQuantidade() > p.getEstoquemaximo())
                    .collect(Collectors.toList());

            return new Mensagem("SUCESSO", produtosAcimaMaximo);
        } catch (Exception e) {
            return new Mensagem("ERRO", "Erro ao gerar relatório de estoque máximo: " + e.getMessage());
        }
    }

    /**
     * Gera relatório de produtos por categoria
     *
     * @return Mensagem com quantidade de produtos por categoria
     */
    private Mensagem gerarRelatorioProdutosPorCategoria() {
        try {
            ArrayList<Produto> produtos = (ArrayList<Produto>) produtoDAO.getMinhaLista();
            Map<String, Long> produtosPorCategoria = produtos.stream()
                    .collect(Collectors.groupingBy(
                            Produto::getCategoria,
                            Collectors.counting()
                    ));
            return new Mensagem("SUCESSO", produtosPorCategoria);
        } catch (Exception e) {
            return new Mensagem("ERRO", "Erro ao gerar relatório por categoria: " + e.getMessage());
        }
    }

    /**
     * Aplica reajuste de preços em um produto específico
     *
     * @param produto Produto com novo preço
     * @return Mensagem com resultado da operação
     */
    private Mensagem reajustarPrecos(Produto produto) {
        try {
            Produto produtoAtual = buscarProdutoPorId(produto.getId());
            if (produtoAtual == null) {
                return new Mensagem("REAJUSTAR_PRECOS", "Produto não encontrado", "ERRO");
            }

            produtoAtual.setPreco(produto.getPreco());

            if (produtoDAO.updateProdutoBD(produtoAtual)) {
                return new Mensagem("REAJUSTAR_PRECOS", "Preço do produto " + produtoAtual.getProduto() + " atualizado com sucesso", "SUCESSO");
            } else {
                return new Mensagem("REAJUSTAR_PRECOS", "Erro ao atualizar preço do produto", "ERRO");
            }

        } catch (Exception e) {
            return new Mensagem("REAJUSTAR_PRECOS", "Erro ao reajustar preços: " + e.getMessage(), "ERRO");
        }
    }

    /**
     * Busca produto por ID
     *
     * @param id ID do produto
     * @return Produto encontrado ou null
     */
    private Produto buscarProdutoPorId(int id) {
        for (Produto p : produtoDAO.getMinhaLista()) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    /**
     * Cria Map do produto para resposta
     *
     * @param produto Produto a ser convertido
     * @return Map com dados do produto
     */
    private Map<String, Object> criarMapProduto(Produto produto) {
        Map<String, Object> mapProduto = new HashMap<>();
        mapProduto.put("id", produto.getId());
        mapProduto.put("produto", produto.getProduto());
        mapProduto.put("preco", produto.getPreco());
        mapProduto.put("unidade", produto.getUnidade());
        mapProduto.put("categoria", produto.getCategoria());
        mapProduto.put("quantidade", produto.getQuantidade());
        mapProduto.put("estoqueMinimo", produto.getEstoqueminimo());
        mapProduto.put("estoqueMaximo", produto.getEstoquemaximo());
        return mapProduto;
    }

    /**
     * Busca produto por nome
     *
     * @param nome Nome do produto
     * @return Produto encontrado ou null
     */
    private Produto buscarProdutoPorNome(String nome) {
        for (Produto p : produtoDAO.getMinhaLista()) {
            if (p.getProduto().equalsIgnoreCase(nome)) {
                return p;
            }
        }
        return null;
    }
}
