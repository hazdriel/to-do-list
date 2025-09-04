package negocio.excecao.tarefa;

public class DelegacaoHistoricoVazioException extends TarefaException  {
    private static final long serialVersionUID = 1L;

    public DelegacaoHistoricoVazioException() {
        super("o histórico ou registro de delegação não pode ser vazio ou nulo. Por favor, preencha-o.");
    }
}
