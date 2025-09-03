package dados;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

import negocio.entidade.Usuario;
import negocio.entidade.GeradorId;

// Repositório para gerenciar persistência de usuários
import negocio.excecao.usuario.UsuarioVazioException;

public class RepositorioUsuarios {
    
    private final Map<String, Usuario> usuarios = new HashMap<>();
    private final PersistenciaArquivo<Usuario> persistencia;
    
    public RepositorioUsuarios() {
        this.persistencia = new PersistenciaArquivo<>("usuarios");
        carregarDados();
    }
    
    public RepositorioUsuarios(PersistenciaArquivo<Usuario> persistencia) {
        this.persistencia = persistencia;
        carregarDados();
    }
    
    // MÉTODOS BÁSICOS DE CRUD
    
    public void inserirUsuario(Usuario usuario) throws UsuarioVazioException {
        if (usuario == null) {
            throw new UsuarioVazioException();
        }
        
        usuarios.put(usuario.getEmail(), usuario);
        salvarDados();
    }
    
    public Usuario buscarUsuario(String email) {
        return usuarios.get(email);
    }
    
    public Optional<Usuario> buscarPorId(String id) {
        if (id == null) {
            return Optional.empty();
        }
        
        return usuarios.values().stream()
            .filter(u -> u.getId().equals(id))
            .findFirst();
    }
    
    public Optional<Usuario> buscarPorEmail(String email) {
        return Optional.ofNullable(usuarios.get(email));
    }
    
    public List<Usuario> listarTodos() {
        return new ArrayList<>(usuarios.values());
    }
    
    public void atualizarUsuario(Usuario usuario) throws UsuarioVazioException {
        if (usuario == null) {
            throw new UsuarioVazioException();
        }
        
        usuarios.put(usuario.getEmail(), usuario);
        salvarDados();
    }
    
    public boolean removerUsuario(String email) {
        boolean removido = usuarios.remove(email) != null;
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
    
    public boolean existeUsuario(String email) {
        return usuarios.containsKey(email);
    }
    
    // MÉTODOS DE PERSISTÊNCIA
    
    private void carregarDados() {
        try {
            List<Usuario> usuariosCarregados = persistencia.carregar();
            usuarios.clear();
            
            int maxIdUsuario = 0;
            for (Usuario usuario : usuariosCarregados) {
                usuarios.put(usuario.getEmail(), usuario);
                
                int numeroId = GeradorId.extrairNumeroId(usuario.getId());
                maxIdUsuario = Math.max(maxIdUsuario, numeroId);
            }
            
            if (maxIdUsuario > 0) {
                GeradorId.sincronizarContadorUsuarios(maxIdUsuario);
            }
            
        } catch (PersistenciaException e) {
            System.err.println("Erro ao carregar usuários: " + e.getMessage());
        }
    }
    
    private void salvarDados() {
        try {
            List<Usuario> listaUsuarios = new ArrayList<>(usuarios.values());
            persistencia.salvar(listaUsuarios);
            
        } catch (PersistenciaException e) {
            System.err.println("Erro ao salvar usuários: " + e.getMessage());
        }
    }
}
