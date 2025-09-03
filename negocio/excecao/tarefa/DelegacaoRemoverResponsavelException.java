package negocio.excecao.tarefa;

import negocio.entidade.Usuario;

public class DelegacaoRemoverResponsavelException extends TarefaException  {
    private static final long serialVersionUID = 1L;

    public DelegacaoRemoverResponsavelException(Usuario responsavel) {
        super("O "+ responsavel+" é o responsável original. Somente os tercerizados podem ser removidos. Por favor, verifique os responsaveis.");
    }

}
