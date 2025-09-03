package iu;

import fachada.Gerenciador;
import negocio.entidade.*;
import java.time.Period;
import java.util.Scanner;
import java.time.Duration;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Módulo da UI dedicado a todas as operações de gerenciamento de tarefas,
 * como criação, atualização, remoção e alteração de status.
 */
public final class InterfaceTarefas {
    private final Scanner scanner;
    private final Gerenciador gerenciador;

    public InterfaceTarefas(Scanner scanner, Gerenciador gerenciador) {
        this.scanner = scanner;
        this.gerenciador = gerenciador;
    }

    /**
     * Exibe o menu principal de gerenciamento de tarefas em um loop interativo.
     */
    public void exibirMenuTarefas() {
    boolean executando = true;
    while (executando) {
        
        System.out.println("--- 📝 GERENCIAR TAREFAS ---");
                System.out.println("1 -> Criar Tarefa");
        System.out.println("2 -> Atualizar Tarefa");
        System.out.println("3 -> Alterar Status de uma Tarefa");
        System.out.println("4 -> Delegar Tarefa");
        System.out.println("5 -> Remover Tarefa");
        System.out.println("6 -> Executar Tarefa Temporizada (Pomodoro)");
        System.out.println("0 -> Voltar ao Menu Principal");
        System.out.print("Escolha uma opção: ");
        
        int opcao = UtilitariosInterface.lerInteiro(scanner);
        
        switch (opcao) {
            case 1 -> exibirMenuCriacaoTarefa(); // Leva ao submenu de criação
            case 2 -> atualizarTarefa();
            case 3 -> gerenciarStatusTarefa();
            case 4 -> delegarTarefa();
            case 5 -> removerTarefa();
            case 6 -> executarTarefaTemporizada();
            case 0 -> executando = false;
            default -> System.out.println("❌ Opção inválida.");
        }
        
        if (executando) {
            UtilitariosInterface.pressioneEnterParaContinuar(scanner);
        }
    }
    System.out.println("\nVoltando ao menu principal...");
}

/**
 * Exibe um submenu para a criação de diferentes tipos de tarefas.
 */
private void exibirMenuCriacaoTarefa() {
    
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

    // =================================================================================
    // MÉTODOS DE CRIAÇÃO DE TAREFAS (Refatorados com Helper/Ajudantes)
    // =================================================================================

    /**
     * Método auxiliar que coleta os dados comuns a todos os tipos de tarefas.
     * Evita a repetição de código nos métodos de criação.
     * @return Um objeto {@code TarefaDadosComuns} contendo os dados lidos.
     */
    private TarefaDadosComuns coletarDadosComunsTarefa() {
        String titulo = UtilitariosInterface.lerString(scanner, "Título: ");
        String descricao = UtilitariosInterface.lerString(scanner, "Descrição: ");
        Prioridade prioridade = UtilitariosInterface.lerPrioridade(scanner);
        LocalDateTime prazo = UtilitariosInterface.lerDataHora(scanner);
        Categoria categoria = selecionarCategoria();
        return new TarefaDadosComuns(titulo, descricao, prioridade, prazo, categoria);
    }

    private void criarTarefaSimples() {
        
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
        
        System.out.println("--- CRIAR TAREFA DELEGÁVEL ---");
        TarefaDadosComuns dados = coletarDadosComunsTarefa();
        
        System.out.println("\nSelecione o responsável pela tarefa:");
        Usuario responsavel = selecionarUsuario();
        if (responsavel == null) return; // Operação cancelada
        
        try {
            gerenciador.criarTarefaDelegavel(dados.titulo, dados.descricao, dados.prioridade, dados.prazo, dados.categoria, responsavel);
            System.out.printf("\n✅ Tarefa delegável criada com sucesso para %s!\n", responsavel.getNome());
        } catch (Exception e) {
            System.out.println("\n❌ Erro ao criar tarefa: " + e.getMessage());
        }
    }

    private void criarTarefaRecorrente() {
        
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
        System.out.println("--- CRIAR TAREFA TEMPORIZADA (POMODORO) ---");
        
        // Dados básicos (sem prioridade - será BAIXA por padrão)
        String titulo = UtilitariosInterface.lerString(scanner, "Título: ");
        String descricao = UtilitariosInterface.lerString(scanner, "Descrição: ");
        System.out.println("Defina o prazo limite da tarefa:");
        LocalDateTime prazo = UtilitariosInterface.lerDataHora(scanner);
        Categoria categoria = selecionarCategoria();
        
        // Prioridade padrão para tarefas temporizadas
        Prioridade prioridade = Prioridade.BAIXA;
        
        // Configurações do Pomodoro
        System.out.println("\n🍅 CONFIGURAÇÕES DO POMODORO");
        
        // Duração da sessão
        Duration duracaoSessao = UtilitariosInterface.lerDuracaoMinutos(scanner, 
            "Duração da sessão em minutos (5-120, padrão: 25): ", 25, 5, 120);
        
        // Duração da pausa
        Duration duracaoPausa = UtilitariosInterface.lerDuracaoMinutos(scanner, 
            "Duração da pausa em minutos (1-60, padrão: 5): ", 5, 1, 60);
        
        // Total de sessões
        int totalSessoes = lerTotalSessoes();
        
        try {
            gerenciador.criarTarefaTemporizada(
                titulo, descricao, prioridade, 
                prazo, categoria, 
                duracaoSessao, duracaoPausa, totalSessoes
            );
            System.out.println("\n✅ Tarefa temporizada criada com sucesso!");
            System.out.println("🍅 Configuração: " + duracaoSessao.toMinutes() + "min sessão + " + 
                              duracaoPausa.toMinutes() + "min pausa × " + totalSessoes + " sessões");
            
            // Perguntar se quer iniciar imediatamente
            System.out.print("\n🚀 Deseja iniciar a primeira sessão agora? (s/N): ");
            String resposta = scanner.nextLine().trim().toLowerCase();
            
            if (resposta.equals("s") || resposta.equals("sim")) {
                // Buscar a tarefa recém-criada e iniciar execução
                List<TarefaTemporizada> tarefasTemporizadas = gerenciador.listarTarefasTemporizadas();
                if (!tarefasTemporizadas.isEmpty()) {
                    // Pegar a última tarefa criada (mais recente)
                    TarefaTemporizada tarefaCriada = tarefasTemporizadas.get(tarefasTemporizadas.size() - 1);
                    if (tarefaCriada.getTitulo().equals(titulo)) {
                        System.out.println("\n🍅 Iniciando primeira sessão...");
                        executarSessaoPomodoro(tarefaCriada);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("\n❌ Erro ao criar tarefa: " + e.getMessage());
        }
    }

    /**
     * Lê o total de sessões do usuário.
     */
    private int lerTotalSessoes() {
        while (true) {
            System.out.print("Total de sessões (1-20, padrão: 4): ");
            String entrada = scanner.nextLine().trim();
            
            if (entrada.isEmpty()) {
                return 4; // Padrão
            }
            
            try {
                int sessoes = Integer.parseInt(entrada);
                if (sessoes >= 1 && sessoes <= 20) {
                    return sessoes;
                } else {
                    System.out.println("❌ Total de sessões deve ser entre 1 e 20.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Digite um número válido.");
            }
        }
    }

/**
 * Método auxiliar para conduzir o fluxo de adição de um novo responsável a uma tarefa delegável.
 * @param tarefa A tarefa delegável que será modificada.
 */
private void adicionarResponsavel(Delegavel tarefa) {
    System.out.println("\nSelecione o utilizador para adicionar como responsável:");
    Usuario novoResponsavel = selecionarUsuario();
    if (novoResponsavel == null) {
        System.out.println("Nenhum utilizador selecionado. Operação cancelada.");
        return; // Retorna se a seleção foi cancelada
    }

    // Talvez O ideal seria chamar gerenciador.adicionarResponsavel(...).
    // vou manter a modificação direta + salvar aqui para simplicidade. !!!!!!!!!!!!!!!!!!!!!!!!
            try {
            tarefa.adicionarResponsavel(novoResponsavel);
            // Tarefa já é salva automaticamente pelo repositório
            System.out.printf("\n✅ %s foi adicionado(a) como responsável.\n", novoResponsavel.getNome());
        } catch (Exception e) {
            System.out.println("\n❌ Erro ao adicionar responsável: " + e.getMessage());
        }
}

/**
 * Método auxiliar para conduzir o fluxo de remoção de um responsável de uma tarefa delegável.
 * @param tarefa A tarefa delegável que será modificada.
 */
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
            // Tarefa já é salva automaticamente pelo repositório
            System.out.printf("\n✅ %s foi removido(a) como responsável.\n", responsavelParaRemover.getNome());
        } catch (Exception e) {
            System.out.println("\n❌ Erro ao remover responsável: " + e.getMessage());
        }
    } else {
        System.out.println("\nOperação cancelada ou opção inválida.");
    }
}

    // =================================================================================
    // MÉTODOS DE ATUALIZAÇÃO E REMOÇÃO
    // =================================================================================

    private void atualizarTarefa() {
        
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
            gerenciador.atualizarTarefa(tarefa.getId(), novoTitulo, novaDescricao, novaPrioridade, novoPrazo, novaCategoria);
            System.out.println("\n✅ Tarefa atualizada com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("\n❌ Erro de validação: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("\n❌ Operação não permitida: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n❌ Erro inesperado ao atualizar tarefa: " + e.getMessage());
        }
    }
    
    private void removerTarefa() {
        
        System.out.println("--- 🗑️ REMOVER TAREFA ---");
        
        TarefaAbstrata tarefa = buscarTarefaPorIdInterativo();
        if (tarefa == null) return;
        
        System.out.println("\nVocê selecionou a tarefa:");
        UtilitariosInterface.exibirTarefaResumo(tarefa);
        
        String confirmacao = UtilitariosInterface.lerString(scanner, "\n⚠️ Tem certeza? Digite 'SIM' para confirmar a remoção: ");
        
        if (confirmacao.equalsIgnoreCase("SIM")) {
            try {
                gerenciador.removerTarefa(tarefa.getId());
                System.out.println("\n✅ Tarefa removida com sucesso!");
            } catch (IllegalArgumentException e) {
                System.out.println("\n❌ Erro de validação: " + e.getMessage());
            } catch (IllegalStateException e) {
                System.out.println("\n❌ Operação não permitida: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("\n❌ Erro inesperado ao remover tarefa: " + e.getMessage());
            }
        } else {
            System.out.println("\nOperação cancelada.");
        }
    }

    // =================================================================================
    // SUBMENU DE STATUS E DELEGAÇÃO
    // =================================================================================

    private void gerenciarStatusTarefa() {
        
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
            String acao = "";
            switch (opcao) {
                case 1 -> { gerenciador.iniciarTarefa(tarefa.getId()); acao = "iniciada"; }
                case 2 -> { gerenciador.concluirTarefa(tarefa.getId()); acao = "concluída"; }
                case 3 -> { gerenciador.cancelarTarefa(tarefa.getId()); acao = "cancelada"; }
                case 0 -> { System.out.println("\nOperação cancelada."); return; }
                default -> { System.out.println("❌ Opção inválida."); return; }
            }

            System.out.printf("\n✅ Tarefa %s com sucesso!\n", acao);

        } catch (IllegalArgumentException e) {
            System.out.println("\n❌ Erro de validação: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("\n❌ Operação não permitida: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n❌ Erro inesperado ao alterar o status: " + e.getMessage());
        }
    }
    

    /**
     * Conduz o fluxo para delegar uma tarefa, seja adicionando, removendo ou trocando responsáveis.
     */
    private void delegarTarefa() {
        
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



    // =================================================================================
    // MÉTODOS AUXILIARES
    // =================================================================================
    
    /**
     * Pede ao utilizador um ID e busca a tarefa correspondente.
     * Centraliza a lógica de busca e tratamento de erro "não encontrado".
     * @return A {@code TarefaAbstrata} encontrada, ou {@code null} se não for encontrada ou a operação for cancelada.
     */
    private TarefaAbstrata buscarTarefaPorIdInterativo() {
        String id = UtilitariosInterface.lerString(scanner, "Digite o ID da tarefa: ");
        if (id.isEmpty()) {
            System.out.println("❌ O ID não pode ser vazio.");
            return null;
        }
        TarefaAbstrata tarefa = gerenciador.buscarTarefaPorId(id);
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
        System.out.println("❌ Opção inválida. A usar a primeira categoria da lista.");
        return categorias.get(0);
    }
    
    private Usuario selecionarUsuario() {
        List<Usuario> usuarios = gerenciador.listarUsuarios();
        for (int i = 0; i < usuarios.size(); i++) {
            System.out.printf("%d -> %s (%s)\n", (i + 1), usuarios.get(i).getNome(), usuarios.get(i).getEmail());
        }
        System.out.print("Escolha um utilizador: ");
        int escolha = UtilitariosInterface.lerInteiro(scanner);
        if (escolha > 0 && escolha <= usuarios.size()) {
            return usuarios.get(escolha - 1);
        }
        System.out.println("❌ Opção inválida. Operação cancelada.");
        return null;
    }
    
    /**
     * Classe interna para agrupar os dados comuns de uma tarefa.
     */
    private record TarefaDadosComuns(String titulo, String descricao, Prioridade prioridade, LocalDateTime prazo, Categoria categoria) {}

    // =================================================================================
    // EXECUÇÃO DE TAREFAS TEMPORIZADAS (POMODORO)
    // =================================================================================
    
    /**
     * Executa uma tarefa temporizada com interface Pomodoro.
     */
    private void executarTarefaTemporizada() {
        System.out.println("--- 🍅 EXECUTAR TAREFA TEMPORIZADA (POMODORO) ---");
        
        // Listar tarefas temporizadas disponíveis
        List<TarefaTemporizada> tarefasTemporizadas = gerenciador.listarTarefasTemporizadas();
        
        if (tarefasTemporizadas.isEmpty()) {
            System.out.println("❌ Nenhuma tarefa temporizada encontrada.");
            System.out.println("Crie uma tarefa temporizada primeiro.");
            return;
        }
        
        // Exibir lista de tarefas temporizadas
        System.out.println("\n📋 Tarefas Temporizadas Disponíveis:");
        for (int i = 0; i < tarefasTemporizadas.size(); i++) {
            TarefaTemporizada tarefa = tarefasTemporizadas.get(i);
            System.out.printf("%d -> %s (Sessões: %d/%d)\n", 
                i + 1, tarefa.getTitulo(), tarefa.getSessoesCompletadas(), tarefa.getTotalSessoes());
        }
        
        System.out.print("\nEscolha uma tarefa (0 para cancelar): ");
        int escolha = UtilitariosInterface.lerInteiro(scanner);
        
        if (escolha <= 0 || escolha > tarefasTemporizadas.size()) {
            System.out.println("Operação cancelada.");
            return;
        }
        
        TarefaTemporizada tarefa = tarefasTemporizadas.get(escolha - 1);
        executarSessaoPomodoro(tarefa);
    }
    
    /**
     * Executa uma sessão Pomodoro para uma tarefa específica.
     */
    private void executarSessaoPomodoro(TarefaTemporizada tarefa) {
        System.out.println("\n🍅 EXECUTANDO TAREFA TEMPORIZADA");
        System.out.println("📝 " + tarefa.getTitulo());
        System.out.println("🎯 Sessões: " + tarefa.getSessoesCompletadas() + "/" + tarefa.getTotalSessoes());
        
        if (tarefa.isCompleta()) {
            System.out.println("✅ Todas as sessões foram completadas!");
            return;
        }
        
        if (tarefa.isSessaoAtiva()) {
            exibirSessaoAtiva(tarefa);
        } else {
            iniciarNovaSessao(tarefa);
        }
    }
    
    /**
     * Inicia uma nova sessão Pomodoro.
     */
    private void iniciarNovaSessao(TarefaTemporizada tarefa) {
        System.out.println("\n⏰ Próxima sessão: " + tarefa.getProximaSessao() + " de " + tarefa.getTotalSessoes());
        System.out.println("⏱️ Duração: " + tarefa.getDuracaoSessao().toMinutes() + " minutos");
        System.out.println("⏸️ Pausa: " + tarefa.getDuracaoPausa().toMinutes() + " minutos");
        
        System.out.print("\nPressione ENTER para iniciar a sessão...");
        scanner.nextLine();
        
        try {
            gerenciador.iniciarSessaoPomodoro(tarefa.getId());
            exibirSessaoAtiva(tarefa);
        } catch (Exception e) {
            System.out.println("\n❌ Erro ao iniciar sessão: " + e.getMessage());
        }
    }
    
    /**
     * Exibe a sessão ativa com contador e barra de progresso.
     */
    private void exibirSessaoAtiva(TarefaTemporizada tarefa) {
        boolean continuarSessao = true;
        long ultimaAtualizacao = System.currentTimeMillis();
        
        while (tarefa.isSessaoAtiva() && continuarSessao) {
            long agora = System.currentTimeMillis();
            
            // Atualizar tela a cada 2 segundos
            if (agora - ultimaAtualizacao >= 1000) {
                // Limpar tela e atualizar display
                System.out.print("\033[H\033[2J");
                System.out.flush();
                
                // Cabeçalho
                System.out.println("🍅 SESSÃO POMODORO ATIVA");
                System.out.println("═══════════════════════════════════════════════════════════");
                System.out.println("📝 Tarefa: " + tarefa.getTitulo());
                System.out.println("⏱️ Sessão: " + (tarefa.getSessoesCompletadas() + 1) + " de " + tarefa.getTotalSessoes());
                
                // Tempo e progresso
                Duration tempoRestante = tarefa.getTempoRestante();
                double progresso = tarefa.getProgressoSessao();
                
                // Status da sessão
                String statusSessao = tarefa.isSessaoPausada() ? "⏸️ PAUSADA" : "▶️ ATIVA";
                System.out.println("Status: " + statusSessao);
                
                System.out.printf("⏰ Tempo restante: %02d:%02d\n", 
                    tempoRestante.toMinutes(), tempoRestante.toSecondsPart());
                System.out.printf("📊 Progresso: %s %.1f%%\n", 
                    UtilitariosInterface.criarBarraProgresso(progresso), progresso);
                
                // Verificar se a sessão expirou
                if (tempoRestante.isZero() || tempoRestante.isNegative()) {
                    System.out.println("\n🔔 SESSÃO EXPIRADA!");
                    try {
                        gerenciador.concluirSessaoPomodoro(tarefa.getId());
                        System.out.println("✅ Sessão automaticamente concluída!");
                        
                        if (!tarefa.isCompleta()) {
                            System.out.println("⏸️ Pausa de " + tarefa.getDuracaoPausa().toMinutes() + " minutos.");
                            System.out.print("Pressione ENTER para iniciar a próxima sessão...");
                            scanner.nextLine();
                            
                            // Iniciar próxima sessão automaticamente
                            gerenciador.iniciarSessaoPomodoro(tarefa.getId());
                            System.out.println("🍅 Próxima sessão iniciada!");
                            try { Thread.sleep(1000); } catch (InterruptedException ie) {}
                            ultimaAtualizacao = System.currentTimeMillis();
                            continue; // Continuar no loop da sessão
                        } else {
                            System.out.println("🎉 Todas as sessões foram completadas!");
                            System.out.println("Pressione ENTER para sair...");
                            scanner.nextLine();
                            break; // Sair do loop
                        }
                    } catch (Exception e) {
                        System.out.println("❌ Erro ao concluir sessão: " + e.getMessage());
                        break;
                    }
                }
                
                // Menu
                if (tarefa.isSessaoPausada()) {
                    System.out.println("\n[1] Retomar Sessão");
                } else {
                    System.out.println("\n[1] Pausar Sessão");
                }
                System.out.println("[2] Concluir Sessão");
                System.out.println("[3] Concluir Tarefa");
                System.out.println("[0] Cancelar");
                System.out.println("\n💡 Digite um número (0-3) e pressione ENTER:");
                System.out.print("Opção: ");
                
                ultimaAtualizacao = agora;
            }
            
            // Verificar se há input disponível (método mais simples)
            try {
                if (System.in.available() > 0) {
                    String input = scanner.nextLine().trim();
                    
                    if (!input.isEmpty()) {
                        try {
                            int opcao = Integer.parseInt(input);
                            if (opcao >= 0 && opcao <= 3) {
                                continuarSessao = processarOpcaoSessao(tarefa, opcao);
                                ultimaAtualizacao = 0; // Forçar atualização imediata
                            } else {
                                System.out.println("❌ Opção inválida! Digite 0, 1, 2 ou 3.");
                                System.out.print("Opção: ");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("❌ Digite apenas números! (0-3)");
                            System.out.print("Opção: ");
                        }
                    }
                } else {
                    // Pequena pausa para não sobrecarregar CPU
                    try { Thread.sleep(100); } catch (InterruptedException ie) {}
                }
            } catch (Exception e) {
                // Em caso de erro, continuar com delay
                try { Thread.sleep(100); } catch (InterruptedException ie) {}
            }
        }
    }
    
    /**
     * Processa as opções do menu da sessão ativa.
     * @return true para continuar sessão, false para parar
     */
    private boolean processarOpcaoSessao(TarefaTemporizada tarefa, int opcao) {
        try {
            switch (opcao) {
                case 1 -> {
                    if (tarefa.isSessaoPausada()) {
                        // Retomar sessão
                        gerenciador.retomarSessaoPomodoro(tarefa.getId());
                        System.out.println("▶️ Sessão retomada!");
                        try { Thread.sleep(1500); } catch (InterruptedException ie) {} // Pausa para mostrar mensagem
                    } else {
                        // Pausar sessão
                        gerenciador.pausarSessaoPomodoro(tarefa.getId());
                        System.out.println("⏸️ Sessão pausada!");
                        try { Thread.sleep(1500); } catch (InterruptedException ie) {} // Pausa para mostrar mensagem
                    }
                    return true; // Continuar na sessão
                }
                case 2 -> {
                    gerenciador.concluirSessaoPomodoro(tarefa.getId());
                    System.out.println("✅ Sessão concluída!");
                    
                    if (tarefa.isCompleta()) {
                        System.out.println("🎉 Todas as sessões foram completadas!");
                        System.out.println("Pressione ENTER para sair...");
                        scanner.nextLine();
                        return false; // Parar após completar todas as sessões
                    } else {
                        System.out.println("⏸️ Pausa de " + tarefa.getDuracaoPausa().toMinutes() + " minutos.");
                        System.out.print("Deseja iniciar a próxima sessão? (s/N): ");
                        String resposta = scanner.nextLine().trim().toLowerCase();
                        
                        if (resposta.equals("s") || resposta.equals("sim")) {
                            // Iniciar próxima sessão
                            gerenciador.iniciarSessaoPomodoro(tarefa.getId());
                            System.out.println("🍅 Próxima sessão iniciada!");
                            try { Thread.sleep(1000); } catch (InterruptedException ie) {}
                            return true; // Continuar para próxima sessão
                        } else {
                            System.out.println("Sessão pausada. Você pode continuar depois.");
                            return false; // Sair da interface de sessão
                        }
                    }
                }
                case 3 -> {
                    System.out.print("Tem certeza que deseja concluir toda a tarefa? (s/N): ");
                    String confirmacao = scanner.nextLine().trim().toLowerCase();
                    
                    if (confirmacao.equals("s") || confirmacao.equals("sim")) {
                        gerenciador.concluirTarefa(tarefa.getId());
                        System.out.println("✅ Tarefa completamente concluída!");
                        System.out.println("Pressione ENTER para sair...");
                        scanner.nextLine();
                        return false; // Parar a sessão
                    } else {
                        System.out.println("Operação cancelada.");
                        try { Thread.sleep(1000); } catch (InterruptedException ie) {}
                        return true; // Continuar sessão
                    }
                }
                case 0 -> {
                    System.out.print("Tem certeza que deseja cancelar a sessão? (s/N): ");
                    String confirmacao = scanner.nextLine().trim().toLowerCase();
                    
                    if (confirmacao.equals("s") || confirmacao.equals("sim")) {
                        System.out.println("Sessão cancelada.");
                        return false; // Parar a sessão
                    } else {
                        return true; // Continuar sessão
                    }
                }
                default -> {
                    System.out.println("❌ Opção inválida. Tente novamente.");
                    try { Thread.sleep(1000); } catch (InterruptedException ie) {}
                    return true; // Continuar sessão
                }
            }
        } catch (Exception e) {
            System.out.println("\n❌ Erro: " + e.getMessage());
            try { Thread.sleep(2000); } catch (InterruptedException ie) {}
            return true; // Continuar em caso de erro
        }
    }
}