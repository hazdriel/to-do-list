package negocio.excecao.categoria;

@SuppressWarnings("serial")
public class CategoriaAtivaRemocaoException extends CategoriaException {

    public CategoriaAtivaRemocaoException(String categoria) {
        super("A "+categoria+" não pode ser removida. Somente categorias inativas ou não padrões podem ser removidas. " +
                "Por favor, verifique se há tarefas ativas e seu tipo.");
    }

}
