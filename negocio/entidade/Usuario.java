package negocio.entidade;

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

  public Usuario(String nome, String email, String senha) throws IllegalArgumentException {
    validarParametrosObrigatorios(nome, email, senha);
    this.id = GeradorId.gerarIdUsuario();
    this.nome = nome.trim();
    this.email = email.trim().toLowerCase();
    this.senha = senha;
  }

  private void validarParametrosObrigatorios(String nome, String email, String senha) throws IllegalArgumentException {
    validarNome(nome);
    validarEmail(email);
    validarSenha(senha);
  }
  
  private void validarNome(String nome) throws IllegalArgumentException {
    if (nome == null || nome.trim().isEmpty()) {
      throw new IllegalArgumentException("Nome não pode ser vazio");
    }
    if (nome.trim().length() < 2) {
      throw new IllegalArgumentException("Nome deve ter pelo menos 2 caracteres");
    }
    if (nome.trim().length() > 100) {
      throw new IllegalArgumentException("Nome deve ter no máximo 100 caracteres");
    }
  }
  
  private void validarEmail(String email) throws IllegalArgumentException {
    if (email == null || email.trim().isEmpty()) {
      throw new IllegalArgumentException("Email não pode ser vazio");
    }
    if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
      throw new IllegalArgumentException("Email deve ter formato válido");
    }
  }
  
  private void validarSenha(String senha) throws IllegalArgumentException {
    if (senha == null || senha.length() < 6) {
      throw new IllegalArgumentException("Senha deve ter pelo menos 6 caracteres");
    }
    if (senha.length() > 50) {
      throw new IllegalArgumentException("Senha deve ter no máximo 50 caracteres");
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
  
  public void setNome(String nome) throws IllegalArgumentException {
    validarNome(nome);
    this.nome = nome.trim();
  }
  
  public void setEmail(String email) throws IllegalArgumentException {
    validarEmail(email);
    this.email = email.trim().toLowerCase();
  }
  
  public void setSenha(String senha) throws IllegalArgumentException {
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
