package service;

import model.Usuario;

public class SessaoService {
    private Usuario usuarioLogado;

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void login(Usuario usuario) {
        this.usuarioLogado = usuario;
    }

    public void logout() {
        this.usuarioLogado = null;
    }

    public boolean estaLogado() {
        return usuarioLogado != null;
    }
}
