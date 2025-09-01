package negocio.excecao.tarefa;

@SuppressWarnings("serial")
public class RecorrentePeriodicidadeException extends TarefaException {

    public RecorrentePeriodicidadeException() {
        super("A periodicidade não pode ser vazia ou nula. Por favor, preencha-a.");
    }

}
