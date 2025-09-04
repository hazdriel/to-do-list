package negocio.excecao.tarefa;

public class PrioridadeVaziaException extends TarefaException  {
    private static final long serialVersionUID = 1L;

    public PrioridadeVaziaException() {
        super("a prioridade pode ser vazia ou nula. Por favor, preencha-a.");
    }

}
