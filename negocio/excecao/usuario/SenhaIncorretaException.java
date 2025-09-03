package negocio.excecao.usuario;

public class SenhaIncorretaException extends UsuarioException  {
    private static final long serialVersionUID = 1L;

    public SenhaIncorretaException(String email) {
        super("A senha fornecida não corresponde à conta "+ email +". Por favor, verifique se a senha está correta e tente novamente.");
    }

}
