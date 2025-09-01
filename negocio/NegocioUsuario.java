package negocio;

import dados.RepositorioUsuarios;
import negocio.entidade.Usuario;

<<<<<<< HEAD
import negocio.excecao.usuario.*;

=======
/**
 * Classe responsável pela lógica de negócio relacionada aos usuários.
 * Gerencia CRUD de usuários, validação de credenciais e operações de conta.
 */
>>>>>>> ef077eb4f53aab0c1a39c79dd604d9ea3815df9e
public class NegocioUsuario {
  private final RepositorioUsuarios repositorio;

<<<<<<< HEAD

  public NegocioUsuario(RepositorioUsuarios repositorio, NegocioSessao sessao) {
=======
  public NegocioUsuario(RepositorioUsuarios repositorio) throws IllegalArgumentException {
    if (repositorio == null) {
      throw new IllegalArgumentException("Repositório não pode ser nulo");
    }
    
>>>>>>> ef077eb4f53aab0c1a39c79dd604d9ea3815df9e
    this.repositorio = repositorio;
  }

<<<<<<< HEAD
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
=======
  public Usuario validarCredenciais(String email, String senha) throws IllegalArgumentException {
    if (email == null || email.trim().isEmpty()) {
      throw new IllegalArgumentException("Email não pode ser nulo ou vazio");
    }
    if (senha == null || senha.trim().isEmpty()) {
      throw new IllegalArgumentException("Senha não pode ser nula ou vazia");
    }

    Usuario usuario = repositorio.buscarUsuario(email.trim());

    if (usuario == null) {
      return null; // Usuário não encontrado
    }
    
    if (!usuario.getSenha().equals(senha)) {
      return null; // Senha incorreta
    }

    return usuario; // Credenciais válidas
>>>>>>> ef077eb4f53aab0c1a39c79dd604d9ea3815df9e
  }

  public boolean usuarioExiste(String email) {
    return repositorio.existeUsuario(email);
  }

<<<<<<< HEAD
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
=======
  public void cadastrarUsuario(String nome, String email, String senha) throws IllegalArgumentException {
    if (nome == null || nome.trim().isEmpty()) {
      throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
    }
    if (email == null || email.trim().isEmpty()) {
      throw new IllegalArgumentException("Email não pode ser nulo ou vazio");
    }
    if (senha == null || senha.trim().isEmpty()) {
      throw new IllegalArgumentException("Senha não pode ser nula ou vazia");
    }

    if (usuarioExiste(email.trim())) {
      throw new IllegalArgumentException("Usuário já existe com este email");
    }

    Usuario novoUsuario = new Usuario(nome.trim(), email.trim(), senha);
    repositorio.inserirUsuario(novoUsuario);
  }


  public Usuario buscarUsuarioPorId(String id) throws IllegalArgumentException {
    if (id == null || id.trim().isEmpty()) {
      throw new IllegalArgumentException("ID não pode ser nulo ou vazio");
    }
    
    return repositorio.buscarPorId(id.trim()).orElse(null);
  }

  public Usuario buscarUsuarioPorEmail(String email) throws IllegalArgumentException {
    if (email == null || email.trim().isEmpty()) {
      throw new IllegalArgumentException("Email não pode ser nulo ou vazio");
    }
    
    return repositorio.buscarUsuario(email.trim());
  }

  public java.util.List<Usuario> listarTodos() {
    return repositorio.listarTodos();
  }

  public void alterarSenha(Usuario usuario, String senhaAtual, String novaSenha) 
      throws IllegalArgumentException {
    if (usuario == null) {
      throw new IllegalArgumentException("Usuário não pode ser nulo");
    }
    if (senhaAtual == null || senhaAtual.trim().isEmpty()) {
      throw new IllegalArgumentException("Senha atual não pode ser nula ou vazia");
    }
    if (novaSenha == null || novaSenha.trim().isEmpty()) {
      throw new IllegalArgumentException("Nova senha não pode ser nula ou vazia");
    }
    
    if (!usuario.getSenha().equals(senhaAtual)) {
      throw new IllegalArgumentException("Senha atual incorreta");
    }

    usuario.setSenha(novaSenha);
    repositorio.atualizarUsuario(usuario);
  }

  public void excluirConta(Usuario usuario, String senhaConfirmacao) 
      throws IllegalArgumentException {
    if (usuario == null) {
      throw new IllegalArgumentException("Usuário não pode ser nulo");
    }
    if (senhaConfirmacao == null || senhaConfirmacao.trim().isEmpty()) {
      throw new IllegalArgumentException("Senha de confirmação é obrigatória");
    }
    
    if (!usuario.getSenha().equals(senhaConfirmacao)) {
      throw new IllegalArgumentException("Senha de confirmação incorreta");
    }

    repositorio.removerUsuario(usuario.getEmail());
>>>>>>> ef077eb4f53aab0c1a39c79dd604d9ea3815df9e
  }

}