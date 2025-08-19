package negocio;

import dados.RepositorioUsuarios;
import negocio.entidade.Usuario;

import negocio.excecao.usuario.*;

public class NegocioUsuario {
  private RepositorioUsuarios repositorio;
  private NegocioSessao sessao;


  public NegocioUsuario(RepositorioUsuarios repositorio, NegocioSessao sessao) {
    this.repositorio = repositorio;
    this.sessao = sessao;
  }

  public boolean autenticar(String email, String senha) throws UsuarioNaoEncontradoException, SenhaIncorretaException {
    Usuario usuario = repositorio.buscarUsuario(email);

    if (usuario == null) {
      throw new UsuarioNaoEncontradoException(email);
    }
    if (!usuario.getSenha().equals(senha)) {
      throw new SenhaIncorretaException(email);
    }

    sessao.login(usuario);
    return true;
  }

  public boolean usuarioExiste(String email) {
    return repositorio.buscarUsuario(email) != null;
  }

  public void cadastrarUsuario(String nome, String email, String senha)
          throws UsuarioExistenteException, NomeVazioException, NomeApenasLetrasException,
          NomeTamanhoInvalidoException, EmailVazioException, EmailFormatoInvalidoException,
          SenhaTamanhoInvalidoException {

    if (usuarioExiste(email)) {
      throw new UsuarioExistenteException(email);
    }

    Usuario.validarNome(nome);
    Usuario.validarEmail(email);
    Usuario.validarSenha(senha);

    Usuario novoUsuario = new Usuario(nome, email, senha);
    repositorio.inserirUsuario(novoUsuario);
  }

}