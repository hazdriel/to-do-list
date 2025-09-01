package negocio.excecao.tarefa;

@SuppressWarnings("serial")
public class TemporizadaTempoException extends TarefaException {

    public TemporizadaTempoException() {
        super("O tempo gasto deve ser um valor não nulo e positivo. Por favor, preencha-o novamente.");
    }

}
