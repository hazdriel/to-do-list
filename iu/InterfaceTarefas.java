package iu;

import fachada.Gerenciador;
import negocio.entidade.*;
import java.time.Period;
import java.util.Scanner;
import java.time.Duration;

import java.time.LocalDateTime;
import java.util.List;

public final class InterfaceTarefas {
    private final Scanner scanner;
    private final Gerenciador gerenciador;

    public InterfaceTarefas(Scanner scanner, Gerenciador gerenciador) {
        this.scanner = scanner;
        this.gerenciador = gerenciador;
    }

    public void exibirMenuTarefas() {
    boolean executando = true;
    while (executando) {
        UtilitariosInterface.limparTela();
        System.out.println("--- 📝 GERENCIAR TAREFAS ---");
        System.out.println("1 -> Criar Tarefa");
        System.out.println("2 -> Atualizar Tarefa");
        System.out.println("3 -> Alterar Status de uma Tarefa");
        System.out.println("4 -> Delegar Tarefa");      
        System.out.println("5 -> Registrar Tempo de Trabalho"); 
        System.out.println("6 -> Remover Tarefa");          
        System.out.println("0 -> Voltar ao Menu Principal");
        System.out.print("Escolha uma opção: ");
        
        int opcao = UtilitariosInterface.lerInteiro(scanner);
        
        switch (opcao) {
            case 1 -> exibirMenuCriacaoTarefa();
            case 2 -> atualizarTarefa();
            case 3 -> gerenciarStatusTarefa();
            case 4 -> delegarTarefa();
            case 5 -> registrarTrabalho();     
            case 6 -> removerTarefa();         
            case 0 -> executando = false;
            default -> System.out.println("❌ Opção inválida.");
        }
        
        if (executando) {
            UtilitariosInterface.pressioneEnterParaContinuar(scanner);
        }
    }
    System.out.println("\nVoltando ao menu principal...");
}

private void exibirMenuCriacaoTarefa() {
    UtilitariosInterface.limparTela();
    System.out.println("--- TIPO DE TAREFA A SER CRIADA ---");
    System.out.println("1 -> Tarefa Simples");
    System.out.println("2 -> Tarefa Delegável");
    System.out.println("3 -> Tarefa Recorrente");
    System.out.println("4 -> Tarefa Temporizada");
    System.out.println("0 -> Cancelar");
    System.out.print("Escolha uma opção: ");

    int opcao = UtilitariosInterface.lerInteiro(scanner);

    switch (opcao) {
        case 1 -> criarTarefaSimples();
        case 2 -> criarTarefaDelegavel();
        case 3 -> criarTarefaRecorrente();
        case 4 -> criarTarefaTemporizada();
        case 0 -> System.out.println("Criação cancelada.");
        default -> System.out.println("❌ Opção inválida.");
    }
}
    private TarefaDadosComuns coletarDadosComunsTarefa() {
        String titulo = UtilitariosInterface.lerString(scanner, "Título: ");
        String descricao = UtilitariosInterface.lerString(scanner, "Descrição: ");
        Prioridade prioridade = UtilitariosInterface.lerPrioridade(scanner);
        LocalDateTime prazo = UtilitariosInterface.lerDataHora(scanner);
        Categoria categoria = selecionarCategoria();
        return new TarefaDadosComuns(titulo, descricao, prioridade, prazo, categoria);
    }

    private void criarTarefaSimples() {
        UtilitariosInterface.limparTela();
        System.out.println("--- CRIAR TAREFA SIMPLES ---");
        TarefaDadosComuns dados = coletarDadosComunsTarefa();
        try {
            gerenciador.criarTarefaSimples(dados.titulo, dados.descricao, dados.prioridade, dados.prazo, dados.categoria);
            System.out.println("\n✅ Tarefa simples criada com sucesso!");
        } catch (Exception e) {
            System.out.println("\n❌ Erro ao criar tarefa: " + e.getMessage());
        }
    }

