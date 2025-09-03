package negocio.excecao.tarefa;

public class DelegacaoMotivoException extends TarefaException  {
    private static final long serialVersionUID = 1L;

    public DelegacaoMotivoException() {
        super("O motivo de delegação não pode ser vazio ou nulo. Por favor, preencha-o.");
    }
}
