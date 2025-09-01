package iu;

import negocio.NegocioSessao;
import negocio.NegocioTarefa;
import negocio.NegocioUsuario;
import negocio.entidade.Categoria;
import negocio.entidade.Prioridade;
import negocio.entidade.TarefaAntiga;
import negocio.excecao.tarefa.*;
import negocio.excecao.usuario.*;

import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TelaMenu {
    Scanner sc = new Scanner(System.in);
    private NegocioUsuario usuarioService;
    private NegocioTarefa tarefaService;
    private NegocioSessao sessao;

    public TelaMenu(NegocioTarefa tarefaService, NegocioUsuario usuarioService, NegocioSessao sessao) {
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
                    try {
                        if (usuarioService.autenticar(email, senha)) {
                            return;
                        }
                    } catch (UsuarioNaoEncontradoException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Reiniciando...");
                    } catch (SenhaIncorretaException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Reiniciando...");
                    }
                }
                case 2 -> {
                    System.out.print("Informe seu nome: ");
                    String nome = sc.nextLine();
                    System.out.print("Informe seu e-mail: ");
                    String email = sc.nextLine();
                    System.out.print("Informe uma senha: ");
                    String senha = sc.nextLine();
                    try {
                        usuarioService.cadastrarUsuario(nome, email, senha);
                    } catch (UsuarioExistenteException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Reiniciando...");
                    } catch (NomeVazioException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Reiniciando...");
                    } catch (NomeApenasLetrasException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Reiniciando...");
                    } catch (NomeTamanhoInvalidoException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Reiniciando...");
                    } catch (EmailVazioException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Reiniciando...");
                    } catch (EmailFormatoInvalidoException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Reiniciando...");
                    } catch (SenhaTamanhoInvalidoException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Reiniciando...");
                    }
                }
                default -> System.out.println("Opção inválida.");
            }
        } while (!sessao.estaLogado());
    }

    public void mostrarMenu()  {
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
        try {
            System.out.println("=== CRIAR NOVA TAREFA ===");

            System.out.print("Título da tarefa: ");
            String titulo = sc.nextLine();

            System.out.print("Descrição da tarefa: ");
            String descricao = sc.nextLine();

            System.out.print("Categoria da tarefa: ");
            String categoriaInput = sc.nextLine();
            Categoria categoria = new Categoria(categoriaInput);

            Prioridade prioridade = null;
            System.out.print("Deseja definir prioridade? (s/n): ");
            String respPrioridade = sc.nextLine();
            if (respPrioridade.equalsIgnoreCase("s")) {
                System.out.print("Prioridade (BAIXA, MEDIA, ALTA): ");
                String nivel = sc.nextLine().toUpperCase();
                try {
                    prioridade = Prioridade.valueOf(nivel);
                } catch (IllegalArgumentException e) {
                    System.out.println("Prioridade inválida. Usando prioridade BAIXA como padrão.");
                    prioridade = Prioridade.BAIXA;
                }
            }

            java.time.LocalDate prazo = null;
            System.out.print("Deseja definir prazo? (s/n): ");
            String respPrazo = sc.nextLine();
            if (respPrazo.equalsIgnoreCase("s")) {
                System.out.print("Prazo (dd/mm/aaaa): ");
                String entrada = sc.nextLine();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                prazo = java.time.LocalDate.parse(entrada, formatter);
            }

            tarefaService.criarTarefa(titulo, descricao, prioridade, prazo, categoria);
            System.out.println("Tarefa criada com sucesso!");

        } catch (DateTimeParseException e) {
            System.out.println("Formato de data inválido. Por favor, use a formatação dd/mm/aaaa.");
            System.out.println("Reiniciando criação de tarefa...");
            menuCriarTarefa();
        } catch (CategoriaVaziaException e) {
            System.out.println(e.getMessage());
            System.out.println("Reiniciando criação de tarefa...");
            menuCriarTarefa();
        } catch (TituloVazioException e) {
            System.out.println(e.getMessage());
            System.out.println("Reiniciando criação de tarefa...");
            menuCriarTarefa();
        } catch (PrazoInvalidoException e) {
            System.out.println(e.getMessage());
            System.out.println("Reiniciando criação de tarefa...");
            menuCriarTarefa();
        }
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
                        TelaTarefa.exibirLista(tarefaService.listarTarefas());
                case 2 -> {
                    System.out.print("Informe a prioridade (BAIXA, MEDIA, ALTA): ");
                    String prioridadeInput = sc.nextLine().trim().toUpperCase();
                    try {
                        Prioridade p = Prioridade.valueOf(prioridadeInput);
                        TelaTarefa.exibirLista(tarefaService.listarPorPrioridade(p));
                    } catch (IllegalArgumentException e) {
                        System.out.println("Prioridade inválida. Use: BAIXA, MEDIA ou ALTA");
                    } catch (Exception e) {
                        System.out.println("Erro ao buscar tarefas por prioridade: " + e.getMessage());
                    }
                }
                case 3 -> TelaTarefa.exibirLista(tarefaService.listarConcluidas());
                case 4 -> TelaTarefa.exibirLista(tarefaService.listarPendentes());
                case 5 -> TelaTarefa.exibirListaAtrasadas(tarefaService.listarAtrasadas());
                case 6 -> {
                    System.out.print("-> ID da tarefa: ");
                    String id = sc.nextLine();
                    TarefaAntiga t = null;
                    try {
                        t = tarefaService.buscarTarefaPorId(id);
                    } catch (TarefaIDNaoPertece e) {
                        System.out.println(e.getMessage());
                        System.out.println("Reiniciando...");
                        continue;
                    } catch (TarefaIDNaoEncontradaException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Reiniciando...");
                        continue;
                    }
                    if (t != null) {
                        TelaTarefa.exibirDetalhada(t);
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
        TarefaAntiga tarefa = null;

        while (tarefa == null) {
            try {
                tarefa = tarefaService.buscarTarefaPorId(id);
            } catch (TarefaIDNaoPertece | TarefaIDNaoEncontradaException e) {
                System.out.println(e.getMessage());
                System.out.print("Informe um ID válido: ");
                id = sc.nextLine();
            }
        }

        int editar;
        do {
            System.out.println("=== Editar Tarefa ===");
            System.out.println("1 -> Título");
            System.out.println("2 -> Descrição");
            System.out.println("3 -> Categoria");
            System.out.println("4 -> Prioridade");
            System.out.println("5 -> Prazo");
            System.out.println("0 -> Voltar");
            System.out.print("Opção: ");
            editar = sc.nextInt();
            sc.nextLine();

            switch (editar) {
                case 1 -> {
                    System.out.print("Novo título (deixe em branco para manter atual): ");
                    String novoTitulo = sc.nextLine();
                    if (novoTitulo.isBlank()) novoTitulo = null;
                    try {
                        tarefaService.atualizarTarefa(id, novoTitulo, null, null, null, null);
                    } catch (TituloVazioException | CategoriaVaziaException | PrazoInvalidoException |
                             TarefaIDNaoPertece | TarefaIDNaoEncontradaException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 2 -> {
                    System.out.print("Nova descrição (deixe em branco para manter atual): ");
                    String novaDescricao = sc.nextLine();
                    if (novaDescricao.isBlank()) novaDescricao = null;
                    try {
                        tarefaService.atualizarTarefa(id, null, novaDescricao, null, null, null);
                    } catch (TituloVazioException | CategoriaVaziaException | PrazoInvalidoException |
                             TarefaIDNaoPertece | TarefaIDNaoEncontradaException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 3 -> {
                    Categoria novaCategoria = null;
                    while (novaCategoria == null) {
                        System.out.print("Nova categoria (deixe em branco para manter atual): ");
                        String catInput = sc.nextLine();
                        if (catInput.isBlank()) break;
                        try {
                            novaCategoria = new Categoria(catInput);
                        } catch (CategoriaVaziaException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    try {
                        tarefaService.atualizarTarefa(id, null, null, novaCategoria, null, null);
                    } catch (TituloVazioException | CategoriaVaziaException | PrazoInvalidoException |
                             TarefaIDNaoPertece | TarefaIDNaoEncontradaException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 4 -> {
                    System.out.print("Nova prioridade (BAIXA, MEDIA, ALTA, deixe em branco para manter atual): ");
                    String nivel = sc.nextLine().toUpperCase();
                    Prioridade novaPrioridade = null;
                    if (!nivel.isBlank()) {
                        try {
                            novaPrioridade = Prioridade.valueOf(nivel);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Prioridade inválida. Mantendo valor atual.");
                        }
                    }
                    try {
                        tarefaService.atualizarTarefa(id, null, null, null, novaPrioridade, null);
                    } catch (TituloVazioException | CategoriaVaziaException | PrazoInvalidoException |
                             TarefaIDNaoPertece | TarefaIDNaoEncontradaException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 5 -> {
                    System.out.print("Novo prazo (dd/MM/yyyy, deixe em branco para manter atual): ");
                    String entrada = sc.nextLine();
                    java.time.LocalDate novoPrazo = null;
                    if (!entrada.isBlank()) {
                        try {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            novoPrazo = java.time.LocalDate.parse(entrada, formatter);
                        } catch (DateTimeParseException e) {
                            System.out.println("Formato de data inválido. Mantendo valor atual.");
                        }
                    }
                    try {
                        tarefaService.atualizarTarefa(id, null, null, null, null, novoPrazo);
                    } catch (TituloVazioException | CategoriaVaziaException | PrazoInvalidoException |
                             TarefaIDNaoPertece | TarefaIDNaoEncontradaException e) {
                        System.out.println(e.getMessage());
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
        boolean concluida = false;
        try {
            concluida = tarefaService.concluirTarefa(idConclusao);
        } catch (TarefaIDNaoPertece e) {
            System.out.println(e.getMessage());
            System.out.println("Reiniciando...");
            menuConcluirTarefa();
            return;
        } catch (TarefaIDNaoEncontradaException e) {
            System.out.println(e.getMessage());
            System.out.println("Reiniciando...");
            menuConcluirTarefa();
            return;
        }
        if (concluida) {
            System.out.println("Tarefa concluída com sucesso!");
        } else {
            System.out.println("Não foi possível concluir a tarefa. Verifique se o ID está correto e se você tem permissão.");
        }
    }

    private void menuRemoverTarefa() {
        System.out.print("-> ID da tarefa a remover: ");
        String idRemocao = sc.nextLine();
        boolean removida = false;
        try {
            removida = tarefaService.removerTarefa(idRemocao);
        } catch (TarefaIDNaoPertece e) {
            System.out.println(e.getMessage());
            System.out.println("Reiniciando...");
            menuRemoverTarefa();
            return;
        } catch (TarefaIDNaoEncontradaException e) {
            System.out.println(e.getMessage());
            System.out.println("Reiniciando...");
            menuRemoverTarefa();
            return;
        }
        if (removida) {
            System.out.println("Tarefa removida com sucesso!");
        }
    }

    private void mostrarDadosUsuario() {
        System.out.println("=== DADOS DO USUÁRIO LOGADO ===");
        System.out.println("Nome: " + sessao.getUsuarioLogado().getNome());
        System.out.println("E-mail: " + sessao.getUsuarioLogado().getEmail());
    }

}