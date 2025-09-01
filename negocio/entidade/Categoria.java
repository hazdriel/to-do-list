package negocio.entidade;

<<<<<<< HEAD
import negocio.excecao.tarefa.CategoriaVaziaException;

public class Categoria {
  private final String id;
=======
import java.io.Serializable;

public class Categoria implements Serializable {
  private static final long serialVersionUID = 1L;
>>>>>>> ef077eb4f53aab0c1a39c79dd604d9ea3815df9e
  private String nome;
  private boolean isPadrao;
  private Usuario criador;

  public Categoria(String nome) throws CategoriaVaziaException {
    if (nome == null || nome.trim().isEmpty()) {
      throw new CategoriaVaziaException();
    }
    this.nome = nome.trim();
    this.isPadrao = true;  // Por padrão, é categoria do sistema
    this.criador = null;   // Categoria do sistema não tem criador
  }
  
  public Categoria(String nome, Usuario criador) throws IllegalArgumentException{
    if (nome == null || nome.trim().isEmpty()) {
      throw new IllegalArgumentException("Nome da categoria não pode ser nulo ou vazio");
    }
    if (criador == null) {
      throw new IllegalArgumentException("Criador da categoria não pode ser nulo");
    }
    this.nome = nome.trim();
    this.isPadrao = false; 
    this.criador = criador;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) throws IllegalArgumentException{
    if (nome == null || nome.trim().isEmpty()) {
      throw new IllegalArgumentException("Nome da categoria não pode ser nulo ou vazio");
    }
    this.nome = nome.trim();
  }
  
  public boolean isPadrao() {
    return isPadrao;
  }
  
  public Usuario getCriador() {
    return criador;
  }
  
  public boolean podeSerRemovida() {
    return !isPadrao;
  }
  
  public boolean foiCriadaPor(Usuario usuario) {
    return criador != null && criador.equals(usuario);
  }
  
  public String getTipo() {
    return isPadrao ? "Sistema" : "Usuário";
  }

<<<<<<< HEAD
    public void setNome(String nome) throws CategoriaVaziaException {
      if (nome == null || nome.trim().isEmpty()) {
        throw new CategoriaVaziaException();
      }
      this.nome = nome.trim();
    }
=======
  @Override
  public String toString() {
    String tipo = isPadrao ? " (Sistema)" : " (Criada por " + (criador != null ? criador.getNome() : "Desconhecido") + ")";
    return nome + tipo;
  }
>>>>>>> ef077eb4f53aab0c1a39c79dd604d9ea3815df9e

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Categoria)) return false;
    Categoria that = (Categoria) o;
    return nome.equalsIgnoreCase(that.nome);
  }

  @Override
  public int hashCode() {
    return nome.toLowerCase().hashCode();
  }
}