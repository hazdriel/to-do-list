package negocio.excecao.tarefa;

@SuppressWarnings("serial")
public class TituloVazioException extends TarefaException {

    public TituloVazioException() {
        super("O título não pode ser vazio. Por favor, preencha-o.");
    }
}
