package negocio.excecao.tarefa;

public class DelegacaoInvalidaException extends TarefaException  {
    private static final long serialVersionUID = 1L;

    public DelegacaoInvalidaException() {
        super("a tarefa não pode ser delegada. Por favor, verifique o status e os delegados.");
    }

}