    private void criarTarefaDelegavel() {
        UtilitariosInterface.limparTela();
        System.out.println("--- CRIAR TAREFA DELEGÁVEL ---");
        TarefaDadosComuns dados = coletarDadosComunsTarefa();
        
        System.out.println("\nSelecione o responsável pela tarefa:");
        Usuario responsavel = selecionarUsuario();
        if (responsavel == null) return;
        
        try {
            gerenciador.criarTarefaDelegavel(dados.titulo, dados.descricao, dados.prioridade, dados.prazo, dados.categoria, responsavel);
            System.out.printf("\n✅ Tarefa delegável criada com sucesso para %s!\n", responsavel.getNome());
        } catch (Exception e) {
            System.out.println("\n❌ Erro ao criar tarefa: " + e.getMessage());
        }
    }

    private void criarTarefaRecorrente() {
        UtilitariosInterface.limparTela();
        System.out.println("--- CRIAR TAREFA RECORRENTE ---");
        TarefaDadosComuns dados = coletarDadosComunsTarefa();
        Period periodicidade = UtilitariosInterface.lerPeriodicidade(scanner);
        try {
            gerenciador.criarTarefaRecorrente(dados.titulo, dados.descricao, dados.prioridade, dados.prazo, dados.categoria, periodicidade);
            System.out.println("\n✅ Tarefa recorrente criada com sucesso!");
        } catch (Exception e) {
            System.out.println("\n❌ Erro ao criar tarefa: " + e.getMessage());
        }
    }

    private void criarTarefaTemporizada() {
        UtilitariosInterface.limparTela();
        System.out.println("--- CRIAR TAREFA TEMPORIZADA ---");
        TarefaDadosComuns dados = coletarDadosComunsTarefa();
        System.out.println("Defina o prazo final da tarefa:");
        LocalDateTime prazoFinal = UtilitariosInterface.lerDataHora(scanner);
        Duration estimativa = UtilitariosInterface.lerDuracao(scanner);
        try {
            gerenciador.criarTarefaTemporizada(dados.titulo, dados.descricao, dados.prioridade, dados.prazo, dados.categoria, prazoFinal, estimativa);
            System.out.println("\n✅ Tarefa temporizada criada com sucesso!");
        } catch (Exception e) {
            System.out.println("\n❌ Erro ao criar tarefa: " + e.getMessage());
        }
    }

private void adicionarResponsavel(Delegavel tarefa) {
    System.out.println("\nSelecione o usuario para adicionar como responsável:");
    Usuario novoResponsavel = selecionarUsuario();
    if (novoResponsavel == null) {
        System.out.println("Nenhum usuario selecionado. Operação cancelada.");
        return;
    }
    try {
        tarefa.adicionarResponsavel(novoResponsavel);
        gerenciador.salvarTarefa((TarefaAbstrata) tarefa); 
        System.out.printf("\n✅ %s foi adicionado(a) como responsável.\n", novoResponsavel.getNome());
    } catch (Exception e) {
        System.out.println("\n❌ Erro ao adicionar responsável: " + e.getMessage());
    }
}

private void removerResponsavel(Delegavel tarefa) {
    List<Usuario> responsaveisAtuais = tarefa.getResponsaveis();
    if (responsaveisAtuais.isEmpty()) {
        System.out.println("\nNão há responsáveis para remover nesta tarefa.");
        return;
    }

    System.out.println("\nSelecione o responsável a ser removido:");
    for (int i = 0; i < responsaveisAtuais.size(); i++) {
        Usuario resp = responsaveisAtuais.get(i);
        System.out.printf("%d -> %s (%s)\n", (i + 1), resp.getNome(), resp.getEmail());
    }
    System.out.print("Escolha uma opção (0 para cancelar): ");
    int escolha = UtilitariosInterface.lerInteiro(scanner);

    if (escolha > 0 && escolha <= responsaveisAtuais.size()) {
        Usuario responsavelParaRemover = responsaveisAtuais.get(escolha - 1);
        try {
            tarefa.removerResponsavel(responsavelParaRemover);
            gerenciador.salvarTarefa((TarefaAbstrata) tarefa);
            System.out.printf("\n✅ %s foi removido(a) como responsável.\n", responsavelParaRemover.getNome());
        } catch (Exception e) {
            System.out.println("\n❌ Erro ao remover responsável: " + e.getMessage());
        }
    } else {
        System.out.println("\nOperação cancelada ou opção inválida.");
    }
}

