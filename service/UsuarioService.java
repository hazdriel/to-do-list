package service;

import repository.UsuarioRepository;
import model.Usuario;

public class UsuarioService {
  private UsuarioRepository repositorio;
  private SessaoService sessao;

  public UsuarioService(UsuarioRepository repositorio, SessaoService sessao) {
    this.repositorio = repositorio;
    this.sessao = sessao;
  }

  public boolean autenticar(String email, String senha) {
    Usuario usuario = repositorio.buscarUsuario(email);

    if (usuario == null) {
      System.out.println("Usuário não encontrado.");
      return false;
    } else if (!usuario.getSenha().equals(senha)) {
      System.out.println("Senha incorreta!");
      return false;
    }

    sessao.login(usuario);
    System.out.println("Login realizado com sucesso. Bem-vindo, " + usuario.getNome() + "!");
    return true;
  }

  public boolean usuarioExiste(String email) {
    return repositorio.buscarUsuario(email) != null;
  }

  public void cadastrarUsuario(String nome, String email, String senha) {
    if (nome == null || nome.isBlank() || email == null || email.isBlank() || senha == null || senha.isBlank()) {
      System.out.println("Nome, e-mail e senha não podem ser vazios.");
      return;
    }

    if (usuarioExiste(email)) {
      System.out.println("Usuário já existe.");
      return;
    }

    Usuario novoUsuario = new Usuario(nome, email, senha);
    repositorio.inserirUsuario(novoUsuario);
    System.out.println("Usuário cadastrado com sucesso!");
  }
}