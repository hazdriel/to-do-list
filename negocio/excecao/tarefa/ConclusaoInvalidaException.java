package negocio.excecao.tarefa;

public class ConclusaoInvalidaException extends TarefaException  {
    private static final long serialVersionUID = 1L;

    public ConclusaoInvalidaException(String titulo) {
        super("A tarefa "+ titulo +" não pode ser concluída. Tarefas canceladas não podem ser concluídas. Por favor, verifique o status.");
    }

}
