package negocio.excecao.tarefa;

public class TarefaIDNaoPertenceException extends TarefaException  {
    private static final long serialVersionUID = 1L;

    public TarefaIDNaoPertenceException(String id) {
        super("a tarefa "+ id +" não pertence ao usuário. Por favor, verifique o ID e tente novamente.");
    }

}
