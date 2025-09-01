package negocio.excecao.tarefa;

@SuppressWarnings("serial")
public class TarefaIDNaoPertenceException extends TarefaException {

    public TarefaIDNaoPertenceException(String id) {
        super("A tarefa "+ id +" não pertence ao usuário. Por favor, verifique o ID e tente novamente.");
    }

}
