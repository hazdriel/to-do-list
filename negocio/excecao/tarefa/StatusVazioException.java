package negocio.excecao.tarefa;

public class StatusVazioException extends TarefaException  {
    private static final long serialVersionUID = 1L;

    public StatusVazioException() {
        super("o status não pode ser vazio ou nulo. Por favor, preencha-o.");
    }

}
