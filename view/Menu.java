package view;

import model.Tarefa;
import model.Prioridade;
import service.SessaoService;
import service.TarefaService;
import service.UsuarioService;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Menu {
    Scanner sc = new Scanner(System.in);
    private UsuarioService usuarioService;
    private TarefaService tarefaService;
    private SessaoService sessao;

    public Menu(TarefaService tarefaService, UsuarioService usuarioService, SessaoService sessao) {
        this.tarefaService = tarefaService;
        this.usuarioService = usuarioService;
        this.sessao = sessao;
    }

    public void solicitarUsuario() {
        int opcao;
        do {
            System.out.println("===================================");
            System.out.println("      BEM-VINDO AO TO-DO-LIST      ");
            System.out.println("===================================");
            System.out.println("Para testar, use e-mail: teste@teste.com e senha: teste");
            System.out.println("1 -> Fazer login");
            System.out.println("2 -> Cadastrar novo usuário");
            System.out.print("Escolha uma opção: ");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1 -> {
                    System.out.print("Informe seu e-mail: ");
                    String email = sc.nextLine();
                    System.out.print("Informe a senha: ");
                    String senha = sc.nextLine();
                    if (usuarioService.autenticar(email, senha)) {
                        return;
                    }
                }
                case 2 -> {
                    System.out.print("Informe seu nome: ");
                    String nome = sc.nextLine();
                    System.out.print("Informe seu e-mail: ");
                    String email = sc.nextLine();
                    System.out.print("Informe uma senha: ");
                    String senha = sc.nextLine();
                    usuarioService.cadastrarUsuario(nome, email, senha);
                }
                default -> System.out.println("Opção inválida.");
            }
        } while (!sessao.estaLogado());
    }

    public void mostrarMenu(){

        if (!sessao.estaLogado()) {
            solicitarUsuario();
        }
        
        int opcao;
        do {
            System.out.println("------ MENU TO-DO LIST ------");
            System.out.println("1 -> Criar tarefa");
            System.out.println("2 -> Listar tarefas");
            System.out.println("3 -> Atualizar tarefa");
            System.out.println("4 -> Concluir tarefa");
            System.out.println("5 -> Remover tarefa");
            System.out.println("6 -> Mostrar dados do usuário");
            System.out.println("7 -> Logout");
            System.out.println("0 -> Sair");
            System.out.println("-----------------------------");
            System.out.print("Opção: ");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1 -> menuCriarTarefa();
                case 2 -> menuListarTarefas();
                case 3 -> menuAtualizarTarefa();
                case 4 -> menuConcluirTarefa();
                case 5 -> menuRemoverTarefa();
                case 6 -> mostrarDadosUsuario();
                case 7 -> {
                    sessao.logout();
                    solicitarUsuario();
                }
                case 0 -> System.out.println("Encerrando...");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private void menuCriarTarefa() {
        System.out.print("Título da tarefa: ");
        String titulo = sc.nextLine();
        System.out.print("Descrição da tarefa: ");
        String descricao = sc.nextLine();

        System.out.print("Deseja definir prioridade? (s/n): ");
        String respPrioridade = sc.nextLine();
        Prioridade prioridade = null;
        if (respPrioridade.equalsIgnoreCase("s")) {
            System.out.print("Prioridade (BAIXA, MEDIA, ALTA): ");
            String nivel = sc.nextLine().toUpperCase();
            prioridade = Prioridade.valueOf(nivel);
        }

        System.out.print("Deseja definir prazo? (s/n): ");
        String respPrazo = sc.nextLine();
        java.time.LocalDate prazo = null;
        if (respPrazo.equalsIgnoreCase("s")) {
            while (prazo == null) {
                System.out.print("Prazo (dd/mm/aaaa): ");
                String entrada = sc.nextLine();
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    prazo = java.time.LocalDate.parse(entrada, formatter);
                } catch (DateTimeParseException e) {
                    System.out.println("Formato de data inválido. Tente novamente.");
                }
            }
        }

        tarefaService.criarTarefa(titulo, descricao, prioridade, prazo);
    }

    private void menuListarTarefas() {
        int subOpcao;
        do {
            System.out.println("------ LISTAR TAREFAS ------");
            System.out.println("1 -> Todas as tarefas");
            System.out.println("2 -> Por prioridade");
            System.out.println("3 -> Apenas concluídas");
            System.out.println("4 -> Apenas pendentes");
            System.out.println("5 -> Atrasadas");
            System.out.println("6 -> Buscar por ID");
            System.out.println("0 -> Voltar");
            System.out.print("Opção: ");
            subOpcao = sc.nextInt();
            sc.nextLine();

            switch (subOpcao) {
                case 1 -> 
                    TarefaView.exibirLista(tarefaService.listarTarefas());
                case 2 -> {
                    System.out.print("Informe a prioridade (BAIXA, MEDIA, ALTA): ");
                    String nivel = sc.nextLine().toUpperCase();
                    try {
                        Prioridade p = Prioridade.valueOf(nivel);
                        TarefaView.exibirLista(tarefaService.listarPorPrioridade(p));
                    } catch (IllegalArgumentException e) {
                        System.out.println("Prioridade inválida.");
                    }
                }
                case 3 -> TarefaView.exibirLista(tarefaService.listarConcluidas());
                case 4 -> TarefaView.exibirLista(tarefaService.listarPendentes());
                case 5 -> TarefaView.exibirListaAtrasadas(tarefaService.listarAtrasadas());
                case 6 -> {
                    System.out.print("-> ID da tarefa: ");
                    String id = sc.nextLine();
                    Tarefa t = tarefaService.buscarTarefaPorId(id);
                    if (t != null) {
                        TarefaView.exibirDetalhada(t);
                    } else {
                        System.out.println("Tarefa não encontrada.");
                    }
                }
                case 0 -> System.out.println("Voltando ao menu principal...");
                default -> System.out.println("Opção inválida.");
            }
        } while (subOpcao != 0);
    }

    private void menuAtualizarTarefa() {
        System.out.print("-> ID da tarefa: ");
        String id = sc.nextLine();
        Tarefa tarefa = tarefaService.buscarTarefaPorId(id);

        if (tarefa == null) {
            System.out.println("Tarefa não encontrada.");
            return;
        }

        int editar;
        do {
            System.out.println("=== Editar Tarefa ===");
            System.out.println("1 -> Título");
            System.out.println("2 -> Descrição");
            System.out.println("3 -> Prioridade");
            System.out.println("4 -> Prazo");
            System.out.println("0 -> Voltar");
            System.out.print("Opção: ");
            editar = sc.nextInt();
            sc.nextLine();

            switch (editar) {
                case 1 -> {
                    System.out.print("Novo título: ");
                    String novoTitulo = sc.nextLine();
                    tarefaService.atualizarTarefa(id, novoTitulo, null, null, null);
                }
                case 2 -> {
                    System.out.print("Nova descrição: ");
                    String novaDescricao = sc.nextLine();
                    tarefaService.atualizarTarefa(id, null, novaDescricao, null, null);
                }
                case 3 -> {
                    System.out.print("Nova prioridade (BAIXA, MEDIA, ALTA): ");
                    String nivel = sc.nextLine().toUpperCase();
                    try {
                        Prioridade nova = Prioridade.valueOf(nivel);
                        tarefaService.atualizarTarefa(id, null, null, nova, null);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Prioridade inválida.");
                    }
                }
                case 4 -> {
                    System.out.print("Novo prazo (dd/mm/aaaa): ");
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        java.time.LocalDate novoPrazo = java.time.LocalDate.parse(sc.nextLine(), formatter);
                        tarefaService.atualizarTarefa(id, null, null, null, novoPrazo);
                    } catch (DateTimeParseException e) {
                        System.out.println("Data inválida.");
                    }
                }
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida.");
            }
        } while (editar != 0);
    }

    private void menuConcluirTarefa() {
        System.out.print("-> ID da tarefa a concluir: ");
        String idConclusao = sc.nextLine();
        boolean concluida = tarefaService.concluirTarefa(idConclusao);
        if (concluida) {
            System.out.println("Tarefa concluída com sucesso!");
        } else {
            System.out.println("Não foi possível concluir a tarefa. Verifique se o ID está correto e se você tem permissão.");
        }
    }

    private void menuRemoverTarefa() {
        System.out.print("-> ID da tarefa a remover: ");
        String idRemocao = sc.nextLine();
        boolean removida = tarefaService.removerTarefa(idRemocao);
        if (removida) {
            System.out.println("Tarefa removida com sucesso!");
        } else {
            System.out.println("Não foi possível remover a tarefa. Verifique se o ID está correto e se você tem permissão.");
        }
    }

    private void mostrarDadosUsuario() {
        System.out.println("=== DADOS DO USUÁRIO LOGADO ===");
        System.out.println("Nome: " + sessao.getUsuarioLogado().getNome());
        System.out.println("E-mail: " + sessao.getUsuarioLogado().getEmail());
    }

}