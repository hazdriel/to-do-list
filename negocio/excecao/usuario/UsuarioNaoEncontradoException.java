package negocio.excecao.usuario;

@SuppressWarnings("serial")
public class UsuarioNaoEncontradoException extends UsuarioException {

    public UsuarioNaoEncontradoException(String email) {
        super("O usuário com e-mail "+ email +" não encontrado. Por favor, verifique se o e-mail está correto e tente novamente.");
    }

}
