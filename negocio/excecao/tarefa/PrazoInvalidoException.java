package negocio.excecao.tarefa;

import java.time.LocalDateTime;

public class PrazoInvalidoException extends TarefaException  {
    private static final long serialVersionUID = 1L;

    public PrazoInvalidoException(LocalDateTime dataInicio, LocalDateTime dataFinal) {
        super("A data inicial "+ dataInicio +" é posterior à data final "+ dataFinal +". Por favor, insira um prazo válido.");
    }

}
