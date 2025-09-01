package negocio;

import negocio.entidade.Usuario;

/**
 * Classe responsável por gerenciar a sessão do usuário no sistema.
 * Controla o estado de login/logout, autenticação e operações do usuário logado.
 * Coordena com NegocioUsuario para operações que requerem usuário logado.
 */

public class NegocioSessao {

    private Usuario usuarioLogado;
    private final NegocioUsuario negocioUsuario;

    public NegocioSessao(NegocioUsuario negocioUsuario) throws IllegalArgumentException {
        if (negocioUsuario == null) {
            throw new IllegalArgumentException("NegocioUsuario não pode ser nulo");
        }
        this.usuarioLogado = null;
        this.negocioUsuario = negocioUsuario;
    }
    
    public Usuario getUsuarioLogado() throws IllegalStateException {
        if (!estaLogado()) {
            throw new IllegalStateException("Nenhum usuário está logado");
        }
        return usuarioLogado;
    }
    
    public boolean autenticar(String email, String senha) throws IllegalArgumentException, IllegalStateException {
        if (estaLogado()) {
            throw new IllegalStateException("Já existe um usuário logado. Faça logout primeiro.");
        }
        
        Usuario usuario = negocioUsuario.validarCredenciais(email, senha);
        if (usuario != null) {
            login(usuario);
            return true;
        }
        return false;
    }

    private void login(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo");
        }
        
        this.usuarioLogado = usuario;
    }
    
    public void logout() throws IllegalStateException {
        if (!estaLogado()) {
            throw new IllegalStateException("Nenhum usuário está logado para fazer logout");
        }
        this.usuarioLogado = null;
    }
    
    public boolean estaLogado() {
        return usuarioLogado != null;
    }
    
    public String getNomeUsuarioLogado() throws IllegalStateException {
        return getUsuarioLogado().getNome();
    }
    
    public String getIdUsuarioLogado() throws IllegalStateException {
        return getUsuarioLogado().getId();
    }

    public boolean isUsuarioLogado(Usuario usuario) {
        return estaLogado() && usuarioLogado.equals(usuario);
    }

    public void alterarSenhaUsuarioLogado(String senhaAtual, String novaSenha) 
            throws IllegalArgumentException, IllegalStateException {
        Usuario usuarioLogado = getUsuarioLogado();
        negocioUsuario.alterarSenha(usuarioLogado, senhaAtual, novaSenha);
    }

    public void excluirContaUsuarioLogado(String senhaConfirmacao) 
            throws IllegalArgumentException, IllegalStateException {
        Usuario usuarioLogado = getUsuarioLogado();
        negocioUsuario.excluirConta(usuarioLogado, senhaConfirmacao);
        logout();
    }
}
