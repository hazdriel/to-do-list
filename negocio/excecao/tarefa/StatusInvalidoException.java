package negocio.excecao.tarefa;

import negocio.entidade.Status;

@SuppressWarnings("serial")
public class StatusInvalidoException extends TarefaException {

    public StatusInvalidoException(Status status) {
        super("O status "+status +" é inválido. São aceitos somente pendente, em progresso, concluida e cancelada. Por favor, preencha-o novamente.");
    }

}
