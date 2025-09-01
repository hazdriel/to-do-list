package negocio.entidade;

import java.time.LocalDateTime;
import java.time.Period;

// Interface que define capacidades de recorrência para tarefas
// Permite que tarefas sejam executadas repetidamente
public interface Recorrente {
    
    Period getPeriodicidade();

    void setPeriodicidade(Period periodicidade);
    
    LocalDateTime getProximaExecucao();
    
    void setProximaExecucao(LocalDateTime proximaExecucao);

    default void reagendarProximaExecucao() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime proxima = agora.plus(getPeriodicidade());
        setProximaExecucao(proxima);
    }
}
