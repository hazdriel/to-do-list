package dados;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import negocio.entidade.Categoria;
import negocio.excecao.categoria.CategoriaVaziaException;

// Repositório para gerenciar persistência de categorias

public class RepositorioCategorias {
    
    private final Map<String, Categoria> categorias = new HashMap<>();
    private final PersistenciaArquivo<Categoria> persistencia;
    
    public RepositorioCategorias() {
        this.persistencia = new PersistenciaArquivo<>("categorias");
        carregarDados();
    }
    
    public RepositorioCategorias(PersistenciaArquivo<Categoria> persistencia) {
        this.persistencia = persistencia;
        carregarDados();
    }
    
    // MÉTODOS BÁSICOS DE CRUD
    
    public void inserirCategoria(Categoria categoria) throws CategoriaVaziaException {
        if (categoria == null) {
            throw new CategoriaVaziaException();
        }
        categorias.put(categoria.getNome(), categoria);
        salvarDados();
    }
    
    public Categoria buscarCategoria(String nome) {
        return categorias.get(nome);
    }
    
    public Optional<Categoria> buscarPorNome(String nome) {
        return Optional.ofNullable(categorias.get(nome));
    }
    
    public boolean removerCategoria(String nome) {
        boolean removido = categorias.remove(nome) != null;
        if (removido) {
            salvarDados();
        }
        return removido;
    }
    
    // MÉTODOS PARA LISTAGEM E FILTRAGEM
    
    public List<Categoria> listarCategorias() {
        return new ArrayList<>(categorias.values());
    }
    
    public int getTotalCategorias() {
        return categorias.size();
    }
    
    // MÉTODOS DE PERSISTÊNCIA
    
    public void limparTodosAsCategorias() {
        try {
            categorias.clear();
            persistencia.limparDados();
        } catch (PersistenciaException e) {
            System.err.println("Erro ao limpar dados de categorias: " + e.getMessage());
        }
    }
    
    private void carregarDados() {
        try {
            List<Categoria> categoriasCarregadas = persistencia.carregar();
            categorias.clear();
            
            for (Categoria categoria : categoriasCarregadas) {
                categorias.put(categoria.getNome(), categoria);
            }
            
        } catch (PersistenciaException e) {
            System.err.println("Erro ao carregar categorias: " + e.getMessage());
        }
    }
    
    private void salvarDados() {
        try {
            List<Categoria> listaCategorias = new ArrayList<>(categorias.values());
            persistencia.salvar(listaCategorias);
            
        } catch (PersistenciaException e) {
            System.err.println("Erro ao salvar categorias: " + e.getMessage());
        }
    }
}
