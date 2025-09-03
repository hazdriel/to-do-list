package iu;

import fachada.Gerenciador;
import negocio.entidade.Usuario;
import java.util.Scanner;

/**
 * Coordenador central da camada de Interface de Utilizador (UI).
 * Esta classe atua como um "maestro", inicializando e orquestrando os diferentes
 * módulos especializados da interface (autenticação, tarefas, etc.),
 * e gerindo o fluxo principal da aplicação.
 */
public final class InterfacePrincipalRefatorada {
    
    private final Scanner scanner;
    private final Gerenciador gerenciador;
    private boolean executando;
    
    // Interfaces especializadas para cada funcionalidade.
    private final InterfaceAutenticacao interfaceAutenticacao;
    private final InterfaceTarefas interfaceTarefas;
    private final InterfaceVisualizacao interfaceVisualizacao;
    private final InterfaceCategorias interfaceCategorias;
    private final InterfaceRelatorios interfaceRelatorios;
    private final InterfacePerfil interfacePerfil;
    
    public InterfacePrincipalRefatorada(Gerenciador gerenciador) {
        this.scanner = new Scanner(System.in);
        this.gerenciador = gerenciador;
        this.executando = true;
        
        // Inicializa todos os módulos da UI, injetando as dependências necessárias.
        this.interfaceAutenticacao = new InterfaceAutenticacao(scanner, gerenciador);
        this.interfaceTarefas = new InterfaceTarefas(scanner, gerenciador);
        this.interfaceVisualizacao = new InterfaceVisualizacao(scanner, gerenciador);
        this.interfaceCategorias = new InterfaceCategorias(scanner, gerenciador);
        this.interfaceRelatorios = new InterfaceRelatorios(scanner, gerenciador);
        this.interfacePerfil = new InterfacePerfil(scanner, gerenciador);
    }
    
    /**
     * Ponto de entrada que inicia e controla o loop principal do sistema.
     */
    public void executar() {
    
        while (executando) {
            if (!gerenciador.estaLogado()) {
                boolean continuar = interfaceAutenticacao.exibirTelaLogin();
                if (!continuar) {
                    executando = false; // Utilizador escolheu sair do sistema.
                }
            } else {
                exibirMenuPrincipal();
            }
        }
        
        
        System.out.println("👋 Obrigado por usar o Lúmina! Até a próxima.");
        scanner.close();
    }
    
    /**
     * Exibe o menu principal e encaminha o utilizador para o módulo escolhido.
     */
    private void exibirMenuPrincipal() {
        
        exibirCabecalhoDoMenu();
        
        System.out.println("1 -> 📝 Gerenciar Tarefas");
        System.out.println("2 -> 🔍 Visualizar Tarefas");
        System.out.println("3 -> 📂 Gerenciar Categorias");
        System.out.println("4 -> 📊 Ver Relatórios");
        System.out.println("5 -> 👤 Ver Perfil");
        System.out.println("6 -> 🚪 Logout");
        System.out.println("0 -> ❌ Sair do Sistema");
        System.out.println("-".repeat(50));
        
        System.out.print("Escolha uma opção: ");
        int opcao = UtilitariosInterface.lerInteiro(scanner);
        
        processarOpcaoDoMenuPrincipal(opcao);
    }

    /**
     * Exibe o cabeçalho padronizado do menu principal, com as informações do utilizador.
     */
    private void exibirCabecalhoDoMenu() {
        System.out.println("=".repeat(50));
        System.out.println("              🎯 MENU PRINCIPAL 🎯");
        System.out.println("=".repeat(50));
        Usuario usuarioLogado = gerenciador.getUsuarioLogado();
        System.out.printf("👤 Utilizador: %s (%s)\n", usuarioLogado.getNome(), usuarioLogado.getEmail());
        System.out.println("-".repeat(50));
    }

    /**
     * Processa a opção escolhida pelo utilizador no menu principal.
     * @param opcao O número da opção escolhida.
     */
    private void processarOpcaoDoMenuPrincipal(int opcao) {
        switch (opcao) {
            case 1 -> interfaceTarefas.exibirMenuTarefas();
            case 2 -> interfaceVisualizacao.exibirMenuVisualizacao(); // Nome do método ajustado
            case 3 -> interfaceCategorias.exibirMenuCategorias();
            case 4 -> interfaceRelatorios.exibirMenuRelatorios();
            case 5 -> interfacePerfil.exibirMenuPerfil();
            case 6 -> {
                interfaceAutenticacao.realizarLogout();
                // A flag 'executando' continua true, o loop principal irá para a tela de login.
            }
            case 0 -> {
                System.out.println("\nSaindo do sistema...");
                executando = false;
            }
            default -> {
                System.out.println("\n⚠️ Opção inválida. Tente novamente.");
                UtilitariosInterface.pressioneEnterParaContinuar(scanner);
            }
        }
    }
    

}