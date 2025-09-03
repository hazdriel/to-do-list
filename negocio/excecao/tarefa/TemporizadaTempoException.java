package negocio.excecao.tarefa;

public class TemporizadaTempoException extends TarefaException  {
    private static final long serialVersionUID = 1L;

    public TemporizadaTempoException() {
        super("O tempo gasto deve ser um valor não nulo e positivo. Por favor, preencha-o novamente.");
    }

}
