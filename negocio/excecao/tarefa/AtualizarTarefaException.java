package negocio.excecao.tarefa;

public class AtualizarTarefaException extends TarefaException  {
    private static final long serialVersionUID = 1L;

    public AtualizarTarefaException(String titulo) {
        super("a "+ titulo +" não pode ser atualizada. Somente tarefas pendetes e em progresso podem ser alteradas. Por favor, verifique o status.");
    }

}
