package negocio.entidade;

import negocio.excecao.tarefa.*;

import java.time.LocalDateTime;
import java.time.Duration;

// Interface que define capacidades de controle de tempo para tarefas
// Permite que tarefas tenham prazo, estimativa e controle de tempo gasto
public interface Temporizada {
    
    LocalDateTime getPrazoFinal();
    
    void setPrazoFinal(LocalDateTime prazoFinal) throws PrazoVazioException, PrazoInvalidoException, AtualizarTarefaException;
    
    Duration getEstimativa();
    
    void setEstimativa(Duration estimativa) throws TemporizadaEstimativaException, AtualizarTarefaException;
    
    Duration getTempoGasto();
    
    void setTempoGasto(Duration tempoGasto) throws TemporizadaTempoException, AtualizarTarefaException, TemporizadaEstimativaException;
    
    default void registrarTrabalho(Duration tempo) throws TemporizadaEstimativaException, TemporizadaTempoException, AtualizarTarefaException, TemporizadaDuracaoException {
        Duration tempoAtual = getTempoGasto();
        setTempoGasto(tempoAtual.plus(tempo));
    }
    
    default boolean estourouPrazo() {
        return LocalDateTime.now().isAfter(getPrazoFinal());
    }
    
    default boolean ultrapassouEstimativa() {
        return getTempoGasto().compareTo(getEstimativa()) > 0;
    }
}
