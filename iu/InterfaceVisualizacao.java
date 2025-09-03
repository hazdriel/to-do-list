package iu;

import fachada.Gerenciador;
import negocio.entidade.*;
import java.util.List;
import java.util.Scanner;

public class InterfaceVisualizacao {
    
    private final Scanner scanner;
    private final Gerenciador gerenciador;
    
    public InterfaceVisualizacao(Scanner scanner, Gerenciador gerenciador) {
        this.scanner = scanner;
        this.gerenciador = gerenciador;
    }
    
    public void exibirMenuVisualizacao() {
        System.out.println("\n------ VISUALIZAR TAREFAS ------");
        System.out.println("1 -> Todas as Tarefas");
        System.out.println("2 -> Minhas Tarefas (Responsabilidades)");
        System.out.println("3 -> Tarefas Delegadas por Mim");
        System.out.println("4 -> Tarefas Delegadas para Mim");
        System.out.println("5 -> Por Prioridade");
        System.out.println("6 -> Por Status");
        System.out.println("7 -> Por Tipo");
        System.out.println("8 -> Por Categoria");
        System.out.println("9 -> Buscar por ID");
        System.out.println("10 -> Estatísticas");
        System.out.println("0 -> Voltar");
        System.out.print("Opção: ");

        int opcao = UtilitariosInterface.lerInteiro(scanner);
        scanner.nextLine(); 

        switch (opcao) {
            case 1 -> exibirTarefas(gerenciador.listarTarefas(), "TODAS AS TAREFAS");
            case 2 -> exibirMinhasTarefas();
            case 3 -> exibirTarefasDelegadasPorMim();
            case 4 -> exibirTarefasDelegadasParaMim();
            case 5 -> exibirPorPrioridade();
            case 6 -> exibirPorStatus();
            case 7 -> exibirPorTipo();
            case 8 -> exibirPorCategoria();
            case 9 -> buscarTarefaPorId();
            case 10 -> exibirEstatisticas();
            case 0 -> System.out.println("Voltando ao menu principal...");
            default -> System.out.println("Opção inválida.");
        }
    }
    
    private void exibirPorPrioridade() {
        System.out.println("\n--- FILTRAR POR PRIORIDADE ---");
        Prioridade prioridade = UtilitariosInterface.lerPrioridade(scanner);
        
        if (prioridade != null) {
            List<TarefaAbstrata> tarefas = gerenciador.listarPorPrioridade(prioridade);
            exibirTarefas(tarefas, "TAREFAS COM PRIORIDADE " + prioridade);
        } else {
            System.out.println("Operação cancelada.");
        }
    }
    
    private void exibirPorStatus() {
        System.out.println("\n--- FILTRAR POR STATUS ---");
        System.out.println("1 -> PENDENTE");
        System.out.println("2 -> EM_PROGRESSO");
        System.out.println("3 -> CONCLUIDA");
        System.out.println("4 -> CANCELADA");
        System.out.print("Opção: ");
        
        int opcao = UtilitariosInterface.lerInteiro(scanner);
        scanner.nextLine();
        
        Status status = switch (opcao) {
            case 1 -> Status.PENDENTE;
            case 2 -> Status.EM_PROGRESSO;
            case 3 -> Status.CONCLUIDA;
            case 4 -> Status.CANCELADA;
            default -> {
                System.out.println("Opção inválida.");
                yield null;
            }
        };
        
        if (status != null) {
            List<TarefaAbstrata> tarefas = switch (status) {
                case PENDENTE -> gerenciador.listarPendentes();
                case EM_PROGRESSO -> gerenciador.listarTarefas().stream()
                    .filter(t -> t.getStatus() == Status.EM_PROGRESSO)
                    .toList();
                case CONCLUIDA -> gerenciador.listarConcluidas();
                case CANCELADA -> gerenciador.listarTarefas().stream()
                    .filter(t -> t.getStatus() == Status.CANCELADA)
                    .toList();
            };
            exibirTarefas(tarefas, "TAREFAS COM STATUS " + status);
        }
    }
    
    private void exibirPorTipo() {
        System.out.println("\n--- FILTRAR POR TIPO ---");
        System.out.println("1 -> Simples");
        System.out.println("2 -> Delegável");
        System.out.println("3 -> Recorrente");
        System.out.println("4 -> Temporizada");
        System.out.print("Opção: ");
        
        int opcao = UtilitariosInterface.lerInteiro(scanner);
        scanner.nextLine();
        
        String tipo = switch (opcao) {
            case 1 -> "Simples";
            case 2 -> "Delegável";
            case 3 -> "Recorrente";
            case 4 -> "Temporizada";
            default -> {
                System.out.println("Opção inválida.");
                yield null;
            }
        };
        
        if (tipo != null) {
            List<TarefaAbstrata> tarefas = gerenciador.listarPorTipo(tipo);
            exibirTarefas(tarefas, "TAREFAS DO TIPO " + tipo.toUpperCase());
        }
    }
    
