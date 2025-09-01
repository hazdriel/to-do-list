package negocio.excecao.tarefa;

@SuppressWarnings("serial")
public class DescricaoVaziaException extends TarefaException {

    public DescricaoVaziaException() {
        super("A descrição não pode ser vazia. Por favor, preencha-a.");
    }

}
