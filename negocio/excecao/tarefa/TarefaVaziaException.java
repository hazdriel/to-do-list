package negocio.excecao.tarefa;

public class TarefaVaziaException extends TarefaException  {
    private static final long serialVersionUID = 1L;

    public TarefaVaziaException() {
        super("A tarefa não pode ser vazia ou nula. Por favor, preencha-a.");
    }
}
