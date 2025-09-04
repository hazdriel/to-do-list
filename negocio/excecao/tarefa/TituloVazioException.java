package negocio.excecao.tarefa;


public class TituloVazioException extends TarefaException  {

    private static final long serialVersionUID = 1L;

    public TituloVazioException() {
        super("o título não pode ser vazio. Por favor, preencha-o.");
    }

}
