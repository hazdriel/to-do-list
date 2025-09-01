package negocio.excecao.tarefa;

@SuppressWarnings("serial")
public class DelegacaoInvalidaException extends TarefaException {

    public DelegacaoInvalidaException() {
        super("A tarefa não pode ser delegada. Por favor, verifique o status e os delegados.");
    }

}
