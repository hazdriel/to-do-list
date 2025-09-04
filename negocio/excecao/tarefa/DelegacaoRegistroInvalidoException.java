package negocio.excecao.tarefa;

public class DelegacaoRegistroInvalidoException extends TarefaException  {
    private static final long serialVersionUID = 1L;

    public DelegacaoRegistroInvalidoException(){
        super("a tarefa não pode ser registrada. Somente responsáveis podem fazer o registro de delegação. Por favor, verifique os delegados.");
    }

}
