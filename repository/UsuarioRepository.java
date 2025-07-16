package repository;

import model.Usuario;

import java.util.Map;
import java.util.HashMap;

public class UsuarioRepository {

    private Map<String, Usuario> usuarios = new HashMap<>();

    public boolean inserirUsuario(Usuario usuario) {
        if (usuarios.containsKey(usuario.getEmail())) {
            return false;
        }
        usuarios.put(usuario.getEmail(), usuario);
        return true;
    }

    public Usuario buscarUsuario(String email) {
        return usuarios.get(email);
    }

    public boolean atualizarUsuario(Usuario usuarioAtualizado) {
        if (usuarios.containsKey(usuarioAtualizado.getEmail())) {
            usuarios.put(usuarioAtualizado.getEmail(), usuarioAtualizado);
            return true;
        }
        return false;
    }

    public boolean removerUsuario(String email) {
        return usuarios.remove(email) != null;
    }

}