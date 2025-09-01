package negocio.excecao.tarefa;

@SuppressWarnings("serial")
public class DelegacaoResponsavelVazioException extends TarefaException {

    public DelegacaoResponsavelVazioException(){
        super("O resposável não pode ser vazio. Por favor, preencha-o.");
    }

}
