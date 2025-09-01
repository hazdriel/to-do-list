package negocio.excecao.tarefa;

@SuppressWarnings("serial")
public class DelegacaoMotivoException extends TarefaException {

    public DelegacaoMotivoException() {
        super("O motivo de delegação não pode ser vazio ou nulo. Por favor, preencha-o.");
    }
}
