package negocio.excecao.tarefa;

public class TarefaIDNaoEncontradaException extends TarefaException  {
    private static final long serialVersionUID = 1L;

    public TarefaIDNaoEncontradaException(String id){
        super("a tarefa "+ id +" não foi encontrada. Por favor, verifique se o ID está correto e tente novamente.");
    }

}
