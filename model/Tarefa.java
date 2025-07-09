package model;

import java.time.LocalDate;

public class Tarefa {
  private static int contador = 1;
  private String id;
  private String titulo;
  private String descricao;
  private Usuario criador;
  private boolean concluida;
  private String prioridade;
  private LocalDate prazo;

  public Tarefa(String titulo, String descricao, Usuario criador) {
    this.id = String.format("%02d", contador++);
    this.titulo = titulo;
    this.descricao = descricao;
    this.criador = criador;
    this.prioridade = "Baixa";
    this.prazo = null;
    this.concluida = false;
  }

  public void isConcluida() {
    this.concluida = true;
  }

  public void exibirTarefa() {
    System.out.println("ID: " + id);
    System.out.println("Título: " + titulo);
    System.out.println("Descrição: " + descricao);
    System.out.println("Criador: " + (criador != null ? criador.getNome() : "N/A"));
    System.out.println("Concluída: " + (concluida ? "Sim" : "Não"));
    System.out.println("Prioridade: " + (prioridade != null ? prioridade : "N/A"));
    System.out.println("Prazo: " + (prazo != null ? prazo.toString() : "N/A"));
    System.out.println("Atrasada: " + (isAtrasada() ? "Sim" : "Não"));
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

  public boolean getConcluida() {
    return concluida;
  }

  public String getPrioridade() {
    return prioridade;
  }

  public void setPrioridade(String prioridade) {
    this.prioridade = prioridade;
  }

  public LocalDate getPrazo() {
    return prazo;
  }

  public void setPrazo(LocalDate prazo) {
    this.prazo = prazo;
  }

}