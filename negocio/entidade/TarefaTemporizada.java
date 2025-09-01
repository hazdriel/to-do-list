package negocio.entidade;

import negocio.excecao.tarefa.CategoriaVaziaException;
import negocio.excecao.tarefa.CriadorVazioException;
import negocio.excecao.tarefa.PrazoInvalidoException;
import negocio.excecao.tarefa.TituloVazioException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Tarefa com controle de tempo, prazo final e estimativa.
 * Implementa Temporizada para funcionalidades de tempo e Delegavel para delegação.
 */
public class TarefaTemporizada extends TarefaAbstrata implements Temporizada, Delegavel {

    private LocalDateTime prazoFinal;
    private Duration estimativa; 
    private Duration tempoGasto;

<<<<<<< HEAD
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
=======
    public TarefaTemporizada(String titulo, String descricao, LocalDateTime prazo, Prioridade prioridade, Categoria categoria,
        Usuario criador, LocalDateTime prazoFinal, Duration estimativa) throws IllegalArgumentException {
        super(titulo, descricao, prazo, prioridade, categoria, criador);
        
        if (prazoFinal == null) {
            throw new IllegalArgumentException("Prazo final não pode ser nulo");
        }
        
        if (estimativa == null || estimativa.isNegative()) {
            throw new IllegalArgumentException("Estimativa deve ser positiva e não nula");
        }
        
        if (prazoFinal.isBefore(prazo)) {
            throw new IllegalArgumentException("Prazo final não pode ser anterior ao prazo inicial");
        }
        
        this.prazoFinal = prazoFinal;
        this.estimativa = estimativa;
        this.tempoGasto = Duration.ZERO;
>>>>>>> ef077eb4f53aab0c1a39c79dd604d9ea3815df9e
    }

    @Override
    public String getTipo() {
        return "Temporizada";
    }

    @Override
    public boolean podeSerDelegada() {
        return Delegavel.super.podeSerDelegada();
    }

    @Override
    public Usuario getResponsavelOriginal() {
        return getCriador(); // Criador é o responsável original
    }

    @Override
    public List<Usuario> getResponsaveis() {
        List<Usuario> responsaveis = new ArrayList<>();
        responsaveis.add(getCriador());
        return responsaveis;
    }

    @Override
    public List<RegistroDelegacao> getHistoricoDelegacoes() {
        return new ArrayList<>();
    }

    @Override
    public LocalDateTime getPrazoFinal() {
        return prazoFinal;
    }

    @Override
    public Duration getEstimativa() {
        return estimativa;
    }

    @Override
    public Duration getTempoGasto() {
        return tempoGasto;
    }

    @Override
    public void setTempoGasto(Duration tempoGasto) throws IllegalArgumentException, IllegalStateException {
        if (tempoGasto == null) {
            throw new IllegalArgumentException("Tempo gasto não pode ser nulo");
        }
        
        if (tempoGasto.isNegative()) {
            throw new IllegalArgumentException("Tempo gasto não pode ser negativo");
        }
        
        if (!podeSerAlterada()) {
            throw new IllegalStateException("Não é possível alterar tempo gasto de tarefa finalizada");
        }
        
        this.tempoGasto = tempoGasto;
    }

    @Override
    public void setEstimativa(Duration estimativa) throws IllegalArgumentException, IllegalStateException {
        if (estimativa == null || estimativa.isNegative()) {
            throw new IllegalArgumentException("Estimativa deve ser positiva e não nula");
        }
        
        if (!podeSerAlterada()) {
            throw new IllegalStateException("Não é possível alterar estimativa de tarefa finalizada");
        }
        
        this.estimativa = estimativa;
    }

    @Override
    public void setPrazoFinal(LocalDateTime prazoFinal) throws IllegalArgumentException, IllegalStateException {
        if (prazoFinal == null) {
            throw new IllegalArgumentException("Prazo final não pode ser nulo");
        }
        
        if (!podeSerAlterada()) {
            throw new IllegalStateException("Não é possível alterar prazo final de tarefa finalizada");
        }
        
        if (prazoFinal.isBefore(getPrazo())) {
            throw new IllegalArgumentException("Prazo final não pode ser anterior ao prazo inicial");
        }
        
        this.prazoFinal = prazoFinal;
    }

    public void registrarTrabalho(Duration duracao) throws IllegalArgumentException, IllegalStateException {
        if (duracao == null || duracao.isNegative()) {
            throw new IllegalArgumentException("Duração deve ser positiva e não nula");
        }
        
        if (!podeSerAlterada()) {
            throw new IllegalStateException("Não é possível registrar trabalho em tarefa finalizada");
        }
        
        Temporizada.super.registrarTrabalho(duracao);
    }

    public boolean estourouPrazo() {
        return Temporizada.super.estourouPrazo();
    }

    public boolean ultrapassouEstimativa() {
        return Temporizada.super.ultrapassouEstimativa();
    }
}
