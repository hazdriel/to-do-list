package negocio.entidade;

import java.time.LocalDate;
import java.time.LocalDateTime;

import negocio.excecao.tarefa.PrazoInvalidoException;

public class TarefaAntiga {
  private static int contador = 1;

  private String id;
  private String titulo;
  private String descricao;
  private Usuario criador;
  private boolean concluida;
  private Prioridade prioridade;
  private Categoria categoria;
  private LocalDate prazo;
  private LocalDate dataCriacao;

  public TarefaAntiga(String titulo, String descricao, LocalDate prazo, Prioridade prioridade, Categoria categoria, Usuario criador) throws PrazoInvalidoException {
    this.id = String.format("%02d", contador++);
    this.titulo = titulo;
    this.descricao = descricao;
    this.criador = criador;
    this.prioridade = prioridade != null ? prioridade : Prioridade.BAIXA;
    this.categoria = categoria;
    this.concluida = false;
    this.dataCriacao = LocalDate.from(LocalDateTime.now());

    if (prazo != null) {
      LocalDateTime dataAtual = LocalDateTime.now();
      LocalDateTime prazoDateTime = prazo.atStartOfDay();
      if (prazoDateTime.isBefore(dataAtual)) {
        throw new PrazoInvalidoException(dataAtual, prazoDateTime);
      }
      this.prazo = prazo;
    } else {
      this.prazo = null;
    }
  }

  public void concluirTarefa() {
    this.concluida = true;
  }

  public boolean isAtrasada() {
    if (prazo == null || concluida) {
      return false;
    }
    return LocalDate.now().isAfter(prazo);
  }

  public String getID() {
    return id;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public Usuario getCriador() {
    return criador;
  }

  public boolean isConcluida() {
    return concluida;
  }

  public Prioridade getPrioridade() {
    return prioridade;
  }

  public void setPrioridade(Prioridade prioridade) {
    this.prioridade = prioridade;
  }

  public Categoria getCategoria() {
    return categoria;
  }

  public void setCategoria(Categoria categoria) {
    this.categoria = categoria;
  }

  public LocalDate getPrazo() {
    return prazo;
  }

  public void setPrazo(LocalDate prazo) {
    this.prazo = prazo;
  }

  @Override
  public String toString() {
    return String.format(
            "ID: %s | Título: %s | Concluída: %s | Prioridade: %s | Prazo: %s",
            id,
            titulo,
            concluida ? "Sim" : "Não",
            prioridade,
            prazo != null ? prazo : "N/A"
    );
  }
}
