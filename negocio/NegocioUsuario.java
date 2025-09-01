package negocio;

import dados.RepositorioUsuarios;
import negocio.entidade.Usuario;
import negocio.excecao.usuario.*;

/**
 * Classe responsável pela lógica de negócio relacionada aos usuários.
 * Gerencia CRUD de usuários, validação de credenciais e operações de conta.
 */
public class NegocioUsuario {
  private final RepositorioUsuarios repositorio;

  public NegocioUsuario(RepositorioUsuarios repositorio) throws RepositorioUsuariosVazioException {
    if (repositorio == null) {
      throw new RepositorioUsuariosVazioException();
    }
    
    this.repositorio = repositorio;
  }

  public Usuario validarCredenciais(String email, String senha) throws EmailVazioException, SenhaVaziaException  {
    if (email == null || email.trim().isEmpty()) {
      throw new EmailVazioException();
    }
    if (senha == null || senha.trim().isEmpty()) {
      throw new SenhaVaziaException();
    }

    Usuario usuario = repositorio.buscarUsuario(email.trim());

    if (usuario == null) {
      return null; // Usuário não encontrado
    }
    
    if (!usuario.getSenha().equals(senha)) {
      return null; // Senha incorreta
    }

    return usuario; // Credenciais válidas
  }

  public boolean usuarioExiste(String email) {
    return repositorio.existeUsuario(email);
  }

  public void cadastrarUsuario(String nome, String email, String senha) throws NomeVazioException, EmailVazioException, SenhaVaziaException, UsuarioExistenteException, SenhaTamanhoInvalidoException, NomeApenasLetrasException, NomeTamanhoInvalidoException, EmailFormatoInvalidoException {
    if (nome == null || nome.trim().isEmpty()) {
      throw new NomeVazioException();
    }
    if (email == null || email.trim().isEmpty()) {
      throw new EmailVazioException();
    }
    if (senha == null || senha.trim().isEmpty()) {
      throw new SenhaVaziaException();
    }

    if (usuarioExiste(email.trim())) {
      throw new UsuarioExistenteException(email);
    }

    Usuario novoUsuario = new Usuario(nome.trim(), email.trim(), senha);
    repositorio.inserirUsuario(novoUsuario);
  }


  public Usuario buscarUsuarioPorId(String id) throws IDUsuarioVazio {
    if (id == null || id.trim().isEmpty()) {
      throw new IDUsuarioVazio();
    }
    
    return repositorio.buscarPorId(id.trim()).orElse(null);
  }

  public Usuario buscarUsuarioPorEmail(String email) throws EmailVazioException {
    if (email == null || email.trim().isEmpty()) {
      throw new EmailVazioException();
    }
    
    return repositorio.buscarUsuario(email.trim());
  }

  public java.util.List<Usuario> listarTodos() {
    return repositorio.listarTodos();
  }

  public void alterarSenha(Usuario usuario, String senhaAtual, String novaSenha)
          throws IllegalArgumentException, UsuarioVazioException, SenhaVaziaException, SenhaIncorretaException, SenhaTamanhoInvalidoException {
    if (usuario == null) {
      throw new UsuarioVazioException();
    }
    if (senhaAtual == null || senhaAtual.trim().isEmpty()) {
      throw new SenhaVaziaException();
    }
    if (novaSenha == null || novaSenha.trim().isEmpty()) {
      throw new SenhaVaziaException();
    }
    
    if (!usuario.getSenha().equals(senhaAtual)) {
      throw new SenhaIncorretaException(usuario.getEmail());
    }

    usuario.setSenha(novaSenha);
    repositorio.atualizarUsuario(usuario);
  }

  public void excluirConta(Usuario usuario, String senhaConfirmacao)
          throws IllegalArgumentException, UsuarioVazioException, SenhaVaziaException, SenhaIncorretaException {
    if (usuario == null) {
      throw new UsuarioVazioException();
    }
    if (senhaConfirmacao == null || senhaConfirmacao.trim().isEmpty()) {
      throw new SenhaVaziaException();
    }
    
    if (!usuario.getSenha().equals(senhaConfirmacao)) {
      throw new SenhaIncorretaException(usuario.getEmail());
    }

    repositorio.removerUsuario(usuario.getEmail());
  }
}