package negocio;

import dados.RepositorioCategorias;
import negocio.entidade.Categoria;
import negocio.entidade.Usuario;
import java.util.List;
import java.util.ArrayList;

public class NegocioCategoria {
    private RepositorioCategorias repositorioCategorias;
    private NegocioSessao sessao;
    private NegocioTarefa negocioTarefa;
    
    public NegocioCategoria(RepositorioCategorias repositorioCategorias, NegocioSessao sessao) {
        if (repositorioCategorias == null) {
            throw new IllegalArgumentException("Repositório de categorias não pode ser nulo");
        }
        if (sessao == null) {
            throw new IllegalArgumentException("Sessão não pode ser nula");
        }
        this.repositorioCategorias = repositorioCategorias;
        this.sessao = sessao;
    }
    
    public NegocioCategoria(RepositorioCategorias repositorioCategorias, NegocioSessao sessao, NegocioTarefa negocioTarefa) {
        if (repositorioCategorias == null) {
            throw new IllegalArgumentException("Repositório de categorias não pode ser nulo");
        }
        if (sessao == null) {
            throw new IllegalArgumentException("Sessão não pode ser nula");
        }
        if (negocioTarefa == null) {
            throw new IllegalArgumentException("Negócio de tarefa não pode ser nulo");
        }
        this.repositorioCategorias = repositorioCategorias;
        this.sessao = sessao;
        this.negocioTarefa = negocioTarefa;
    }
    
    // MÉTODOS DE LISTAGEM

    public List<Categoria> listarCategoriasDoUsuario() throws IllegalStateException {
        Usuario usuario = sessao.getUsuarioLogado();
        if (usuario == null) {
            throw new IllegalStateException("Usuário não está logado");
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

    public Categoria criarCategoria(String nome) throws IllegalArgumentException, IllegalStateException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da categoria não pode ser nulo ou vazio");
        }
        
        Usuario usuario = sessao.getUsuarioLogado();
        if (usuario == null) {
            throw new IllegalStateException("Usuário não está logado");
        }
        
        Categoria novaCategoria = new Categoria(nome, usuario);
        repositorioCategorias.inserirCategoria(novaCategoria);
        return novaCategoria;
    }
    
    // MÉTODOS DE REMOÇÃO

    public boolean removerCategoria(String nomeCategoria, boolean temTarefasAssociadas) throws IllegalArgumentException, IllegalStateException {
        if (nomeCategoria == null || nomeCategoria.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da categoria não pode ser nulo ou vazio");
        }
        
        Usuario usuario = sessao.getUsuarioLogado();
        if (usuario == null) {
            throw new IllegalStateException("Usuário não está logado");
        }
        
        // Verificar se é categoria padrão
        if (isCategoriaPadrao(nomeCategoria)) {
            throw new IllegalStateException("Não é possível remover a categoria padrão '" + nomeCategoria + "'");
        }
        
        // Verificar se há tarefas associadas
        if (temTarefasAssociadas) {
            throw new IllegalStateException("Não é possível remover a categoria '" + nomeCategoria + "' pois existem tarefas associadas a ela");
        }
        
        // Verificar se o usuário tem permissão
        Categoria categoria = repositorioCategorias.buscarCategoria(nomeCategoria);
        if (categoria == null) {
            throw new IllegalArgumentException("Categoria não encontrada: '" + nomeCategoria + "'");
        }
        
        if (!categoria.foiCriadaPor(usuario)) {
            throw new IllegalStateException("Você não tem permissão para remover esta categoria");
        }
        
        // Remover categoria
        if (repositorioCategorias.removerCategoria(nomeCategoria)) {
            return true;
        } else {
            throw new IllegalStateException("Erro ao remover categoria do repositório");
        }
    }
    
    public boolean removerCategoria(String nomeCategoria) throws IllegalArgumentException, IllegalStateException {
        if (nomeCategoria == null || nomeCategoria.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da categoria não pode ser nulo ou vazio");
        }
        
        Categoria categoria = repositorioCategorias.buscarCategoria(nomeCategoria);
        
        if (categoria == null) {
            throw new IllegalArgumentException("Categoria não encontrada: '" + nomeCategoria + "'");
        }
        
        boolean temTarefasAssociadas = false;
        if (negocioTarefa != null) {
            temTarefasAssociadas = !negocioTarefa.podeRemoverCategoria(categoria);
        }
        
        return removerCategoria(nomeCategoria, temTarefasAssociadas);
    }
    
    // MÉTODOS AUXILIARES

    public boolean isCategoriaPadrao(String nomeCategoria) throws IllegalArgumentException {
        if (nomeCategoria == null || nomeCategoria.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da categoria não pode ser nulo ou vazio");
        }
        
        Categoria categoria = repositorioCategorias.buscarCategoria(nomeCategoria);
        return categoria != null && categoria.isPadrao();
    }
    
    public void garantirCategoriasPadrao() {
        String[] categoriasPadrao = {"Trabalho", "Estudo", "Pessoal"};
        
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

