package negocio.excecao.tarefa;

@SuppressWarnings("serial")
public class TarefaIDVazioException extends TarefaException {

    public TarefaIDVazioException() {
        super("O ID não pode ser vazio ou nulo. Por favor, preencha-o.");
    }

}
