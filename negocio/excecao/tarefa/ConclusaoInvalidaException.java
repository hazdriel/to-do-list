package negocio.excecao.tarefa;

@SuppressWarnings("serial")
public class ConclusaoInvalidaException extends TarefaException {

    public ConclusaoInvalidaException(String titulo) {
        super("A tarefa "+ titulo +" não pode ser concluída. Tarefas canceladas não podem ser concluídas. Por favor, verifique o status.");
    }

}
