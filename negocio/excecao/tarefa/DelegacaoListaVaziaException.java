package negocio.excecao.tarefa;

@SuppressWarnings("serial")
public class DelegacaoListaVaziaException extends TarefaException {

    public DelegacaoListaVaziaException() {
        super("A lista de responsáveis não pode ser vazia ou nula. Por favor, preencha-a.");
    }

}
