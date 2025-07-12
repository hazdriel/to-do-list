package repository;

import model.Usuario;

import java.util.ArrayList;

public class UsuarioRepository {

    private ArrayList<Usuario> usuarios = new ArrayList<>();

    public void inserirUsuarios(Usuario usuario) {
        usuarios.add(usuario);
    }

    public Usuario buscarUsuarios(String email) {
        for (Usuario usuario : usuarios) {
            if (usuario.getEmail().equals(email)) {
                return usuario;
            }
        }
        return null;
    }

    public boolean atualizarUsuarios(Usuario usuarioAtualizado) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getEmail().equals(usuarioAtualizado.getEmail())) {
                usuarios.set(i, usuarioAtualizado);
                return true;
            }
        }
        return false;
    }

    public boolean removerUsuarios(String email) {
        return usuarios.removeIf(c -> c.getEmail().equals(email));
    }

}