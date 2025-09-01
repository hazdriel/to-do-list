package negocio.entidade;

import negocio.excecao.categoria.CategoriaVaziaException;
import negocio.excecao.tarefa.*;

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
                  Prioridade prioridade, Categoria categoria, Usuario criador) throws TituloVazioException, CriadorVazioException {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new TituloVazioException();
        }
        if (criador == null) {
            throw new CriadorVazioException();
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

    public void concluir() throws ConclusaoInvalidaException, RecorrenteExecucaoException, AtualizarTarefaException {
        if (this.status == Status.CANCELADA) {
            throw new ConclusaoInvalidaException(this.titulo);
        }
        this.status = Status.CONCLUIDA;
        if (this.dataFim == null) {
            this.dataFim = LocalDateTime.now();
        }
        touch();
    }
    
    public void cancelar() throws CancelamentoInvalidoException {
        if (this.status == Status.CONCLUIDA) {
            throw new CancelamentoInvalidoException(this.titulo);
        }
        this.status = Status.CANCELADA;
        touch();
    }
    
    public void iniciar() throws IniciacaoInvalidaException {
        if (this.status != Status.PENDENTE) {
            throw new IniciacaoInvalidaException(this.titulo);
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
    
    public void setTitulo(String titulo) throws TituloVazioException {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new TituloVazioException();
        }
        this.titulo = titulo.trim();
        touch();
    }
    
    public void setDescricao(String descricao) throws DescricaoVaziaException{
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new DescricaoVaziaException();
        }
        this.descricao = descricao.trim();
        touch();
    }
    
    public void setPrazo(LocalDateTime prazo) throws AtualizarTarefaException, PrazoPassadoException, PrazoInvalidoException{
        if (!podeSerAlterada()) {
            throw new AtualizarTarefaException(getTitulo());
        }
        
        if (prazo != null) {
            if (prazo.isBefore(LocalDateTime.now())) {
                throw new PrazoPassadoException(prazo);
            }
            if (prazo.isBefore(dataCriacao)) {
                throw new PrazoInvalidoException(dataCriacao, prazo);
            }
        }
        
        this.prazo = prazo;
        touch();
    }
    
    public void setPrioridade(Prioridade prioridade) throws AtualizarTarefaException {
        if (!podeSerAlterada()) {
            throw new AtualizarTarefaException(getTitulo());
        }
        
        this.prioridade = prioridade != null ? prioridade : Prioridade.MEDIA;
        touch();
    }

    public void setCategoria(Categoria categoria) throws AtualizarTarefaException, CategoriaVaziaException {
        if (!podeSerAlterada()) {
            throw new AtualizarTarefaException(titulo);
        }
        
        if (categoria == null) {
            throw new CategoriaVaziaException();
        }
        
        this.categoria = categoria;
        touch();
    }
    
    public void setStatus(Status status) throws StatusVazioException, AtualizarTarefaException {
        if (status == null) {
            throw new StatusVazioException();
        }
        
        if (this.status == Status.CONCLUIDA && status != Status.CONCLUIDA) {
            throw new AtualizarTarefaException(titulo);
        }
        
        if (this.status == Status.CANCELADA && status != Status.CANCELADA) {
            throw new AtualizarTarefaException(titulo);
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