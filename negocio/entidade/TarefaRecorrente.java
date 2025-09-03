
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

    private final Usuario responsavelOriginal;
    private final List<Usuario> responsaveis;
    private final List<RegistroDelegacao> historicoDelegacoes;
    private Period periodicidade;
    private LocalDateTime proximaExecucao;

    public TarefaRecorrente(String titulo, String descricao, LocalDateTime prazo, 
                           Prioridade prioridade, Categoria categoria,
                           Usuario criador, Usuario responsavel, Period periodicidade) throws IllegalArgumentException, CriadorVazioException, TituloVazioException, RecorrentePeriodicidadeException, DelegacaoResponsavelVazioException {
        super(titulo, descricao, prazo, prioridade, categoria, criador);
        
        if (responsavel == null) {
            throw new DelegacaoResponsavelVazioException();
        }
        
        if (periodicidade == null) {
            throw new RecorrentePeriodicidadeException();
        }
        
        this.responsavelOriginal = responsavel;
        this.responsaveis = new ArrayList<>();
        this.historicoDelegacoes = new ArrayList<>();
        
        this.responsaveis.add(criador);
        if (!responsavel.equals(criador)) {
            this.responsaveis.add(responsavel);
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
        return responsavelOriginal;
    }

    @Override
    public List<Usuario> getResponsaveis() {
        return new ArrayList<>(responsaveis);
    }

    @Override
    public List<RegistroDelegacao> getHistoricoDelegacoes() {
        return new ArrayList<>(historicoDelegacoes);
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
    public void setProximaExecucao(LocalDateTime proximaExecucao) throws RecorrenteExecucaoException, AtualizarTarefaException {
        if (proximaExecucao == null) {
            throw new RecorrenteExecucaoException();
        }
        
        if (!podeSerAlterada()) {
            throw new AtualizarTarefaException(getTitulo());
        }
        
        this.proximaExecucao = proximaExecucao;
    }

    @Override
    public void setPeriodicidade(Period periodicidade)
            throws IllegalArgumentException, IllegalStateException, RecorrentePeriodicidadeException, AtualizarTarefaException {
        if (periodicidade == null) {
            throw new RecorrentePeriodicidadeException();
        }
        
        if (!podeSerAlterada()) {
            throw new AtualizarTarefaException(getTitulo());
        }
        
        this.periodicidade = periodicidade;
    }

    @Override
    public void concluir() throws ConclusaoInvalidaException, RecorrenteExecucaoException, AtualizarTarefaException {
        super.concluir();
    }

}
