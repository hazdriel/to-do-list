package negocio.excecao.tarefa;

@SuppressWarnings("serial")
public class DelegacaoStatusInvalidoException extends TarefaException {

    public DelegacaoStatusInvalidoException(){
        super("A tarefa não pode ser delegada. Somente tarefas pendentes podem ser delegadas. Por favor, verifique o status.");
    }

}
