package negocio.excecao.tarefa;

public class DescricaoVaziaException extends TarefaException  {
    private static final long serialVersionUID = 1L;

    public DescricaoVaziaException() {
        super("A descrição não pode ser vazia. Por favor, preencha-a.");
    }

}
