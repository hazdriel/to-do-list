
package negocio.entidade;

import negocio.excecao.tarefa.*;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

/**
 * Tarefa que se repete periodicamente.
 * Implementa Recorrente para funcionalidades de recorrência e Delegavel para delegação.
 */

public class TarefaRecorrente extends TarefaAbstrata implements Recorrente, Delegavel {

<<<<<<< HEAD
  public TarefaRecorrente(String titulo, String descricao, LocalDateTime prazo, Prioridade prioridade,Categoria categoria,
      Usuario criador, Period periodicidade) throws IllegalArgumentException, CriadorVazioException, TituloVazioException, CategoriaVaziaException, PrazoInvalidoException {
    super(titulo, descricao, prazo, prioridade, categoria, criador);
    this.periodicidade = periodicidade;
    this.proximaExecucao = prazo;
  }
=======
    private Period periodicidade;
    private LocalDateTime proximaExecucao;
>>>>>>> ef077eb4f53aab0c1a39c79dd604d9ea3815df9e

    public TarefaRecorrente(String titulo, String descricao, LocalDateTime prazo, 
                           Prioridade prioridade, Categoria categoria,
                           Usuario criador, Period periodicidade) throws IllegalArgumentException {
        super(titulo, descricao, prazo, prioridade, categoria, criador);
        
        if (periodicidade == null) {
            throw new IllegalArgumentException("Periodicidade não pode ser nula");
        }
        
        this.periodicidade = periodicidade;
        this.proximaExecucao = prazo;
    }

    @Override
    public String getTipo() {
        return "Recorrente";
    }

    @Override
    public boolean podeSerDelegada() {
        return Delegavel.super.podeSerDelegada();
    }

    @Override
    public Usuario getResponsavelOriginal() {
        return getCriador(); // Criador é o responsável original
    }

<<<<<<< HEAD
  @Override
  public void concluir() throws ConclusaoInvalidaException {
    super.concluir();
    this.proximaExecucao = this.proximaExecucao.plus(periodicidade);
    this.setStatus(Status.PENDENTE);
  }
=======
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
    public Period getPeriodicidade() {
        return periodicidade;
    }

    @Override
    public LocalDateTime getProximaExecucao() {
        return proximaExecucao;
    }

    @Override
    public void setProximaExecucao(LocalDateTime proximaExecucao) throws IllegalArgumentException {
        if (proximaExecucao == null) {
            throw new IllegalArgumentException("Próxima execução não pode ser nula");
        }
        
        if (!podeSerAlterada()) {
            throw new IllegalStateException("Não é possível alterar próxima execução de tarefa finalizada");
        }
        
        this.proximaExecucao = proximaExecucao;
    }

    @Override
    public void setPeriodicidade(Period periodicidade) throws IllegalArgumentException, IllegalStateException {
        if (periodicidade == null) {
            throw new IllegalArgumentException("Periodicidade não pode ser nula");
        }
        
        if (!podeSerAlterada()) {
            throw new IllegalStateException("Não é possível alterar periodicidade de tarefa finalizada");
        }
        
        this.periodicidade = periodicidade;
    }

    @Override
    public void concluir() {
        super.concluir();
        Recorrente.super.reagendarProximaExecucao();
    }
>>>>>>> ef077eb4f53aab0c1a39c79dd604d9ea3815df9e
}
