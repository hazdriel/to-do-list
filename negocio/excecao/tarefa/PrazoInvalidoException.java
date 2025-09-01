package negocio.excecao.tarefa;

import java.time.LocalDateTime;

@SuppressWarnings("serial")
public class PrazoInvalidoException extends TarefaException {

    public PrazoInvalidoException(LocalDateTime dataInicio, LocalDateTime dataFinal) {
        super("A data inicial "+ dataInicio +" é posterior à data final "+ dataFinal +". Por favor, insira um prazo válido.");
    }

}
