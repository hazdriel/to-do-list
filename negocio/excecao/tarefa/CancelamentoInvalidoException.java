package negocio.excecao.tarefa;

@SuppressWarnings("serial")
public class CancelamentoInvalidoException extends TarefaException {

    public CancelamentoInvalidoException(String titulo){
        super("A tarefa "+ titulo +" não pode ser cancelada. Tarefas concluídas não podem ser canceladas. Por favor, verifique o status.");
    }

}
