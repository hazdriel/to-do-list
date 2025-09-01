package negocio.excecao.tarefa;

@SuppressWarnings("serial")
public class TarefaVaziaException extends TarefaException {

    public TarefaVaziaException() {
        super("A tarefa não pode ser vazia ou nula. Por favor, preencha-a.");
    }
}
