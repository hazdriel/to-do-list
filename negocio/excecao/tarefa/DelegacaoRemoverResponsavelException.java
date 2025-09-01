package negocio.excecao.tarefa;

import negocio.entidade.Usuario;

@SuppressWarnings("serial")
public class DelegacaoRemoverResponsavelException extends TarefaException {

    public DelegacaoRemoverResponsavelException(Usuario responsavel) {
        super("O "+ responsavel+" é o responsável original. Somente os tercerizados podem ser removidos. Por favor, verifique os responsaveis.");
    }

}
