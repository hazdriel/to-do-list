package negocio.entidade;

import java.io.Serializable;
import java.time.LocalDateTime;

<<<<<<< HEAD:negocio/entidade/Tarefa.java
import negocio.excecao.tarefa.*;

/**
 * Classe abstrata que define a estrutura base de uma tarefa.
 * Contém apenas os atributos e comportamentos essenciais comuns a todas as tarefas.
 */

public abstract class Tarefa {
=======
// Classe abstrata base para todas as tarefas do sistema
public abstract class TarefaAbstrata implements Serializable {
    private static final long serialVersionUID = 1L;
>>>>>>> ef077eb4f53aab0c1a39c79dd604d9ea3815df9e:negocio/entidade/TarefaAbstrata.java
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
    
<<<<<<< HEAD:negocio/entidade/Tarefa.java
    public Tarefa(String titulo, String descricao, LocalDateTime prazo, 
                  Prioridade prioridade, Categoria categoria, Usuario criador) throws TituloVazioException, CriadorVazioException, CategoriaVaziaException, PrazoInvalidoException {
        validarParametrosObrigatorios(titulo, criador, categoria);
        this.id = UUID.randomUUID().toString();
=======
    public TarefaAbstrata(String titulo, String descricao, LocalDateTime prazo, 
                  Prioridade prioridade, Categoria categoria, Usuario criador) throws IllegalArgumentException {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("Título não pode ser vazio");
        }
        if (criador == null) {
            throw new IllegalArgumentException("Criador não pode ser nulo");
        }
        
        this.id = GeradorId.gerarIdTarefa();
>>>>>>> ef077eb4f53aab0c1a39c79dd604d9ea3815df9e:negocio/entidade/TarefaAbstrata.java
        this.titulo = titulo.trim();
        this.descricao = descricao.trim();
        this.prazo = prazo;
        this.prioridade = prioridade != null ? prioridade : Prioridade.BAIXA;
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
    
<<<<<<< HEAD:negocio/entidade/Tarefa.java
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
    
=======
>>>>>>> ef077eb4f53aab0c1a39c79dd604d9ea3815df9e:negocio/entidade/TarefaAbstrata.java
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
<<<<<<< HEAD:negocio/entidade/Tarefa.java

    private void validarParametrosObrigatorios(String titulo, Usuario criador, Categoria categoria) throws TituloVazioException, CriadorVazioException, CategoriaVaziaException {
        validarTitulo(titulo);
        validarCriador(criador);
        validarCategoria(categoria);
    }

    public static void validarTitulo(String titulo) throws TituloVazioException {
=======
    
    public void setTitulo(String titulo) throws IllegalArgumentException {
>>>>>>> ef077eb4f53aab0c1a39c79dd604d9ea3815df9e:negocio/entidade/TarefaAbstrata.java
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
    
<<<<<<< HEAD:negocio/entidade/Tarefa.java
    protected void setDescricao(String descricao) {
        this.descricao = descricao != null ? descricao.trim() : "";
        touch();
    }
    
    protected void setPrazo(LocalDateTime prazo) throws PrazoInvalidoException {
        validarPrazo(prazo);
=======
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
        
>>>>>>> ef077eb4f53aab0c1a39c79dd604d9ea3815df9e:negocio/entidade/TarefaAbstrata.java
        this.prazo = prazo;
        touch();
    }
    
<<<<<<< HEAD:negocio/entidade/Tarefa.java
    protected void setPrioridade(Prioridade prioridade) {
=======
    public void setPrioridade(Prioridade prioridade) throws IllegalArgumentException, IllegalStateException {
        if (!podeSerAlterada()) {
            throw new IllegalStateException("Não é possível alterar prioridade de tarefa finalizada");
        }
        
>>>>>>> ef077eb4f53aab0c1a39c79dd604d9ea3815df9e:negocio/entidade/TarefaAbstrata.java
        this.prioridade = prioridade != null ? prioridade : Prioridade.MEDIA;
        touch();
    }

<<<<<<< HEAD:negocio/entidade/Tarefa.java
    protected void setCategoria(Categoria categoria) throws CategoriaVaziaException {
        validarCategoria(categoria);
=======
    public void setCategoria(Categoria categoria) throws IllegalArgumentException, IllegalStateException {
        if (!podeSerAlterada()) {
            throw new IllegalStateException("Não é possível alterar categoria de tarefa finalizada");
        }
        
        if (categoria == null) {
            throw new IllegalArgumentException("Categoria não pode ser nula");
        }
        
>>>>>>> ef077eb4f53aab0c1a39c79dd604d9ea3815df9e:negocio/entidade/TarefaAbstrata.java
        this.categoria = categoria;
        touch();
    }
    
<<<<<<< HEAD:negocio/entidade/Tarefa.java
    protected void setStatus(Status status) {
=======
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
        
>>>>>>> ef077eb4f53aab0c1a39c79dd604d9ea3815df9e:negocio/entidade/TarefaAbstrata.java
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