package iu;

import fachada.Gerenciador;
import negocio.entidade.Usuario;
import java.util.Scanner;


// Interface para gerenciamento de perfil do usuário
public class InterfacePerfil {
    
    private final Scanner scanner;
    private final Gerenciador gerenciador;
    
    public InterfacePerfil(Scanner scanner, Gerenciador gerenciador) {
        this.scanner = scanner;
        this.gerenciador = gerenciador;
    }
    
    public void exibirMenuPerfil() {
        boolean voltarMenu = false;
        
        while (!voltarMenu) {
            try {
                Usuario usuario = gerenciador.getUsuarioLogado();
                exibirCabecalhoPerfil(usuario);
            exibirOpcoesPerfil();
            
            int opcao = UtilitariosInterface.lerInteiro(scanner);
            
            switch (opcao) {
                case 1 -> alterarSenhaUsuario();
                case 2 -> {
                    if (excluirContaUsuario()) {
                        voltarMenu = true; 
                    }
                }
                case 0 -> voltarMenu = true;
                default -> {
                    System.out.println("⚠️ Opção inválida. Tente novamente.");
                    UtilitariosInterface.pressioneEnterParaContinuar(scanner);
                }
            }
            } catch (Exception e) {
                System.out.println("\n❌ Erro ao acessar perfil: " + e.getMessage());
                voltarMenu = true;
            }
        }
    }
    
    private void exibirCabecalhoPerfil(Usuario usuario) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           👤 PERFIL DO UTILIZADOR 👤");
        System.out.println("=".repeat(50));
        
        System.out.println("ID:    " + usuario.getId());
        System.out.println("Nome:  " + usuario.getNome());
        System.out.println("Email: " + usuario.getEmail());
    }

    private void exibirOpcoesPerfil() {
        System.out.println("⚙️  CONFIGURAÇÕES DA CONTA:");
        System.out.println("1 -> 🔐 Alterar Senha");
        System.out.println("2 -> ❌ Excluir Conta");
        System.out.println("0 -> ⬅️  Voltar ao Menu Principal");
        System.out.println("-".repeat(50));
        
        System.out.print("Escolha uma opção: ");
    }
    
    private void alterarSenhaUsuario() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("           🔐 ALTERAR SENHA 🔐");
        System.out.println("=".repeat(40));
        
        System.out.print("🔒 Senha atual: ");
        String senhaAtual = scanner.nextLine();
        
        System.out.print("🔑 Nova senha: ");
        String novaSenha = scanner.nextLine();
        
        System.out.print("🔑 Confirme a nova senha: ");
        String confirmacaoSenha = scanner.nextLine();
        
        if (!novaSenha.equals(confirmacaoSenha)) {
            System.out.println("❌ As senhas não coincidem!");
            UtilitariosInterface.pressioneEnterParaContinuar(scanner);
            return;
        }
        
        try {
            gerenciador.alterarSenha(senhaAtual, novaSenha);
            System.out.println("✅ Senha alterada com sucesso!");
        } catch (Exception e) {
            System.out.println("❌ Erro ao alterar senha: " + e.getMessage());
        }
        
        UtilitariosInterface.pressioneEnterParaContinuar(scanner);
    }
    
    private boolean excluirContaUsuario() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("           ❌ EXCLUIR CONTA ❌");
        System.out.println("=".repeat(40));
        System.out.println("⚠️  ATENÇÃO: Esta ação é irreversível!");
        System.out.println("⚠️  Todos os seus dados serão perdidos!");
        System.out.println("=".repeat(40));
        
        System.out.print("Tem certeza que deseja excluir sua conta? (sim/não): ");
        String confirmacao = scanner.nextLine().trim().toLowerCase();
        
        if (!confirmacao.equals("sim")) {
            System.out.println("✅ Operação cancelada.");
            UtilitariosInterface.pressioneEnterParaContinuar(scanner);
            return false;
        }
        
        System.out.print("🔒 Digite sua senha para confirmar: ");
        String senhaConfirmacao = scanner.nextLine();
        
        try {
            gerenciador.excluirConta(senhaConfirmacao);
            System.out.println("✅ Conta excluída com sucesso!");
            System.out.println("👋 Você será redirecionado para a tela de login.");
            UtilitariosInterface.pressioneEnterParaContinuar(scanner);
            return true; 
        } catch (Exception e) {
            System.out.println("❌ Erro ao excluir conta: " + e.getMessage());
            UtilitariosInterface.pressioneEnterParaContinuar(scanner);
            return false;
        }
    }
}
