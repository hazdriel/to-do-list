package negocio.excecao.tarefa;

public class PrazoVazioException extends TarefaException  {
    private static final long serialVersionUID = 1L;

    public PrazoVazioException() {
        super("o prazo não pode ser vazio. Por favor, preencha-o.");
    }

}
