package negocio.excecao.tarefa;

@SuppressWarnings("serial")
public class TarefaTituloNaoEncontradaException extends TarefaException{

    public TarefaTituloNaoEncontradaException(String busca){
        super("A tarefa "+ busca +" não encontrada. Por favor, verifique se o título está correto e tente novamente.");
    }

}
