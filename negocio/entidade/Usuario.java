package negocio.entidade;

import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

import negocio.excecao.usuario.*;

public class Usuario {
  private String id;
  private String nome;
  private String email;
  private String senha;

  private static final int MIN_NOME = 2;
  private static final int MAX_NOME = 100;
  private static final int MIN_SENHA = 6;
  private static final int MAX_SENHA = 50;

  private static final Pattern EMAIL_PATTERN = 
    Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

  public Usuario(String nome, String email, String senha)
          throws EmailVazioException, SenhaTamanhoInvalidoException, NomeApenasLetrasException, NomeTamanhoInvalidoException,
          NomeVazioException, EmailFormatoInvalidoException {
    validarParametrosObrigatorios(nome, email, senha);
    this.id = UUID.randomUUID().toString();
    this.nome = nome.trim();
    this.email = email.trim().toLowerCase();
    this.senha = senha;
  }

  private void validarParametrosObrigatorios(String nome, String email, String senha)
          throws NomeTamanhoInvalidoException, NomeVazioException, EmailVazioException, EmailFormatoInvalidoException,
          SenhaTamanhoInvalidoException, NomeApenasLetrasException {
    validarNome(nome);
    validarEmail(email);
    validarSenha(senha);
  }

  public static void validarNome(String nome) throws NomeVazioException, NomeTamanhoInvalidoException, NomeApenasLetrasException {
    if (nome == null || nome.trim().isEmpty()) {
      throw new NomeVazioException();
    }
    if (nome.trim().length() < MIN_NOME || nome.trim().length() > MAX_NOME) {
      throw new NomeTamanhoInvalidoException(nome);
    }
    if (!nome.matches("^[A-Za-zÀ-ÿ\\s]+$")) {
      throw new NomeApenasLetrasException(nome);
    }
  }

  public static void validarEmail(String email) throws EmailVazioException, EmailFormatoInvalidoException {
    if (email == null || email.trim().isEmpty()) {
      throw new EmailVazioException();
    }
    if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
      throw new EmailFormatoInvalidoException(email);
    }
  }

  public static void validarSenha(String senha) throws SenhaTamanhoInvalidoException {
    if (senha == null || senha.trim().length() < MIN_SENHA || senha.length() > MAX_SENHA) {
      throw new SenhaTamanhoInvalidoException();
    }
  }
  
  public String getId() { 
    return id; 
  }
  
  public String getNome() { 
    return nome; 
  }
  
  public String getEmail() { 
    return email; 
  }
  
  public String getSenha() { 
    return senha; 
  }
  
  public void setNome(String nome) throws NomeApenasLetrasException, NomeTamanhoInvalidoException, NomeVazioException {
    validarNome(nome);
    this.nome = nome.trim();
  }
  
  public void setEmail(String email) throws EmailVazioException, EmailFormatoInvalidoException {
    validarEmail(email);
    this.email = email.trim().toLowerCase();
  }
  
  public void setSenha(String senha) throws SenhaTamanhoInvalidoException {
    validarSenha(senha);
    this.senha = senha;
  }
  
  public boolean verificarSenha(String senhaFornecida) {
    return this.senha != null && this.senha.equals(senhaFornecida);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Usuario usuario = (Usuario) obj;
    return Objects.equals(email, usuario.email);
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(email);
  }
}
