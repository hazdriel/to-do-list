package negocio.excecao.tarefa;

@SuppressWarnings("serial")
public class IniciacaoInvalidaException extends TarefaException {

    public IniciacaoInvalidaException(String titulo){
        super("A tarefa "+ titulo +" não pode ser iniciada. Somente tarefas pendentes podem ser iniciadas. Por favor, verifique o status.");
    }

}
