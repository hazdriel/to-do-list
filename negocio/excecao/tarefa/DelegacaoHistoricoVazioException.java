package negocio.excecao.tarefa;

@SuppressWarnings("serial")
public class DelegacaoHistoricoVazioException extends TarefaException {

    public DelegacaoHistoricoVazioException() {
        super("O histórico ou registro de delegação não pode ser vazio ou nulo. Por favor, preencha-o.");
    }
}
