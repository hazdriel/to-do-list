package dados;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import negocio.entidade.Categoria;
import negocio.entidade.Usuario;

// Repositório de categorias que salva em arquivo .dat
public class RepositorioCategoriasComPersistencia {
    
    private final Map<String, Categoria> categorias = new HashMap<>();
    private final IPersistencia<Categoria> persistencia;
    
    // Construtor que carrega dados existentes
    public RepositorioCategoriasComPersistencia() {
        this.persistencia = new PersistenciaArquivo<>("categorias");
        carregarDados();
        
        // Se não há dados salvos, inicializar com categorias padrão
        if (categorias.isEmpty()) {
            inicializarCategoriasPadrao();
        }
    }
    
    // Construtor para testes
    public RepositorioCategoriasComPersistencia(IPersistencia<Categoria> persistencia) {
        this.persistencia = persistencia;
        carregarDados();
        
        if (categorias.isEmpty()) {
            inicializarCategoriasPadrao();
        }
    }
    
    private void inicializarCategoriasPadrao() {
        Categoria trabalho = new Categoria("Trabalho");
        Categoria pessoal = new Categoria("Pessoal");
        Categoria estudos = new Categoria("Estudos");
        
        categorias.put(trabalho.getNome(), trabalho);
        categorias.put(pessoal.getNome(), pessoal);
        categorias.put(estudos.getNome(), estudos);
        
        salvarDados(); // Persistir as categorias padrão
        System.out.println("Categorias padrão inicializadas");
    }
    
    public void inserirCategoria(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("Categoria não pode ser nula");
        }
        categorias.put(categoria.getNome(), categoria);
        salvarDados();
    }
    
    public Categoria buscarCategoria(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return null;
        }
        return categorias.get(nome.trim());
    }
    
    public List<Categoria> listarCategorias() {
        return new ArrayList<>(categorias.values());
    }
    
    // Lista apenas categorias do usuário + categorias padrão
    public List<Categoria> listarCategoriasDoUsuario(Usuario usuario) {
        if (usuario == null) {
            return new ArrayList<>();
        }
        
        List<Categoria> resultado = new ArrayList<>();
        
        for (Categoria categoria : categorias.values()) {
            // Incluir categorias padrão
            if (categoria.isPadrao()) {
                resultado.add(categoria);
            }
            // Incluir categorias criadas pelo usuário
            else if (categoria.foiCriadaPor(usuario)) {
                resultado.add(categoria);
            }
        }
        
        return resultado;
    }
    
    public boolean removerCategoria(String nome, Usuario usuario) {
        if (nome == null || nome.trim().isEmpty() || usuario == null) {
            return false;
        }
        
        Categoria categoria = categorias.get(nome.trim());
        
        if (categoria == null) {
            return false; // Categoria não existe
        }
        
        // Verificar se é categoria padrão
        if (categoria.isPadrao()) {
            return false; // Não pode remover categoria padrão
        }
        
        // Verificar se o usuário é o criador
        if (!categoria.foiCriadaPor(usuario)) {
            return false; // Só o criador pode remover
        }
        
        boolean removido = categorias.remove(nome.trim()) != null;
        if (removido) {
            salvarDados();
        }
        return removido;
    }
    
    public boolean isCategoriaPadrao(String nome) {
        Categoria categoria = categorias.get(nome);
        return categoria != null && categoria.isPadrao();
    }
    
    public int getTotalCategorias() {
        return categorias.size();
    }
    
    public void limparTodos() {
        try {
            categorias.clear();
            persistencia.limparDados();
            System.out.println("Todos os dados de categorias foram removidos");
            
        } catch (PersistenciaException e) {
            System.err.println("Erro ao limpar dados de categorias: " + e.getMessage());
        }
    }
    
    // Carrega dados do arquivo
    private void carregarDados() {
        try {
            List<Categoria> categoriasCarregadas = persistencia.carregar();
            categorias.clear();
            
            for (Categoria categoria : categoriasCarregadas) {
                categorias.put(categoria.getNome(), categoria);
            }
            
            System.out.println("Carregadas " + categorias.size() + " categorias do arquivo");
            
        } catch (PersistenciaException e) {
            System.out.println("Erro ao carregar categorias: " + e.getMessage());
        }
    }
    
    // Salva dados no arquivo
    private void salvarDados() {
        try {
            List<Categoria> listaCategorias = new ArrayList<>(categorias.values());
            persistencia.salvar(listaCategorias);
            
        } catch (PersistenciaException e) {
            System.err.println("Erro ao salvar categorias: " + e.getMessage());
        }
    }
}
