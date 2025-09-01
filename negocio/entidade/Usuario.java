package negocio.entidade;

import negocio.excecao.usuario.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

public class Usuario implements Serializable {
  private static final long serialVersionUID = 1L;
  private String id;
  private String nome;
  private String email;
  private String senha;

  private static final Pattern EMAIL_PATTERN = 
    Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

  public Usuario(String nome, String email, String senha) throws IllegalArgumentException, EmailVazioException, SenhaTamanhoInvalidoException, NomeApenasLetrasException, NomeTamanhoInvalidoException, SenhaVaziaException, NomeVazioException, EmailFormatoInvalidoException {
    validarNome(nome);
    validarEmail(email);
    validarSenha(senha);
    this.id = GeradorId.gerarIdUsuario();
    this.nome = nome.trim();
    this.email = email.trim().toLowerCase();
    this.senha = senha;
  }
  
  private void validarNome(String nome) throws NomeVazioException, NomeApenasLetrasException, NomeTamanhoInvalidoException {
    if (nome == null || nome.trim().isEmpty()) {
      throw new NomeVazioException();
    }
    if (nome.trim().length() < 2) {
      throw new NomeTamanhoInvalidoException(nome);
    }
    if (nome.trim().length() > 100) {
      throw new NomeTamanhoInvalidoException(nome);
    }
    if (!nome.matches("^[A-Za-zÀ-ÖØ-öø-ÿ\\s]+$")) {
      throw new NomeApenasLetrasException(nome);
    }
  }
  
  private void validarEmail(String email) throws EmailVazioException, EmailFormatoInvalidoException {
    if (email == null || email.trim().isEmpty()) {
      throw new EmailVazioException();
    }
    if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
      throw new EmailFormatoInvalidoException(email);
    }
  }
  
  private void validarSenha(String senha) throws SenhaVaziaException, SenhaTamanhoInvalidoException {
    if (senha == null || senha.trim().isEmpty()) {
      throw new SenhaVaziaException();
    }
    if (senha.length() < 6 || senha.length() > 50) {
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
  
  public void setNome(String nome) throws IllegalArgumentException, NomeApenasLetrasException, NomeTamanhoInvalidoException, NomeVazioException {
    validarNome(nome);
    this.nome = nome.trim();
  }
  
  public void setEmail(String email) throws IllegalArgumentException, EmailVazioException, EmailFormatoInvalidoException {
    validarEmail(email);
    this.email = email.trim().toLowerCase();
  }
  
  public void setSenha(String senha) throws IllegalArgumentException, SenhaTamanhoInvalidoException, SenhaVaziaException {
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
