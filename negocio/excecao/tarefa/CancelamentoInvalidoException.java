package negocio.excecao.tarefa;

@SuppressWarnings("serial")
public class CancelamentoInvalidoException extends TarefaException {

    public CancelamentoInvalidoException(String titulo){
        super("A tarefa "+ titulo +" está concluída, não pode ser cancelada. Por favor, verifique o status.");
    }

}
