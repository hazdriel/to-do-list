package negocio.excecao.tarefa;

@SuppressWarnings("serial")
public class AtualizarTarefaException extends TarefaException {

    public AtualizarTarefaException(String titulo) {
        super("A "+ titulo +" não pode ser atualizada. Somente tarefas pendetes e em progresso podem ser alteradas. Por favor, verifique o status.");
    }

}
