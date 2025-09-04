package negocio.excecao.tarefa;

import negocio.entidade.Status;

public class StatusInvalidoException extends TarefaException  {
    private static final long serialVersionUID = 1L;

    public StatusInvalidoException(Status status) {
        super("o status "+status +" é inválido. São aceitos somente pendente, em progresso, concluida e cancelada. Por favor, preencha-o novamente.");
    }

}
