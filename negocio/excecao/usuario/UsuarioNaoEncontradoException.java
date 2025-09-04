package negocio.excecao.usuario;

public class UsuarioNaoEncontradoException extends UsuarioException  {
    private static final long serialVersionUID = 1L;

    public UsuarioNaoEncontradoException(String email) {
        super("o usuário com e-mail "+ email +" não encontrado. Por favor, verifique se o e-mail está correto e tente novamente.");
    }

}
