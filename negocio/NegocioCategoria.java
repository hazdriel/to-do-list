package negocio;

import dados.RepositorioCategorias;
import negocio.entidade.Categoria;
import negocio.entidade.Usuario;
import negocio.excecao.categoria.*;
import negocio.excecao.sessao.NegocioTarefaVazioException;
import negocio.excecao.sessao.SessaoJaInativaException;
import negocio.excecao.sessao.SessaoNulaException;
import negocio.excecao.tarefa.CriadorVazioException;

import java.util.List;
import java.util.ArrayList;

public class NegocioCategoria {
    private RepositorioCategorias repositorioCategorias;
    private NegocioSessao sessao;
    private NegocioTarefa negocioTarefa;
    
    public NegocioCategoria(RepositorioCategorias repositorioCategorias, NegocioSessao sessao) throws RepositorioCategoriaVazioException, SessaoNulaException {
        if (repositorioCategorias == null) {
            throw new RepositorioCategoriaVazioException();
        }
        if (sessao == null) {
            throw new SessaoNulaException();
        }
        this.repositorioCategorias = repositorioCategorias;
        this.sessao = sessao;
    }
    
    public NegocioCategoria(RepositorioCategorias repositorioCategorias, NegocioSessao sessao, NegocioTarefa negocioTarefa) throws RepositorioCategoriaVazioException, SessaoNulaException, NegocioTarefaVazioException {
        if (repositorioCategorias == null) {
            throw new RepositorioCategoriaVazioException();
        }
        if (sessao == null) {
            throw new SessaoNulaException();
        }
        if (negocioTarefa == null) {
            throw new NegocioTarefaVazioException();
        }
        this.repositorioCategorias = repositorioCategorias;
        this.sessao = sessao;
        this.negocioTarefa = negocioTarefa;
    }
    
    // MÉTODOS DE LISTAGEM

    public List<Categoria> listarCategoriasDoUsuario() throws SessaoJaInativaException {
        Usuario usuario = sessao.getUsuarioLogado();
        if (usuario == null) {
            throw new SessaoJaInativaException();
        }
        
        List<Categoria> todasCategorias = repositorioCategorias.listarCategorias();
        List<Categoria> resultado = new ArrayList<>();
        
        for (Categoria categoria : todasCategorias) {
            if (categoria.isPadrao()) {
                resultado.add(categoria);
            } else if (categoria.foiCriadaPor(usuario)) {
                resultado.add(categoria);
            }
        }
        
        return resultado;
    }

    // MÉTODOS DE CRIAÇÃO

    public Categoria criarCategoria(String nome) throws CategoriaVaziaException, SessaoJaInativaException, CriadorVazioException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new CategoriaVaziaException();
        }
        
        Usuario usuario = sessao.getUsuarioLogado();
        if (usuario == null) {
            throw new SessaoJaInativaException();
        }
        
        Categoria novaCategoria = new Categoria(nome, usuario);
        repositorioCategorias.inserirCategoria(novaCategoria);
        return novaCategoria;
    }
    
    // MÉTODOS DE REMOÇÃO

    public boolean removerCategoria(String nomeCategoria, boolean temTarefasAssociadas) throws CategoriaVaziaException, SessaoJaInativaException,
            CategoriaNaoEncontrada, CategoriaAtivaRemocaoException, CategoriaNaoPertenceException, RepositorioCategoriaRemocaoException {
        if (nomeCategoria == null || nomeCategoria.trim().isEmpty()) {
            throw new CategoriaVaziaException();
        }
        
        Usuario usuario = sessao.getUsuarioLogado();
        if (usuario == null) {
            throw new SessaoJaInativaException();
        }
        
        // Verificar se é categoria padrão
        if (isCategoriaPadrao(nomeCategoria)) {
            throw new CategoriaAtivaRemocaoException(nomeCategoria);
        }
        
        // Verificar se há tarefas associadas
        if (temTarefasAssociadas) {
            throw new CategoriaAtivaRemocaoException(nomeCategoria);
        }
        
        // Verificar se o usuário tem permissão
        Categoria categoria = repositorioCategorias.buscarCategoria(nomeCategoria);
        if (categoria == null) {
            throw new CategoriaNaoEncontrada(nomeCategoria);
        }
        
        if (!categoria.foiCriadaPor(usuario)) {
            throw new CategoriaNaoPertenceException(nomeCategoria);
        }
        
        // Remover categoria
        if (repositorioCategorias.removerCategoria(nomeCategoria)) {
            return true;
        } else {
            throw new RepositorioCategoriaRemocaoException();
        }
    }
    
    public boolean removerCategoria(String nomeCategoria) throws CategoriaVaziaException, CategoriaNaoEncontrada, SessaoJaInativaException, CategoriaNaoPertenceException, CategoriaAtivaRemocaoException, RepositorioCategoriaRemocaoException {
        if (nomeCategoria == null || nomeCategoria.trim().isEmpty()) {
            throw new CategoriaVaziaException();
        }
        
        Categoria categoria = repositorioCategorias.buscarCategoria(nomeCategoria);
        
        if (categoria == null) {
            throw new CategoriaNaoEncontrada(nomeCategoria);
        }
        
        boolean temTarefasAssociadas = false;
        if (negocioTarefa != null) {
            temTarefasAssociadas = !negocioTarefa.podeRemoverCategoria(categoria);
        }
        
        return removerCategoria(nomeCategoria, temTarefasAssociadas);
    }
    
    // MÉTODOS AUXILIARES

    public boolean isCategoriaPadrao(String nomeCategoria) throws CategoriaVaziaException {
        if (nomeCategoria == null || nomeCategoria.trim().isEmpty()) {
            throw new CategoriaVaziaException();
        }
        
        Categoria categoria = repositorioCategorias.buscarCategoria(nomeCategoria);
        return categoria != null && categoria.isPadrao();
    }
    
    public void garantirCategoriasPadrao() throws CategoriaVaziaException {
        String[] categoriasPadrao = {"Trabalho", "Estudos", "Pessoal"};
        
        for (String nomeCategoria : categoriasPadrao) {
            if (!existeCategoria(nomeCategoria)) {
                Categoria categoriaPadrao = new Categoria(nomeCategoria);
                repositorioCategorias.inserirCategoria(categoriaPadrao);
            }
        }
    }
    
    private boolean existeCategoria(String nomeCategoria) {
        return repositorioCategorias.buscarCategoria(nomeCategoria) != null;
    }
}

