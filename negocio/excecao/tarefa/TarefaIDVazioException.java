package negocio.excecao.tarefa;

public class TarefaIDVazioException extends TarefaException  {
    private static final long serialVersionUID = 1L;

    public TarefaIDVazioException() {
        super("O ID não pode ser vazio ou nulo. Por favor, preencha-o.");
    }

}
