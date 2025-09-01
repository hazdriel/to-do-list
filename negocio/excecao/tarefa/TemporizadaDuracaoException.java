package negocio.excecao.tarefa;

@SuppressWarnings("serial")
public class TemporizadaDuracaoException extends TarefaException {

    public TemporizadaDuracaoException() {
        super("A duração deve ser um valor não nulo e positivo. Por favor, preencha-a novamente.");
    }

}
