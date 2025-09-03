package negocio.excecao.tarefa;

public class CancelamentoInvalidoException extends TarefaException  {
    private static final long serialVersionUID = 1L;

    public CancelamentoInvalidoException(String titulo){
        super("A tarefa "+ titulo +" está concluída, não pode ser cancelada. Por favor, verifique o status.");
    }

}
