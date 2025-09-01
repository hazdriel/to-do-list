package negocio.excecao.usuario;

@SuppressWarnings("serial")
public class IDUsuarioVazio extends UsuarioException {

    public IDUsuarioVazio() {
        super("O ID de usuário não pode ser vazio. Por favor, preencha-o.");
    }

}
