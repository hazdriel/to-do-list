package negocio.excecao.tarefa;

@SuppressWarnings("serial")
public class PrazoVazioException extends TarefaException {

    public PrazoVazioException() {
        super("O prazo não pode ser vazio. Por favor, preencha-o.");
    }

}
