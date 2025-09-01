package negocio.excecao.tarefa;

public class TemporizadaEstimativaException extends TarefaException {

    public TemporizadaEstimativaException() {
        super("A estimativa deve ser um valor não nulo e positivo. Por favor, preencha-a novamente.");
    }

}
