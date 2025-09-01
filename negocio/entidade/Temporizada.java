package negocio.entidade;

import java.time.LocalDateTime;
import java.time.Duration;

// Interface que define capacidades de controle de tempo para tarefas
// Permite que tarefas tenham prazo, estimativa e controle de tempo gasto
public interface Temporizada {
    
    LocalDateTime getPrazoFinal();
    
    void setPrazoFinal(LocalDateTime prazoFinal);
    
    Duration getEstimativa();
    
    void setEstimativa(Duration estimativa);
    
    Duration getTempoGasto();
    
    void setTempoGasto(Duration tempoGasto);
    
    default void registrarTrabalho(Duration tempo) {
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
