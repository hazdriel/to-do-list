package negocio.entidade;

import negocio.excecao.tarefa.CategoriaVaziaException;
import negocio.excecao.tarefa.CriadorVazioException;
import negocio.excecao.tarefa.PrazoInvalidoException;
import negocio.excecao.tarefa.TituloVazioException;

import java.time.LocalDateTime;

public class TarefaDelegavel extends Tarefa {

  private final Usuario responsavelOriginal;
  private Usuario responsavelAtual;
  private boolean jaDelegada;

  public TarefaDelegavel(String titulo, String descricao, LocalDateTime prazo, Prioridade prioridade, Categoria categoria, Usuario criador, Usuario responsavel)
          throws IllegalArgumentException, CriadorVazioException, TituloVazioException, CategoriaVaziaException, PrazoInvalidoException {
    super(titulo, descricao, prazo, prioridade, categoria, criador);
    this.responsavelOriginal = responsavel;
    this.responsavelAtual = responsavel;
    this.jaDelegada = false;
  }

  @Override
  public String getTipo() {
    return "Delegável";
  }

  @Override
  public boolean podeSerDelegada() {
    return !jaDelegada && getStatus() == Status.PENDENTE;
  }

  public Usuario getResponsavelAtual() {
    return responsavelAtual;
  }

  public Usuario getResponsavelOriginal() {
    return responsavelOriginal;
  }

  public void delegarPara(Usuario novoResponsavel) {
    if (!podeSerDelegada()) {
      throw new IllegalStateException("A tarefa não pode ser delegada.");
    }
    if (novoResponsavel.equals(responsavelAtual)) {
      throw new IllegalArgumentException("Novo responsável deve ser diferente do atual.");
    }
    this.responsavelAtual = novoResponsavel;
    this.jaDelegada = true;
  }
}
