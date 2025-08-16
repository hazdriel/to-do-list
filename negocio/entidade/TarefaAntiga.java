package negocio.entidade;

import java.time.LocalDate;

public class TarefaAntiga {
  private static int contador = 1;
  private String id;
  private String titulo;
  private String descricao;
  private Usuario criador;
  private boolean concluida;
  private Prioridade prioridade;
  private LocalDate prazo;
  private LocalDate dataCriacao = LocalDate.now();

  public TarefaAntiga(String titulo, String descricao, Usuario criador) {
    this.id = String.format("%02d", contador++);
    this.titulo = titulo;
    this.descricao = descricao;
    this.criador = criador;
    this.prioridade = Prioridade.BAIXA;
    this.prazo = null;
    this.concluida = false;
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

  public LocalDate getPrazo() {
    return prazo;
  }

  public void setPrazo(LocalDate prazo) {
    this.prazo = prazo;
  }

}