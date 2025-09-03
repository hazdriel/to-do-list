package negocio.excecao.usuario;

public class UsuarioIDNaoEncontrado extends UsuarioException {

    public UsuarioIDNaoEncontrado(String id) {
        super("O usuário "+ id +" não foi encontrado. Por favor, verifique se o ID está correto e tente novamente.");
    }

}
