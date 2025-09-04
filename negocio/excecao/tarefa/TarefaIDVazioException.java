package negocio.excecao.tarefa;

public class TarefaIDVazioException extends TarefaException  {
    private static final long serialVersionUID = 1L;

    public TarefaIDVazioException() {
        super("o ID não pode ser vazio ou nulo. Por favor, preencha-o.");
    }

}
