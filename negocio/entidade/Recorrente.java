package negocio.entidade;

import negocio.excecao.tarefa.AtualizarTarefaException;
import negocio.excecao.tarefa.RecorrenteExecucaoException;
import negocio.excecao.tarefa.RecorrentePeriodicidadeException;

import java.time.LocalDateTime;
import java.time.Period;

// Interface que define capacidades de recorrência para tarefas
// Permite que tarefas sejam executadas repetidamente
public interface Recorrente {
    
    Period getPeriodicidade();

    void setPeriodicidade(Period periodicidade) throws RecorrentePeriodicidadeException, AtualizarTarefaException;
    
    LocalDateTime getProximaExecucao();
    
    void setProximaExecucao(LocalDateTime proximaExecucao) throws RecorrenteExecucaoException, AtualizarTarefaException;

    default void reagendarProximaExecucao() throws RecorrenteExecucaoException, AtualizarTarefaException {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime proxima = agora.plus(getPeriodicidade());
        setProximaExecucao(proxima);
    }
}
