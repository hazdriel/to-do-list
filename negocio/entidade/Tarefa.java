package negocio.entidade;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Classe abstrata que define a estrutura base de uma tarefa.
 * Contém apenas os atributos e comportamentos essenciais comuns a todas as tarefas.
 */

public abstract class Tarefa {
    private final String id;
    private String titulo;
    private String descricao;
    private LocalDateTime prazo;
    private Prioridade prioridade;
    private Categoria categoria;
    private Status status;
    private Usuario responsavel;
    private final Usuario criador;
    private final LocalDateTime dataCriacao;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private LocalDateTime dataUltimaModificacao;
    
    public Tarefa(String titulo, String descricao, LocalDateTime prazo, 
                  Prioridade prioridade, Categoria categoria, Usuario criador) throws IllegalArgumentException {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("Título não pode ser vazio");
        }
        if (criador == null) {
            throw new IllegalArgumentException("Criador não pode ser nulo");
        }
        
        this.id = UUID.randomUUID().toString();
        this.titulo = titulo.trim();
        this.descricao = descricao != null ? descricao.trim() : "";
        this.prazo = prazo;
        this.prioridade = prioridade != null ? prioridade : Prioridade.MEDIA;
        this.categoria = categoria;
        this.status = Status.PENDENTE;
        this.criador = criador;
        this.responsavel = criador;
        this.dataCriacao = LocalDateTime.now();
        this.dataUltimaModificacao = LocalDateTime.now();
    }
    
    private void touch() {
        this.dataUltimaModificacao = LocalDateTime.now();
    }
    
    public abstract String getTipo();
    public abstract boolean podeSerDelegada();

    public void concluir() throws IllegalStateException {
        if (this.status == Status.CANCELADA) {
            throw new IllegalStateException("Não é possível concluir uma tarefa cancelada");
        }
        this.status = Status.CONCLUIDA;
        if (this.dataFim == null) {
            this.dataFim = LocalDateTime.now();
        }
        touch();
    }
    
    public void cancelar() throws IllegalStateException {
        if (this.status == Status.CONCLUIDA) {
            throw new IllegalStateException("Não é possível cancelar uma tarefa já concluída");
        }
        this.status = Status.CANCELADA;
        touch();
    }
    
    public void iniciar() throws IllegalStateException {
        if (this.status != Status.PENDENTE) {
            throw new IllegalStateException("Apenas tarefas pendentes podem ser iniciadas");
        }
        this.status = Status.EM_PROGRESSO;
        if (this.dataInicio == null) {
            this.dataInicio = LocalDateTime.now();
        }
        touch();
    }
    
    public void delegar(Usuario novoResponsavel) throws IllegalArgumentException, UnsupportedOperationException {
        if (novoResponsavel == null) {
            throw new IllegalArgumentException("Responsável não pode ser nulo");
        }
        if (this.status == Status.CONCLUIDA || this.status == Status.CANCELADA) {
            throw new IllegalStateException("Não é possível delegar uma tarefa finalizada");
        }
        if (!podeSerDelegada()) {
            throw new UnsupportedOperationException("Este tipo de tarefa não pode ser delegada");
        }
        if (novoResponsavel.equals(this.responsavel)) {
            throw new IllegalArgumentException("Novo responsável é o mesmo do atual");
        }
        this.responsavel = novoResponsavel;
        touch();
    }
    
    public boolean estaAtrasada() {
        return prazo != null && 
               prazo.isBefore(LocalDateTime.now()) && 
               status != Status.CONCLUIDA && 
               status != Status.CANCELADA;
    }
    
    public boolean concluiuEmAtraso() {
        return prazo != null && dataFim != null && dataFim.isAfter(prazo);
    }
    
    public boolean estaConcluida() {
        return status == Status.CONCLUIDA;
    }
    
    public boolean estaCancelada() {
        return status == Status.CANCELADA;
    }
    
    public boolean estaEmProgresso() {
        return status == Status.EM_PROGRESSO;
    }
    
    public boolean estaPendente() {
        return status == Status.PENDENTE;
    }
    
    protected void setTitulo(String titulo) throws IllegalArgumentException {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("Título não pode ser vazio");
        }
        this.titulo = titulo.trim();
        touch();
    }
    
    protected void setDescricao(String descricao) throws IllegalArgumentException {
        this.descricao = descricao != null ? descricao.trim() : "";
        touch();
    }
    
    protected void setPrazo(LocalDateTime prazo) throws IllegalArgumentException {
        this.prazo = prazo;
        touch();
    }
    
    protected void setPrioridade(Prioridade prioridade) throws IllegalArgumentException {
        this.prioridade = prioridade != null ? prioridade : Prioridade.MEDIA;
        touch();
    }

    protected void setCategoria(Categoria categoria) throws IllegalArgumentException {
        this.categoria = categoria;
        touch();
    }
    
    protected void setStatus(Status status) throws IllegalArgumentException {
        this.status = status;
        touch();
    }
    
    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }
    public LocalDateTime getPrazo() { return prazo; }
    public Prioridade getPrioridade() { return prioridade; }
    public Categoria getCategoria() { return categoria; }
    public Status getStatus() { return status; }
    public Usuario getResponsavel() { return responsavel; }
    public Usuario getCriador() { return criador; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public LocalDateTime getDataUltimaModificacao() { return dataUltimaModificacao; }
    public LocalDateTime getDataInicio() { return dataInicio; }
    public LocalDateTime getDataFim() { return dataFim; }
    
    @Override
    public String toString() {
        return String.format("%s: %s (%s - %s)", 
                           getTipo(), titulo, status, prioridade);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Tarefa tarefa = (Tarefa) obj;
        return id.equals(tarefa.id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}