package negocio.excecao.tarefa;

public class RecorrentePeriodicidadeException extends TarefaException  {
    private static final long serialVersionUID = 1L;

    public RecorrentePeriodicidadeException() {
        super("a periodicidade não pode ser vazia ou nula. Por favor, preencha-a.");
    }

}
