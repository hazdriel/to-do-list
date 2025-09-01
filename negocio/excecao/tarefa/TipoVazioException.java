package negocio.excecao.tarefa;

@SuppressWarnings("serial")
public class TipoVazioException extends TarefaException {

    public TipoVazioException() {
        super("O tipo de tarefa não pode ser vazio ou nulo. Por favor, preencha-o.");
    }

}
