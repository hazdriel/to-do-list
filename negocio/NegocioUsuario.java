package negocio;

import dados.RepositorioUsuarios;
import negocio.entidade.Usuario;

/**
 * Classe responsável pela lógica de negócio relacionada aos usuários.
 * Gerencia CRUD de usuários, validação de credenciais e operações de conta.
 */
public class NegocioUsuario {
  private final RepositorioUsuarios repositorio;

  public NegocioUsuario(RepositorioUsuarios repositorio) throws IllegalArgumentException {
    if (repositorio == null) {
      throw new IllegalArgumentException("Repositório não pode ser nulo");
    }
    
    this.repositorio = repositorio;
  }

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
  }

  public boolean usuarioExiste(String email) {
    return repositorio.existeUsuario(email);
  }

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
  }
}