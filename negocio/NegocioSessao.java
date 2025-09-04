package negocio;

import negocio.entidade.Usuario;
import negocio.excecao.sessao.SessaoJaInativaException;
import negocio.excecao.sessao.NegocioUsuarioVazioException;
import negocio.excecao.sessao.LoginJaAtivoException;
import negocio.excecao.usuario.*;

/**
 * Classe responsável por gerenciar a sessão do usuário no sistema.
 * Controla o estado de login/logout, autenticação e operações do usuário logado.
 * Coordena com NegocioUsuario para operações que requerem usuário logado.
 */

public class NegocioSessao {

    private Usuario usuarioLogado;
    private final NegocioUsuario negocioUsuario;

    public NegocioSessao(NegocioUsuario negocioUsuario) throws NegocioUsuarioVazioException {
        if (negocioUsuario == null) {
            throw new NegocioUsuarioVazioException();
        }
        this.usuarioLogado = null;
        this.negocioUsuario = negocioUsuario;
    }
    
    public Usuario getUsuarioLogado() throws SessaoJaInativaException {
        if (!estaLogado()) {
            throw new SessaoJaInativaException();
        }
        return usuarioLogado;
    }
    
    public boolean autenticar(String email, String senha) throws LoginJaAtivoException, EmailVazioException, SenhaVaziaException, UsuarioVazioException {
        if (estaLogado()) {
            throw new LoginJaAtivoException();
        }
        
        Usuario usuario = negocioUsuario.validarCredenciais(email, senha);
        if (usuario != null) {
            login(usuario);
            return true;
        }
        return false;
    }

    private void login(Usuario usuario) throws UsuarioVazioException {
        if (usuario == null) {
            throw new UsuarioVazioException();
        }
        
        this.usuarioLogado = usuario;
    }
    
    public void logout() throws SessaoJaInativaException {
        if (!estaLogado()) {
            throw new SessaoJaInativaException();
        }
        this.usuarioLogado = null;
    }
    
    public boolean estaLogado() {
        return usuarioLogado != null;
    }
    
    public String getNomeUsuarioLogado() throws SessaoJaInativaException {
        return getUsuarioLogado().getNome();
    }
    
    public String getIdUsuarioLogado() throws SessaoJaInativaException {
        return getUsuarioLogado().getId();
    }

    public boolean isUsuarioLogado(Usuario usuario) {
        return estaLogado() && usuarioLogado.equals(usuario);
    }

    public void alterarSenhaUsuarioLogado(String senhaAtual, String novaSenha)
            throws SessaoJaInativaException, SenhaTamanhoInvalidoException, SenhaIncorretaException, SenhaVaziaException, UsuarioVazioException {
        Usuario usuarioLogado = getUsuarioLogado();
        negocioUsuario.alterarSenha(usuarioLogado, senhaAtual, novaSenha);
    }

    public void excluirContaUsuarioLogado(String senhaConfirmacao)
            throws SessaoJaInativaException, SenhaIncorretaException, SenhaVaziaException, UsuarioVazioException {
        Usuario usuarioLogado = getUsuarioLogado();
        negocioUsuario.excluirConta(usuarioLogado, senhaConfirmacao);
        logout();
    }
}
