package negocio.excecao.tarefa;

import negocio.entidade.Prioridade;

@SuppressWarnings("serial")
public class PrioridadeInvalidaException extends TarefaException {

    public PrioridadeInvalidaException(Prioridade prioridade) {
        super("A prioridade "+ prioridade +" é inválida. São aceitas somente baixa, media, alta e urgente. Por favor, preencha-a novamente.");
    }

}
