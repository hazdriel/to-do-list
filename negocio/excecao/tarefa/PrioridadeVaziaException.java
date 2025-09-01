package negocio.excecao.tarefa;

@SuppressWarnings("serial")
public class PrioridadeVaziaException extends TarefaException {

    public PrioridadeVaziaException() {
        super("A prioridade pode ser vazia ou nula. Por favor, preencha-a.");
    }

}