    private void exibirPorCategoria() {
        System.out.println("\n--- FILTRAR POR CATEGORIA ---");
        List<Categoria> categorias = gerenciador.listarCategorias();
        
        if (categorias.isEmpty()) {
            System.out.println("Nenhuma categoria disponível.");
            return;
        }
        
        System.out.println("Categorias disponíveis:");
        for (int i = 0; i < categorias.size(); i++) {
            System.out.println((i + 1) + " -> " + categorias.get(i).getNome());
        }
        
        System.out.print("Escolha uma categoria (número): ");
        int escolha = UtilitariosInterface.lerInteiro(scanner);
        scanner.nextLine();
        
        if (escolha > 0 && escolha <= categorias.size()) {
            Categoria categoria = categorias.get(escolha - 1);
            List<TarefaAbstrata> tarefas = gerenciador.listarPorCategoria(categoria);
            exibirTarefas(tarefas, "TAREFAS DA CATEGORIA " + categoria.getNome().toUpperCase());
        } else {
            System.out.println("Escolha inválida.");
        }
    }
    
    private void exibirMinhasTarefas() {
        System.out.println("\n--- MINHAS TAREFAS (RESPONSABILIDADES) ---");
        
        try {
                        List<TarefaAbstrata> minhasTarefas = gerenciador.listarTarefasDoUsuario();
            
            if (minhasTarefas.isEmpty()) {
                System.out.println("📭 Você não tem responsabilidades em nenhuma tarefa delegável.");
                System.out.println("Pressione Enter para continuar...");
                scanner.nextLine();
                return;
            }
            
            System.out.println("📋 Tarefas onde você é responsável:");
            System.out.println("=".repeat(60));
            
            for (TarefaAbstrata tarefa : minhasTarefas) {
                System.out.println("🆔 ID: " + tarefa.getId());
                System.out.println("📝 Título: " + tarefa.getTitulo());
                System.out.println("📄 Descrição: " + tarefa.getDescricao());
                System.out.println("🎯 Prioridade: " + tarefa.getPrioridade());
                System.out.println("📅 Prazo: " + UtilitariosInterface.formatarDataHora(tarefa.getPrazo()));
                System.out.println("📊 Status: " + tarefa.getStatus());
                System.out.println("👤 Criador: " + tarefa.getCriador().getNome() + " (" + tarefa.getCriador().getEmail() + ")");
                if (tarefa instanceof Delegavel) {
                    Delegavel delegavel = (Delegavel) tarefa;
                    List<Usuario> responsaveis = delegavel.getResponsaveis();
                    
                    System.out.println("👥 Responsáveis atuais (" + responsaveis.size() + "):");
                    for (Usuario responsavel : responsaveis) {
                        String indicador = responsavel.equals(gerenciador.getUsuarioLogado()) ? "👉 VOCÊ" : "👤";
                        System.out.println("   " + indicador + " " + responsavel.getNome() + " (" + responsavel.getEmail() + ")");
                    }
                    
                    System.out.println("📚 Histórico de delegações: " + delegavel.getHistoricoDelegacoes().size());
                }
                
                System.out.println("-".repeat(40));
            }
            
            System.out.println("Total: " + minhasTarefas.size() + " tarefa(s) onde você é responsável");
            
        } catch (Exception e) {
            System.out.println("❌ Erro ao buscar suas tarefas: " + e.getMessage());
        }
        
        System.out.println("Pressione Enter para continuar...");
        scanner.nextLine();
    }
    
