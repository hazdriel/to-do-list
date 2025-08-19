package negocio.entidade;

import negocio.excecao.tarefa.CategoriaVaziaException;
import negocio.excecao.tarefa.CriadorVazioException;
import negocio.excecao.tarefa.PrazoInvalidoException;
import negocio.excecao.tarefa.TituloVazioException;

import java.time.Duration;
import java.time.LocalDateTime;

public class TarefaTemporizada extends Tarefa {

  private final LocalDateTime prazoFinal;
  private Duration estimativa; 
  private Duration tempoGasto; 

  public TarefaTemporizada(String titulo, String descricao, LocalDateTime prazo, Prioridade prioridade, Categoria categoria,
      Usuario criador, LocalDateTime prazoFinal, Duration estimativa) throws IllegalArgumentException, CriadorVazioException, TituloVazioException, CategoriaVaziaException, PrazoInvalidoException {
    super(titulo, descricao, prazo, prioridade, categoria, criador);
    this.prazoFinal = prazoFinal;
    this.estimativa = estimativa;
    this.tempoGasto = Duration.ZERO;
  }

  @Override
  public String getTipo() {
    return "Temporizada";
  }

  @Override
  public boolean podeSerDelegada() {
    return true;
  }

  public LocalDateTime getPrazoFinal() {
    return prazoFinal;
  }

  public Duration getEstimativa() {
    return estimativa;
  }

  public Duration getTempoGasto() {
    return tempoGasto;
  }

  public void registrarTrabalho(Duration duracao) {
    if (duracao.isNegative()) {
      throw new IllegalArgumentException("A duração deve ser positiva.");
    }
    this.tempoGasto = this.tempoGasto.plus(duracao);
  }

  public boolean estourouPrazo() {
    return LocalDateTime.now().isAfter(prazoFinal);
  }

  public boolean ultrapassouEstimativa() {
    return tempoGasto.compareTo(estimativa) > 0;
  }
}
