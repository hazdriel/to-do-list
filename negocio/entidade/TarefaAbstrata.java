package negocio.entidade;

import java.io.Serializable;
import java.time.LocalDateTime;

// Classe abstrata base para todas as tarefas do sistema
public abstract class TarefaAbstrata implements Serializable {
    private static final long serialVersionUID = 1L;
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
    
    public TarefaAbstrata(String titulo, String descricao, LocalDateTime prazo, 
                  Prioridade prioridade, Categoria categoria, Usuario criador) throws IllegalArgumentException {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("Título não pode ser vazio");
        }
        if (criador == null) {
            throw new IllegalArgumentException("Criador não pode ser nulo");
        }
        
        this.id = GeradorId.gerarIdTarefa();
        this.titulo = titulo.trim();
        this.descricao = descricao.trim();
        this.prazo = prazo;
        this.prioridade = prioridade != null ? prioridade : Prioridade.BAIXA;
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
    
    public boolean estaAtrasada() {
        return prazo != null && 
               prazo.isBefore(LocalDateTime.now()) && 
               status != Status.CONCLUIDA && 
               status != Status.CANCELADA;
    }
    
    public boolean concluiuEmAtraso() {
        return prazo != null && dataFim != null && dataFim.isAfter(prazo);
    }
    
    public boolean estaFinalizada() {
        return status == Status.CONCLUIDA || status == Status.CANCELADA;
    }
    
    public boolean podeSerAlterada() {
        return !estaFinalizada();
    }
    
    public boolean estaAtiva() {
        return status == Status.PENDENTE || status == Status.EM_PROGRESSO;
    }
    
    public boolean temStatus(Status status) {
        return this.status == status;
    }
    
    public void setTitulo(String titulo) throws IllegalArgumentException {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("Título não pode ser vazio");
        }
        this.titulo = titulo.trim();
        touch();
    }
    
    public void setDescricao(String descricao) throws IllegalArgumentException {
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição não pode ser vazia");
        }
        this.descricao = descricao.trim();
        touch();
    }
    
    public void setPrazo(LocalDateTime prazo) throws IllegalArgumentException, IllegalStateException {
        if (!podeSerAlterada()) {
            throw new IllegalStateException("Não é possível alterar prazo de tarefa finalizada");
        }
        
        if (prazo != null) {
            if (prazo.isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Prazo não pode ser no passado");
            }
            if (prazo.isBefore(dataCriacao)) {
                throw new IllegalArgumentException("Prazo não pode ser anterior à data de criação");
            }
        }
        
        this.prazo = prazo;
        touch();
    }
    
    public void setPrioridade(Prioridade prioridade) throws IllegalArgumentException, IllegalStateException {
        if (!podeSerAlterada()) {
            throw new IllegalStateException("Não é possível alterar prioridade de tarefa finalizada");
        }
        
        this.prioridade = prioridade != null ? prioridade : Prioridade.MEDIA;
        touch();
    }

    public void setCategoria(Categoria categoria) throws IllegalArgumentException, IllegalStateException {
        if (!podeSerAlterada()) {
            throw new IllegalStateException("Não é possível alterar categoria de tarefa finalizada");
        }
        
        if (categoria == null) {
            throw new IllegalArgumentException("Categoria não pode ser nula");
        }
        
        this.categoria = categoria;
        touch();
    }
    
    public void setStatus(Status status) throws IllegalArgumentException, IllegalStateException {
        if (status == null) {
            throw new IllegalArgumentException("Status não pode ser nulo");
        }
        
        if (this.status == Status.CONCLUIDA && status != Status.CONCLUIDA) {
            throw new IllegalStateException("Não é possível alterar status de tarefa concluída");
        }
        
        if (this.status == Status.CANCELADA && status != Status.CANCELADA) {
            throw new IllegalStateException("Não é possível alterar status de tarefa cancelada");
        }
        
        if (status == Status.EM_PROGRESSO && dataInicio == null) {
            dataInicio = LocalDateTime.now();
        }
        
        if (status == Status.CONCLUIDA && dataFim == null) {
            dataFim = LocalDateTime.now();
        }
        
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
        TarefaAbstrata tarefa = (TarefaAbstrata) obj;
        return id.equals(tarefa.id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}