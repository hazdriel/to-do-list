package negocio.excecao.tarefa;

@SuppressWarnings("serial")
public class RecorrenteExecucaoException extends TarefaException {

    public RecorrenteExecucaoException() {
        super("A próxima execução não pode ser vazia ou nula. Por favor, preencha-a.");;
    }

}
