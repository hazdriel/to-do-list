package negocio.excecao.tarefa;

public class TemporizadaDuracaoException extends TarefaException  {
    private static final long serialVersionUID = 1L;

    public TemporizadaDuracaoException() {
        super("A duração deve ser um valor não nulo e positivo. Por favor, preencha-a novamente.");
    }

}
