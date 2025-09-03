package negocio.excecao.tarefa;

public class TipoVazioException extends TarefaException  {
    private static final long serialVersionUID = 1L;

    public TipoVazioException() {
        super("O tipo de tarefa não pode ser vazio ou nulo. Por favor, preencha-o.");
    }

}
