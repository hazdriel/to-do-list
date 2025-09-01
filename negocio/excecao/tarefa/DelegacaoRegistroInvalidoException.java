package negocio.excecao.tarefa;

@SuppressWarnings("serial")
public class DelegacaoRegistroInvalidoException extends TarefaException {

    public DelegacaoRegistroInvalidoException(){
        super("A tarefa não pode ser registrada. Somente responsáveis podem fazer o registro de delegação. Por favor, verifique os delegados.");
    }

}
