package negocio.entidade;

import java.time.LocalDateTime;
import java.util.UUID;

import negocio.excecao.tarefa.*;

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
                  Prioridade prioridade, Categoria categoria, Usuario criador) throws TituloVazioException, CriadorVazioException, CategoriaVaziaException, PrazoInvalidoException {
        validarParametrosObrigatorios(titulo, criador, categoria);
        this.id = UUID.randomUUID().toString();
        this.titulo = titulo.trim();
        this.descricao = descricao != null ? descricao.trim() : "";
        this.prazo = prazo;
        this.prioridade = prioridade != null ? prioridade : Prioridade.MEDIA;
        this.categoria = categoria;
        this.criador = criador;
        this.descricao = descricao != null ? descricao.trim() : "";
        this.status = Status.PENDENTE;
        this.responsavel = criador;
        this.dataCriacao = LocalDateTime.now();
        this.dataUltimaModificacao = LocalDateTime.now();
    }
    
    private void touch() {
        this.dataUltimaModificacao = LocalDateTime.now();
    }
    
    public abstract String getTipo();
    public abstract boolean podeSerDelegada();

    public void concluir() throws ConclusaoInvalidaException {
        if (this.status == Status.CANCELADA) {
            throw new ConclusaoInvalidaException(titulo);
        }
        this.status = Status.CONCLUIDA;
        if (this.dataFim == null) {
            this.dataFim = LocalDateTime.now();
        }
        touch();
    }
    
    public void cancelar() throws CancelamentoInvalidoException {
        if (this.status == Status.CONCLUIDA) {
            throw new CancelamentoInvalidoException(titulo);
        }
        this.status = Status.CANCELADA;
        touch();
    }
    
    public void iniciar() throws IniciacaoInvalidaException {
        if (this.status != Status.PENDENTE) {
            throw new IniciacaoInvalidaException(titulo);
        }
        this.status = Status.EM_PROGRESSO;
        if (this.dataInicio == null) {
            this.dataInicio = LocalDateTime.now();
        }
        touch();
    }
    
    public void delegar(Usuario novoResponsavel) throws DelegacaoResponsavelVazioException, DelegacaoResponsavelInvalidoException, DelegacaoStatusInvalidoException {
        if (novoResponsavel == null) {
            throw new DelegacaoResponsavelVazioException();
        }
        if (this.status == Status.CONCLUIDA || this.status == Status.CANCELADA) {
            throw new DelegacaoStatusInvalidoException(titulo, status);
        }
        if (!podeSerDelegada()) {
            throw new DelegacaoStatusInvalidoException(titulo, status);
        }
        if (novoResponsavel.equals(this.responsavel)) {
            throw new DelegacaoResponsavelInvalidoException(this.responsavel, novoResponsavel);
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

    private void validarParametrosObrigatorios(String titulo, Usuario criador, Categoria categoria) throws TituloVazioException, CriadorVazioException, CategoriaVaziaException {
        validarTitulo(titulo);
        validarCriador(criador);
        validarCategoria(categoria);
    }

    public static void validarTitulo(String titulo) throws TituloVazioException {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new TituloVazioException();
        }
    }

    public static void validarCriador(Usuario criador) throws CriadorVazioException {
        if (criador == null) throw new CriadorVazioException();
    }

    public static void validarCategoria(Categoria categoria) throws CategoriaVaziaException {
        if (categoria == null) throw new CategoriaVaziaException();
    }

    private void validarPrazo(LocalDateTime prazo) throws PrazoInvalidoException {
        if (prazo != null) {
            LocalDateTime base = dataInicio != null ? dataInicio : dataCriacao;
            if (prazo.isBefore(base)) throw new PrazoInvalidoException(base, prazo);
        }
    }
    
    protected void setTitulo(String titulo) throws TituloVazioException {
        validarTitulo(titulo);
        this.titulo = titulo.trim();
        touch();
    }
    
    protected void setDescricao(String descricao) {
        this.descricao = descricao != null ? descricao.trim() : "";
        touch();
    }
    
    protected void setPrazo(LocalDateTime prazo) throws PrazoInvalidoException {
        validarPrazo(prazo);
        this.prazo = prazo;
        touch();
    }
    
    protected void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade != null ? prioridade : Prioridade.MEDIA;
        touch();
    }

    protected void setCategoria(Categoria categoria) throws CategoriaVaziaException {
        validarCategoria(categoria);
        this.categoria = categoria;
        touch();
    }
    
    protected void setStatus(Status status) {
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