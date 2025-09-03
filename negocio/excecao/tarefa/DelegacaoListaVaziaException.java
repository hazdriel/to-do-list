package negocio.excecao.tarefa;

public class DelegacaoListaVaziaException extends TarefaException  {
    private static final long serialVersionUID = 1L;

    public DelegacaoListaVaziaException() {
        super("A lista de responsáveis não pode ser vazia ou nula. Por favor, preencha-a.");
    }

}
