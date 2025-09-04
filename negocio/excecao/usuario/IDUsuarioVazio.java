package negocio.excecao.usuario;

public class IDUsuarioVazio extends UsuarioException {

    public IDUsuarioVazio() {
        super("o ID de usuário não pode ser vazio. Por favor, preencha-o.");
    }

}
