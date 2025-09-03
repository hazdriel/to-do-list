package negocio.excecao.tarefa;

public class TemporizadaNaoEException extends TarefaException  {
    private static final long serialVersionUID = 1L;

    public TemporizadaNaoEException(String id) {
        super("A "+ id +" não pode ser registrada. Somente tarefas temporizadas podem registrar trabalho. Por favor, verifique o tipo.");
    }

}
