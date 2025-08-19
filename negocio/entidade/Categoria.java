package negocio.entidade;
import java.util.UUID;

import negocio.excecao.tarefa.CategoriaVaziaException;

public class Categoria {
  private final String id;
  private String nome;

  public Categoria(String nome) throws CategoriaVaziaException {
    if (nome == null || nome.trim().isEmpty()) {
      throw new CategoriaVaziaException();
    }
    this.id = UUID.randomUUID().toString(); 
    this.nome = nome.trim();
  }

  public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) throws CategoriaVaziaException {
      if (nome == null || nome.trim().isEmpty()) {
        throw new CategoriaVaziaException();
      }
      this.nome = nome.trim();
    }

    @Override
    public String toString() {
        return "Categoria{id='" + id + "', nome='" + nome + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Categoria)) return false;
        Categoria that = (Categoria) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}