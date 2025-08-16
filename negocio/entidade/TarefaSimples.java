package negocio.entidade;

import java.time.LocalDateTime;

public class TarefaSimples extends Tarefa {

  public TarefaSimples(String titulo, String descricao, LocalDateTime prazo, Prioridade prioridade, Categoria categoria, Usuario criador)
      throws IllegalArgumentException {
    super(titulo, descricao, prazo, prioridade, categoria, criador);
  }

  @Override
  public String getTipo() {
    return "Simples";
  }

  @Override
  public boolean podeSerDelegada() {
    return false;
  }
}
