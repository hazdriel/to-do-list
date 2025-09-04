package negocio.excecao.tarefa;

public class RecorrenteExecucaoException extends TarefaException  {
    private static final long serialVersionUID = 1L;

    public RecorrenteExecucaoException() {
        super("a próxima execução não pode ser vazia ou nula. Por favor, preencha-a.");;
    }

}
