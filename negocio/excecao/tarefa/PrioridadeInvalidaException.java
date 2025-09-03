package negocio.excecao.tarefa;

import negocio.entidade.Prioridade;

public class PrioridadeInvalidaException extends TarefaException  {
    private static final long serialVersionUID = 1L;

    public PrioridadeInvalidaException(Prioridade prioridade) {
        super("A prioridade "+ prioridade +" é inválida. São aceitas somente baixa, media, alta e urgente. Por favor, preencha-a novamente.");
    }

}
