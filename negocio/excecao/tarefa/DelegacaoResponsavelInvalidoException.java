package negocio.excecao.tarefa;

import negocio.entidade.Usuario;

@SuppressWarnings("serial")
public class DelegacaoResponsavelInvalidoException extends TarefaException {

    public DelegacaoResponsavelInvalidoException(Usuario responsavelAtual){
        super("O usuário "+ responsavelAtual +" já é responsável por está tarefa. Por favor, preencha um usuário delegavél distinto.");
    }

}