    private void exibirTarefasDelegadasPorMim() {
        System.out.println("\n--- TAREFAS DELEGADAS POR MIM ---");
        
        try {
            List<TarefaAbstrata> tarefasDelegadas = gerenciador.listarTarefasDelegadasPeloUsuario();
            
            if (tarefasDelegadas.isEmpty()) {
                System.out.println("📭 Você não delegou nenhuma tarefa para outros usuários.");
                System.out.println("Pressione Enter para continuar...");
                scanner.nextLine();
                return;
            }
            
            System.out.println("📋 Tarefas que você criou e delegou para outros usuários:");
            System.out.println("=".repeat(70));
            
            for (TarefaAbstrata tarefa : tarefasDelegadas) {
                System.out.println("🆔 ID: " + tarefa.getId());
                System.out.println("📝 Título: " + tarefa.getTitulo());
                System.out.println("📄 Descrição: " + tarefa.getDescricao());
                System.out.println("🎯 Prioridade: " + tarefa.getPrioridade());
                System.out.println("📅 Prazo: " + UtilitariosInterface.formatarDataHora(tarefa.getPrazo()));
                System.out.println("📊 Status: " + tarefa.getStatus());

                if (tarefa instanceof Delegavel) {
                    Delegavel delegavel = (Delegavel) tarefa;
                    List<Usuario> responsaveis = delegavel.getResponsaveis();
                    
                    System.out.println("👥 Responsáveis atuais (" + responsaveis.size() + "):");
                    for (Usuario responsavel : responsaveis) {
                        String indicador = responsavel.equals(gerenciador.getUsuarioLogado()) ? "👉 VOCÊ (Criador)" : "👤";
                        System.out.println("   " + indicador + " " + responsavel.getNome() + " (" + responsavel.getEmail() + ")");
                    }
                    
                    System.out.println("📚 Histórico de delegações: " + delegavel.getHistoricoDelegacoes().size());
                    
                    if (!delegavel.getHistoricoDelegacoes().isEmpty()) {
                        System.out.println("📖 Últimas delegações:");
                        for (RegistroDelegacao registro : delegavel.getHistoricoDelegacoes()) {
                            System.out.println("   📅 " + registro.getDataDelegacao() + " - " + registro.getMotivo());
                        }
                    }
                }
                
                System.out.println("-".repeat(40));
            }
            
            System.out.println("Total: " + tarefasDelegadas.size() + " tarefa(s) que você criou e delegou");
            
        } catch (Exception e) {
            System.out.println("❌ Erro ao buscar tarefas delegadas por você: " + e.getMessage());
        }
        
        System.out.println("Pressione Enter para continuar...");
        scanner.nextLine();
    }
    
    private void exibirTarefasDelegadasParaMim() {
        System.out.println("\n--- TAREFAS DELEGADAS PARA MIM ---");
        
        try {
            List<TarefaAbstrata> tarefasDelegadas = gerenciador.listarTarefasDelegadasParaUsuario();
            
            if (tarefasDelegadas.isEmpty()) {
                System.out.println("📭 Nenhuma tarefa foi delegada para você por outros usuários.");
                System.out.println("Pressione Enter para continuar...");
                scanner.nextLine();
                return;
            }
            
            System.out.println("📋 Tarefas delegadas para você por outros usuários:");
            System.out.println("=".repeat(60));
            
            for (TarefaAbstrata tarefa : tarefasDelegadas) {
                System.out.println("🆔 ID: " + tarefa.getId());
                System.out.println("📝 Título: " + tarefa.getTitulo());
                System.out.println("📄 Descrição: " + tarefa.getDescricao());
                System.out.println("🎯 Prioridade: " + tarefa.getPrioridade());
                System.out.println("📅 Prazo: " + UtilitariosInterface.formatarDataHora(tarefa.getPrazo()));
                System.out.println("📊 Status: " + tarefa.getStatus());
                System.out.println("👤 Criador: " + tarefa.getCriador().getNome() + " (" + tarefa.getCriador().getEmail() + ")");
            
                if (tarefa instanceof Delegavel) {
                    Delegavel delegavel = (Delegavel) tarefa;
                    List<Usuario> responsaveis = delegavel.getResponsaveis();
                    
                    System.out.println("👥 Responsáveis atuais (" + responsaveis.size() + "):");
                    for (Usuario responsavel : responsaveis) {
                        String indicador = responsavel.equals(gerenciador.getUsuarioLogado()) ? "👉 VOCÊ" : "👤";
                        System.out.println("   " + indicador + " " + responsavel.getNome() + " (" + responsavel.getEmail() + ")");
                    }
                    
                    System.out.println("📚 Histórico de delegações: " + delegavel.getHistoricoDelegacoes().size());
                    
                    if (!delegavel.getHistoricoDelegacoes().isEmpty()) {
                        System.out.println("📖 Últimas delegações:");
                        for (RegistroDelegacao registro : delegavel.getHistoricoDelegacoes()) {
                            System.out.println("   📅 " + registro.getDataDelegacao() + " - " + registro.getMotivo());
                        }
                    }
                }
                
                System.out.println("-".repeat(40));
            }
            
            System.out.println("Total: " + tarefasDelegadas.size() + " tarefa(s) delegada(s) para você por outros usuários");
            
        } catch (Exception e) {
            System.out.println("❌ Erro ao buscar tarefas delegadas: " + e.getMessage());
        }
        
        System.out.println("Pressione Enter para continuar...");
        scanner.nextLine();
    }
    
