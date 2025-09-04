package negocio.excecao.tarefa;

public class RepositorioTarefasVazioException extends TarefaException  {
    private static final long serialVersionUID = 1L;

    public RepositorioTarefasVazioException() {
        super("o repositório de tarefas não pode ser vazio ou nulo. Por favor, preencha-o.");
    }

}
