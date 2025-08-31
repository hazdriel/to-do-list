package dados;

import negocio.entidade.Categoria;
import negocio.entidade.Usuario;
import java.util.*;

/**
 * Repositório para gerenciar categorias de usuários.
 */
public class RepositorioCategorias {
    private Map<String, Categoria> categorias;
    private Map<Usuario, Set<Categoria>> categoriasPorUsuario;

    public RepositorioCategorias() {
        this.categorias = new HashMap<>();
        this.categoriasPorUsuario = new HashMap<>();
        
        inicializarCategoriasPadrao();
    }

    private void inicializarCategoriasPadrao() {
        try {
            Categoria pessoal = new Categoria("Pessoal");
            Categoria estudo = new Categoria("Estudo");
            Categoria trabalho = new Categoria("Trabalho");
            
            categorias.put("Pessoal", pessoal);
            categorias.put("Estudo", estudo);
            categorias.put("Trabalho", trabalho);
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao inicializar categorias padrão: " + e.getMessage());
        }
    }

    public void inserirCategoria(Categoria categoria, Usuario usuario) {
        categorias.put(categoria.getNome(), categoria);
        
        if (!categoriasPorUsuario.containsKey(usuario)) {
            categoriasPorUsuario.put(usuario, new HashSet<>());
        }
        
        categoriasPorUsuario.get(usuario).add(categoria);
    }

    public List<Categoria> buscarCategoriasDoUsuario(Usuario usuario) {
        Set<Categoria> todasCategorias = new HashSet<>();
        
        for (Categoria categoria : categorias.values()) {
            todasCategorias.add(categoria);
        }
        Set<Categoria> categoriasUsuario = categoriasPorUsuario.get(usuario);
        if (categoriasUsuario != null) {
            todasCategorias.addAll(categoriasUsuario);
        }
        
        return new ArrayList<>(todasCategorias);
    }

    public Categoria buscarCategoriaPorNome(String nome) {
        return categorias.get(nome);
    }


    public Categoria buscarCategoriaPorNome(String nome, Usuario usuario) {
        Categoria categoria = categorias.get(nome);
        if (categoria != null) {
            return categoria;
        }
    
        Set<Categoria> categoriasUsuario = categoriasPorUsuario.get(usuario);
        if (categoriasUsuario == null) {
            return null;
        }
        
        for (Categoria cat : categoriasUsuario) {
            if (cat.getNome().equalsIgnoreCase(nome)) {
                return cat;
            }
        }
        return null;
    }

 
    public boolean removerCategoria(String nomeCategoria, Usuario usuario) {
        Categoria categoria = buscarCategoriaPorNome(nomeCategoria, usuario);
        
        if (categoria == null) {
            System.out.println("Categoria '" + nomeCategoria + "' não encontrada.");
            return false;
        }
        
        if (categoria.isPadrao()) {
            System.out.println("Não é possível remover a categoria padrão '" + nomeCategoria + "'.");
            return false;
        }

        if (!categoria.foiCriadaPor(usuario)) {
            System.out.println("Você não tem permissão para remover a categoria '" + nomeCategoria + "'.");
            return false;
        }
        
        Set<Categoria> categoriasUsuario = categoriasPorUsuario.get(usuario);
        if (categoriasUsuario != null) {
            categoriasUsuario.remove(categoria);
        }
        categorias.remove(categoria.getNome());
        
        return true;
    }

    public boolean categoriaExiste(String nome, Usuario usuario) {
        return buscarCategoriaPorNome(nome, usuario) != null;
    }

    public boolean isCategoriaPadrao(String nome) {
        Categoria categoria = categorias.get(nome);
        return categoria != null && categoria.isPadrao();
    }
}
