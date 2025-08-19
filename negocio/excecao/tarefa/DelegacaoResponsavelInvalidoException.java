package negocio.excecao.tarefa;

import negocio.entidade.Usuario;

@SuppressWarnings("serial")
public class DelegacaoResponsavelInvalidoException extends TarefaException {

    public DelegacaoResponsavelInvalidoException(Usuario responsavelAtual, Usuario responsavelNovo){
        super("O responsável atual "+ responsavelAtual +" e "+ responsavelAtual +" são iguais. Por favor, preencha um responsável delegavél distinto.");
    }

}
