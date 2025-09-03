package negocio.excecao.tarefa;

public class DelegacaoStatusInvalidoException extends TarefaException  {
    private static final long serialVersionUID = 1L;

    public DelegacaoStatusInvalidoException(){
        super("A tarefa não pode ser delegada. Somente tarefas pendentes podem ser delegadas. Por favor, verifique o status.");
    }

}
