package negocio.excecao.sessao;

@SuppressWarnings("serial")
public class NegocioTarefaVazioException extends SessaoException {

    public NegocioTarefaVazioException() {
        super("NegocioTarefa não pode ser vazio ou nulo. Por favor, preencha-o.");
    }

}
