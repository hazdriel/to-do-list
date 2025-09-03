package negocio.excecao.sessao;

public class NegocioTarefaVazioException extends SessaoException  {
    private static final long serialVersionUID = 1L;

    public NegocioTarefaVazioException() {
        super("NegocioTarefa não pode ser vazio ou nulo. Por favor, preencha-o.");
    }

}
