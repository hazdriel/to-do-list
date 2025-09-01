package negocio.excecao.tarefa;

@SuppressWarnings("serial")
public class TemporizadaNaoEException extends TarefaException {

    public TemporizadaNaoEException(String id) {
        super("A "+ id +" não pode ser registrada. Somente tarefas temporizadas podem registrar trabalho. Por favor, verifique o tipo.");
    }

}
