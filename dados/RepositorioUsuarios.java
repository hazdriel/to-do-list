package dados;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import negocio.entidade.Usuario;
import negocio.entidade.GeradorId;

// Repositório de usuários que salva em arquivo .dat
public class RepositorioUsuarios {
    
    private final Map<String, Usuario> usuarios = new HashMap<>();
    private final IPersistencia<Usuario> persistencia;
    
    // Construtor que carrega dados existentes
    public RepositorioUsuarios() {
        this.persistencia = new PersistenciaArquivo<>("usuarios");
        carregarDados();
    }
    
    // Construtor para testes
    public RepositorioUsuarios(IPersistencia<Usuario> persistencia) {
        this.persistencia = persistencia;
        carregarDados();
    }
    
    public void inserirUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo");
        }
        
        if (usuarios.containsKey(usuario.getEmail())) {
            throw new IllegalArgumentException("Usuário já existe com este email");
        }
        
        usuarios.put(usuario.getEmail(), usuario);
        salvarDados();
    }
    
    public Usuario buscarUsuario(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
        return usuarios.get(email.trim());
    }
    
    public List<Usuario> listarTodos() {
        return new ArrayList<>(usuarios.values());
    }
    
    public boolean atualizarUsuario(Usuario usuario) {
        if (usuario == null || !usuarios.containsKey(usuario.getEmail())) {
            return false;
        }
        
        usuarios.put(usuario.getEmail(), usuario);
        salvarDados();
        return true;
    }
    
    public boolean removerUsuario(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        boolean removido = usuarios.remove(email.trim()) != null;
        if (removido) {
            salvarDados();
        }
        return removido;
    }
    
    public int getTotalUsuarios() {
        return usuarios.size();
    }
    
    public boolean temUsuarios() {
        return !usuarios.isEmpty();
    }
    
    public void limparTodos() {
        try {
            usuarios.clear();
            persistencia.limparDados();
            System.out.println("Todos os usuários foram removidos");
            
        } catch (PersistenciaException e) {
            System.err.println("Erro ao limpar usuários: " + e.getMessage());
        }
    }
    
    // Carrega dados do arquivo
    private void carregarDados() {
        try {
            List<Usuario> usuariosCarregados = persistencia.carregar();
            usuarios.clear();
            
            int maxIdUsuario = 0;
            for (Usuario usuario : usuariosCarregados) {
                usuarios.put(usuario.getEmail(), usuario);
                
                // Encontrar o maior ID para sincronizar o contador
                int numeroId = GeradorId.extrairNumeroId(usuario.getId());
                maxIdUsuario = Math.max(maxIdUsuario, numeroId);
            }
            
            // Sincronizar o contador para evitar IDs duplicados
            if (maxIdUsuario > 0) {
                GeradorId.sincronizarContadorUsuarios(maxIdUsuario);
            }
            
            System.out.println("Carregados " + usuarios.size() + " usuários do arquivo");
            System.out.println("Contador de IDs sincronizado: " + maxIdUsuario);
            
        } catch (PersistenciaException e) {
            System.out.println("Erro ao carregar usuários: " + e.getMessage());
        }
    }
    
    // Salva dados no arquivo
    private void salvarDados() {
        try {
            List<Usuario> listaUsuarios = new ArrayList<>(usuarios.values());
            persistencia.salvar(listaUsuarios);
            
        } catch (PersistenciaException e) {
            System.err.println("Erro ao salvar usuários: " + e.getMessage());
        }
    }
}