    private void atualizarTarefa() {
        UtilitariosInterface.limparTela();
        System.out.println("--- ATUALIZAR TAREFA ---");
        
        TarefaAbstrata tarefa = buscarTarefaPorIdInterativo();
        if (tarefa == null) return;

        System.out.println("\n✏️ Digite os novos dados (pressione ENTER para manter o valor atual).");
        
        String novoTitulo = UtilitariosInterface.lerString(scanner, "Novo título (" + tarefa.getTitulo() + "): ");
        if (novoTitulo.isEmpty()) novoTitulo = tarefa.getTitulo();
        
        String novaDescricao = UtilitariosInterface.lerString(scanner, "Nova descrição (" + tarefa.getDescricao() + "): ");
        if (novaDescricao.isEmpty()) novaDescricao = tarefa.getDescricao();
        
        System.out.println("Nova prioridade (" + tarefa.getPrioridade() + "):");
        Prioridade novaPrioridade = UtilitariosInterface.lerPrioridade(scanner);
        
        System.out.println("Novo prazo (" + UtilitariosInterface.formatarDataHora(tarefa.getPrazo()) + "):");
        LocalDateTime novoPrazo = UtilitariosInterface.lerDataHora(scanner);

        System.out.println("Nova categoria (" + tarefa.getCategoria().getNome() + "):");
        Categoria novaCategoria = selecionarCategoria();
        
        try {
            boolean sucesso = gerenciador.atualizarTarefa(tarefa.getId(), novoTitulo, novaDescricao, novaPrioridade, novoPrazo, novaCategoria);
            if (sucesso) {
                System.out.println("\n✅ Tarefa atualizada com sucesso!");
            } else {
                System.out.println("\n❌ Não foi possível atualizar a tarefa.");
            }
        } catch (Exception e) {
            System.out.println("\n❌ Erro ao atualizar tarefa: " + e.getMessage());
        }
    }
    
    private void removerTarefa() {
        UtilitariosInterface.limparTela();
        System.out.println("--- 🗑️ REMOVER TAREFA ---");
        
        TarefaAbstrata tarefa = buscarTarefaPorIdInterativo();
        if (tarefa == null) return;
        
        System.out.println("\nVocê selecionou a tarefa:");
        UtilitariosInterface.exibirTarefaResumo(tarefa);
        
        String confirmacao = UtilitariosInterface.lerString(scanner, "\n⚠️ Tem certeza? Digite 'SIM' para confirmar a remoção: ");
        
        if (confirmacao.equalsIgnoreCase("SIM")) {
            try {
                if (gerenciador.removerTarefa(tarefa.getId())) {
                    System.out.println("\n✅ Tarefa removida com sucesso!");
                } else {
                    System.out.println("\n❌ Não foi possível remover a tarefa.");
                }
            } catch (Exception e) {
                System.out.println("\n❌ Erro ao remover tarefa: " + e.getMessage());
            }
        } else {
            System.out.println("\nOperação cancelada.");
        }
    }

    private void gerenciarStatusTarefa() {
        UtilitariosInterface.limparTela();
        System.out.println("--- ALTERAR STATUS DA TAREFA ---");
        
        TarefaAbstrata tarefa = buscarTarefaPorIdInterativo();
        if (tarefa == null) return;
        
        System.out.println("\n1 -> Iniciar Tarefa");
        System.out.println("2 -> Concluir Tarefa");
        System.out.println("3 -> Cancelar Tarefa");
        System.out.println("0 -> Voltar");
        System.out.print("Escolha uma ação: ");
        int opcao = UtilitariosInterface.lerInteiro(scanner);
        
        try {
            boolean sucesso = false;
            String acao = "";
            switch (opcao) {
                case 1 -> { sucesso = gerenciador.iniciarTarefa(tarefa.getId()); acao = "iniciada"; }
                case 2 -> { sucesso = gerenciador.concluirTarefa(tarefa.getId()); acao = "concluída"; }
                case 3 -> { sucesso = gerenciador.cancelarTarefa(tarefa.getId()); acao = "cancelada"; }
                case 0 -> { System.out.println("\nOperação cancelada."); return; }
                default -> { System.out.println("❌ Opção inválida."); return; }
            }

            if(sucesso) {
                System.out.printf("\n✅ Tarefa %s com sucesso!\n", acao);
            } else {
                System.out.printf("\n❌ Não foi possível alterar o status da tarefa para '%s'.\n", acao.toUpperCase());
            }

        } catch (Exception e) {
            System.out.println("\n❌ Erro ao alterar o status: " + e.getMessage());
        }
    }
    