    private void buscarTarefaPorId() {
        System.out.println("\n--- BUSCAR TAREFA POR ID ---");
        String id = UtilitariosInterface.lerString(scanner, "ID da tarefa: ");
        
        TarefaAbstrata tarefa = gerenciador.buscarTarefa(id);
        
        if (tarefa != null) {
            UtilitariosInterface.exibirTarefaDetalhada(tarefa);
        } else {
            System.out.println("❌ Tarefa não encontrada.");
        }
    }
    
    private void exibirEstatisticas() {
        System.out.println("\n--- ESTATÍSTICAS DAS TAREFAS ---");
        
        List<TarefaAbstrata> todasTarefas = gerenciador.listarTarefas();
        if (todasTarefas == null || todasTarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa encontrada para gerar estatísticas.");
            return;
        }
        
        long totalTarefas = todasTarefas.size();
        long tarefasConcluidas = todasTarefas.stream()
            .filter(t -> t.getStatus() == Status.CONCLUIDA)
            .count();
        long tarefasPendentes = todasTarefas.stream()
            .filter(t -> t.getStatus() == Status.PENDENTE)
            .count();
        long tarefasEmProgresso = todasTarefas.stream()
            .filter(t -> t.getStatus() == Status.EM_PROGRESSO)
            .count();
        long tarefasAtrasadas = todasTarefas.stream()
            .filter(TarefaAbstrata::estaAtrasada)
            .count();
        
        long tarefasAlta = todasTarefas.stream()
            .filter(t -> t.getPrioridade() == Prioridade.ALTA)
            .count();
        long tarefasMedia = todasTarefas.stream()
            .filter(t -> t.getPrioridade() == Prioridade.MEDIA)
            .count();
        long tarefasBaixa = todasTarefas.stream()
            .filter(t -> t.getPrioridade() == Prioridade.BAIXA)
            .count();
        
        System.out.println("📊 ESTATÍSTICAS GERAIS:");
        System.out.println("   Total de tarefas: " + totalTarefas);
        System.out.println("   Tarefas concluídas: " + tarefasConcluidas + " (" + calcularPorcentagem(tarefasConcluidas, totalTarefas) + "%)");
        System.out.println("   Tarefas pendentes: " + tarefasPendentes + " (" + calcularPorcentagem(tarefasPendentes, totalTarefas) + "%)");
        System.out.println("   Tarefas em progresso: " + tarefasEmProgresso + " (" + calcularPorcentagem(tarefasEmProgresso, totalTarefas) + "%)");
        System.out.println("   Tarefas atrasadas: " + tarefasAtrasadas + " (" + calcularPorcentagem(tarefasAtrasadas, totalTarefas) + "%)");
        
        System.out.println("\n📈 DISTRIBUIÇÃO POR PRIORIDADE:");
        System.out.println("   Alta: " + tarefasAlta + " (" + calcularPorcentagem(tarefasAlta, totalTarefas) + "%)");
        System.out.println("   Média: " + tarefasMedia + " (" + calcularPorcentagem(tarefasMedia, totalTarefas) + "%)");
        System.out.println("   Baixa: " + tarefasBaixa + " (" + calcularPorcentagem(tarefasBaixa, totalTarefas) + "%)");
        
        long tarefasSimples = todasTarefas.stream()
            .filter(t -> "Simples".equals(t.getTipo()))
            .count();
        long tarefasDelegaveis = todasTarefas.stream()
            .filter(t -> "Delegável".equals(t.getTipo()))
            .count();
        long tarefasRecorrentes = todasTarefas.stream()
            .filter(t -> "Recorrente".equals(t.getTipo()))
            .count();
        long tarefasTemporizadas = todasTarefas.stream()
            .filter(t -> "Temporizada".equals(t.getTipo()))
            .count();
        
        System.out.println("\n🔧 DISTRIBUIÇÃO POR TIPO:");
        System.out.println("   Simples: " + tarefasSimples + " (" + calcularPorcentagem(tarefasSimples, totalTarefas) + "%)");
        System.out.println("   Delegáveis: " + tarefasDelegaveis + " (" + calcularPorcentagem(tarefasDelegaveis, totalTarefas) + "%)");
        System.out.println("   Recorrentes: " + tarefasRecorrentes + " (" + calcularPorcentagem(tarefasRecorrentes, totalTarefas) + "%)");
        System.out.println("   Temporizadas: " + tarefasTemporizadas + " (" + calcularPorcentagem(tarefasTemporizadas, totalTarefas) + "%)");
    }

    private String calcularPorcentagem(long valor, long total) {
        if (total == 0) return "0.0";
        double porcentagem = (double) valor / total * 100;
        return String.format("%.1f", porcentagem);
    }
    
    private void exibirTarefas(List<TarefaAbstrata> tarefas, String titulo) {
        UtilitariosInterface.exibirTarefas(tarefas, titulo);
    }
}
