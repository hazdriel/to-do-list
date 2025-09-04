package negocio.excecao.tarefa;

import negocio.entidade.Usuario;

public class DelegacaoResponsavelInvalidoException extends TarefaException  {
    private static final long serialVersionUID = 1L;

    public DelegacaoResponsavelInvalidoException(Usuario responsavelAtual){
        super("o usuário "+ responsavelAtual +" já é responsável por está tarefa. Por favor, preencha um usuário delegavél distinto.");
    }

}
