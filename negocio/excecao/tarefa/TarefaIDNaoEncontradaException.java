package negocio.excecao.tarefa;

@SuppressWarnings("serial")
public class TarefaIDNaoEncontradaException extends TarefaException{

    public TarefaIDNaoEncontradaException(String id){
        super("A tarefa "+ id +" não encontrada. Por favor, verifique se o ID está correto e tente novamente.");
    }

}
