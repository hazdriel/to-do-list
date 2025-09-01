package negocio.entidade;

import negocio.excecao.tarefa.CategoriaVaziaException;
import negocio.excecao.tarefa.CriadorVazioException;
import negocio.excecao.tarefa.PrazoInvalidoException;
import negocio.excecao.tarefa.TituloVazioException;

import java.time.LocalDateTime;

public class TarefaSimples extends TarefaAbstrata {

  public TarefaSimples(String titulo, String descricao, LocalDateTime prazo, Prioridade prioridade, Categoria categoria, Usuario criador)
          throws IllegalArgumentException, CriadorVazioException, TituloVazioException, CategoriaVaziaException, PrazoInvalidoException {
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
