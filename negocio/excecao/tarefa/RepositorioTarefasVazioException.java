package negocio.excecao.tarefa;

@SuppressWarnings("serial")
public class RepositorioTarefasVazioException extends TarefaException {

    public RepositorioTarefasVazioException() {
        super("O repositório de tarefas não pode ser vazio ou nulo. Por favor, preencha-o.");
    }

}
