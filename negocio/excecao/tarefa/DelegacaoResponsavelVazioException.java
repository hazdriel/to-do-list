package negocio.excecao.tarefa;

public class DelegacaoResponsavelVazioException extends TarefaException  {
    private static final long serialVersionUID = 1L;

    public DelegacaoResponsavelVazioException(){
        super("O resposável não pode ser vazio. Por favor, preencha-o.");
    }

}