    private void delegarTarefa() {
        UtilitariosInterface.limparTela();
        System.out.println("--- 🔀 DELEGAR TAREFA ---");

        TarefaAbstrata tarefa = buscarTarefaPorIdInterativo();
        if (tarefa == null) return;
        
        if (!(tarefa instanceof Delegavel tarefaDelegavel)) {
            System.out.println("❌ Erro: A tarefa '" + tarefa.getTitulo() + "' não é do tipo delegável.");
            return;
        }

        System.out.println("\nResponsáveis Atuais: " + tarefaDelegavel.getResponsaveis().stream().map(Usuario::getNome).toList());
        System.out.println("1 -> Adicionar Responsável");
        System.out.println("2 -> Remover Responsável");
        System.out.println("0 -> Cancelar");
        System.out.print("Escolha uma ação: ");
        int opcao = UtilitariosInterface.lerInteiro(scanner);

        switch (opcao) {
            case 1 -> adicionarResponsavel(tarefaDelegavel);
            case 2 -> removerResponsavel(tarefaDelegavel);
            case 0 -> System.out.println("Delegação cancelada.");
            default -> System.out.println("❌ Opção inválida.");
        }
    }

    private void registrarTrabalho() {
        UtilitariosInterface.limparTela();
        System.out.println("--- ⏱️ REGISTRAR TEMPO DE TRABALHO ---");

        TarefaAbstrata tarefa = buscarTarefaPorIdInterativo();
        if (tarefa == null) return;

        System.out.println("Digite a duração do trabalho realizado:");
        Duration duracao = UtilitariosInterface.lerDuracao(scanner);

        try {
            if (gerenciador.registrarTrabalho(tarefa.getId(), duracao)) {
                System.out.println("\n✅ Tempo de trabalho registrado com sucesso!");
            } else {
                System.out.println("\n❌ Não foi possível registrar o tempo de trabalho.");
            }
        } catch (Exception e) {
            System.out.println("\n❌ Erro ao registrar trabalho: " + e.getMessage());
        }
    }
    private TarefaAbstrata buscarTarefaPorIdInterativo() {
        String id = UtilitariosInterface.lerString(scanner, "Digite o ID da tarefa: ");
        if (id.isEmpty()) {
            System.out.println("❌ O ID não pode ser vazio.");
            return null;
        }
        TarefaAbstrata tarefa = gerenciador.buscarTarefa(id);
        if (tarefa == null) {
            System.out.println("❌ Tarefa com ID '" + id + "' não encontrada.");
        }
        return tarefa;
    }
    
    private Categoria selecionarCategoria() {
        System.out.println("\nSelecione uma Categoria:");
        List<Categoria> categorias = gerenciador.listarCategorias();
        if (categorias.isEmpty()) {
            System.out.println("Nenhuma categoria encontrada. Crie uma primeiro.");
            return null;
        }
        for (int i = 0; i < categorias.size(); i++) {
            System.out.printf("%d -> %s\n", (i + 1), categorias.get(i).getNome());
        }
        System.out.print("Escolha uma opção: ");
        int escolha = UtilitariosInterface.lerInteiro(scanner);
        if (escolha > 0 && escolha <= categorias.size()) {
            return categorias.get(escolha - 1);
        }
        System.out.println("❌ Opção inválida. Usando a primeira categoria da lista.");
        return categorias.get(0);
    }
    
    private Usuario selecionarUsuario() {
        List<Usuario> usuarios = gerenciador.listarUsuarios();
        for (int i = 0; i < usuarios.size(); i++) {
            System.out.printf("%d -> %s (%s)\n", (i + 1), usuarios.get(i).getNome(), usuarios.get(i).getEmail());
        }
        System.out.print("Escolha um usuario: ");
        int escolha = UtilitariosInterface.lerInteiro(scanner);
        if (escolha > 0 && escolha <= usuarios.size()) {
            return usuarios.get(escolha - 1);
        }
        System.out.println("❌ Opção inválida. Operação cancelada.");
        return null;
    }
    
    private record TarefaDadosComuns(String titulo, String descricao, Prioridade prioridade, LocalDateTime prazo, Categoria categoria) {}
}