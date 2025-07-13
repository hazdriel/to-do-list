package repository;

import model.Usuario;
import java.util.ArrayList;

public class UsuarioRepository {
    private ArrayList<Usuario> usuarios = new ArrayList<>();

    public boolean inserirUsuario(Usuario usuario) {
        if (usuario != null) {
            return usuarios.add(usuario);
        }
        return false;
    }

    public ArrayList<Usuario> listarUsuarios() {
        return new ArrayList<>(usuarios);
    }

    public Usuario buscarUsuarioPorNome(String nome) {
        for (Usuario usuario : usuarios) {
            if (usuario.getNome().equals(nome)) {
                return usuario;
            }
        }
        return null;
    }

    public boolean atualizarUsuario(Usuario usuarioAtualizado) {
        if (usuarioAtualizado == null) {
            return false;
        }
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getNome().equals(usuarioAtualizado.getNome())) {
                usuarios.set(i, usuarioAtualizado);
                return true;
            }
        }
        return false;
    }

    public boolean removerUsuario(String nome) {
        return usuarios.removeIf(c -> c.getNome().equals(nome));
    }
}
