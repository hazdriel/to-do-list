package negocio.excecao.tarefa;

@SuppressWarnings("serial")
public class StatusVazioException extends TarefaException {

    public StatusVazioException() {
        super("O status não pode ser vazio ou nulo. Por favor, preencha-o.");
    }

}
