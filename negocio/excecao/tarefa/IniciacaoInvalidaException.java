package negocio.excecao.tarefa;

public class IniciacaoInvalidaException extends TarefaException  {
    private static final long serialVersionUID = 1L;

    public IniciacaoInvalidaException(String titulo){
        super("a tarefa "+ titulo +" não pode ser iniciada. Somente tarefas pendentes podem ser iniciadas. Por favor, verifique o status.");
    }

}
