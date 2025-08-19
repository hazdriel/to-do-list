
package negocio.entidade;

import negocio.excecao.tarefa.*;

import java.time.LocalDateTime;
import java.time.Period;

public class TarefaRecorrente extends Tarefa {

  private final Period periodicidade; 
  private LocalDateTime proximaExecucao;

  public TarefaRecorrente(String titulo, String descricao, LocalDateTime prazo, Prioridade prioridade,Categoria categoria,
      Usuario criador, Period periodicidade) throws IllegalArgumentException, CriadorVazioException, TituloVazioException, CategoriaVaziaException, PrazoInvalidoException {
    super(titulo, descricao, prazo, prioridade, categoria, criador);
    this.periodicidade = periodicidade;
    this.proximaExecucao = prazo;
  }

  @Override
  public String getTipo() {
    return "Recorrente";
  }

  @Override
  public boolean podeSerDelegada() {
    return true; 
  }

  public Period getPeriodicidade() {
    return periodicidade;
  }

  public LocalDateTime getProximaExecucao() {
    return proximaExecucao;
  }

  @Override
  public void concluir() throws ConclusaoInvalidaException {
    super.concluir();
    this.proximaExecucao = this.proximaExecucao.plus(periodicidade);
    this.setStatus(Status.PENDENTE);
  }
}
