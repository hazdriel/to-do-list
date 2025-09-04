package iu;

import fachada.Gerenciador;
import negocio.entidade.Usuario;
import negocio.excecao.sessao.*;
import java.util.Scanner;

// Coordenador central da interface de usuário
public final class InterfacePrincipalRefatorada {
    
    private final Scanner scanner;
    private final Gerenciador gerenciador;
    private boolean executando;
    
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
        
        this.interfaceAutenticacao = new InterfaceAutenticacao(scanner, gerenciador);
        this.interfaceTarefas = new InterfaceTarefas(scanner, gerenciador);
        this.interfaceVisualizacao = new InterfaceVisualizacao(scanner, gerenciador);
        this.interfaceCategorias = new InterfaceCategorias(scanner, gerenciador);
        this.interfaceRelatorios = new InterfaceRelatorios(scanner, gerenciador);
        this.interfacePerfil = new InterfacePerfil(scanner, gerenciador);
    }
    
    public void executar() {
        while (executando) {
            if (!gerenciador.estaLogado()) {
                boolean continuar = interfaceAutenticacao.exibirTelaLogin();
                if (!continuar) {
                    executando = false; 
                }
            } else {
                exibirMenuPrincipal();
            }
        }

        System.out.println("👋 Obrigado por usar o Lúmina! Até a próxima.");
        scanner.close();
    }
    
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

    private void exibirCabecalhoDoMenu() {
        System.out.println("=".repeat(50));
        System.out.println("              🎯 MENU PRINCIPAL 🎯");
        System.out.println("=".repeat(50));
        try {
            Usuario usuarioLogado = gerenciador.getUsuarioLogado();
            System.out.printf("👤 Usuario: %s (%s)\n", usuarioLogado.getNome(), usuarioLogado.getEmail());
        } catch (SessaoJaInativaException e) {
            System.out.println("❌ Erro ao visualizar menu: " + e.getMessage());
        }
        System.out.println("-".repeat(50));
    }

    
    private void processarOpcaoDoMenuPrincipal(int opcao) {
        switch (opcao) {
            case 1 -> interfaceTarefas.exibirMenuTarefas();
            case 2 -> interfaceVisualizacao.exibirMenuVisualizacao();
            case 3 -> interfaceCategorias.exibirMenuCategorias();
            case 4 -> interfaceRelatorios.exibirMenuRelatorios();
            case 5 -> interfacePerfil.exibirMenuPerfil();
            case 6 -> {
                interfaceAutenticacao.realizarLogout();
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