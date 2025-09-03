package iu;

import fachada.Gerenciador;
import negocio.entidade.Usuario;
import negocio.excecao.sessao.*;
import negocio.excecao.usuario.*;
import java.util.Scanner;

// Interface para autenticação de usuários (login, cadastro, logout)
public final class InterfaceAutenticacao {
    
    private final Scanner scanner;
    private final Gerenciador gerenciador;
    
    public InterfaceAutenticacao(Scanner scanner, Gerenciador gerenciador) {
        this.scanner = scanner;
        this.gerenciador = gerenciador;
    }
    
    
    public boolean exibirTelaLogin() {
        while (true) { 
            System.out.println("===================================");
            System.out.println("  BEM-VINDO AO LÚMINA - TO-DO-LIST ");
            System.out.println("===================================");
            System.out.println("1 -> Fazer Login");
            System.out.println("2 -> Cadastrar novo usuário");
            System.out.println("0 -> Sair do Sistema");
            System.out.print("Escolha uma opção: ");
            
            int opcao = UtilitariosInterface.lerInteiro(scanner);
            
            switch (opcao) {
                case 1:
                    realizarLogin();
                    return true; 
                case 2:
                    realizarCadastro();
                    return true; 
                case 0:
                    return false; 
                default:
                    System.out.println("\n❌ Opção inválida. Por favor, tente novamente.");
                    UtilitariosInterface.pressioneEnterParaContinuar(scanner);

            }
        }
    }
    
    private void realizarLogin() {
        
        System.out.println("--- LOGIN DE ACESSO AO SISTEMA ---");
        System.out.print("📧 Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("🔒 Senha: ");
        String senha = scanner.nextLine();
        
        try {
            if (gerenciador.fazerLogin(email, senha)) {
                System.out.println("\n✅ Login realizado com sucesso! Bem-vindo(a).");
            } else {
                System.out.println("\n❌ Falha no login. Email ou senha incorretos.");
            }
        } catch (LoginJaAtivoException e) {
            System.out.println("\n❌ Você já está logado. Faça logout primeiro.");
        } catch (EmailVazioException e) {
            System.out.println("\n❌ Email não pode estar vazio. Tente novamente.");
        } catch (SenhaVaziaException e) {
            System.out.println("\n❌ Senha não pode estar vazia. Tente novamente.");
        } catch (UsuarioVazioException e) {
            System.out.println("\n❌ Erro interno do sistema. Tente novamente.");
        } catch (Exception e) {
            System.out.println("\n❌ Erro inesperado no login: " + e.getMessage());
        }
    }
    

    private void realizarCadastro() {
        UtilitariosInterface.limparTela();
        System.out.println("--- CADASTRO DE NOVO USUÁRIO ---");
        System.out.print("👤 Nome Completo: ");
        String nome = scanner.nextLine().trim();
        System.out.print("📧 Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("🔒 Senha: ");
        String senha = scanner.nextLine();
        
        try {
            gerenciador.cadastrarUsuario(nome, email, senha);
            System.out.println("\n✅ Usuário cadastrado com sucesso! Agora, por favor, faça o login.");
        } catch (NomeVazioException e) {
            System.out.println("\n❌ Nome não pode estar vazio. Tente novamente.");
        } catch (EmailVazioException e) {
            System.out.println("\n❌ Email não pode estar vazio. Tente novamente.");
        } catch (SenhaVaziaException e) {
            System.out.println("\n❌ Senha não pode estar vazia. Tente novamente.");
        } catch (UsuarioExistenteException e) {
            System.out.println("\n❌ Este email já está cadastrado. Tente fazer login ou use outro email.");
        } catch (SenhaTamanhoInvalidoException e) {
            System.out.println("\n❌ Senha deve ter pelo menos 6 caracteres. Tente novamente.");
        } catch (NomeApenasLetrasException e) {
            System.out.println("\n❌ Nome deve conter apenas letras e espaços. Tente novamente.");
        } catch (NomeTamanhoInvalidoException e) {
            System.out.println("\n❌ Nome deve ter entre 2 e 50 caracteres. Tente novamente.");
        } catch (EmailFormatoInvalidoException e) {
            System.out.println("\n❌ Formato de email inválido. Tente novamente.");
        } catch (UsuarioVazioException e) {
            System.out.println("\n❌ Erro interno do sistema. Tente novamente.");
        } catch (Exception e) {
            System.out.println("\n❌ Erro inesperado: " + e.getMessage());
        }
    }
    

    public void realizarLogout() {
        
        System.out.println("--- LOGOUT ---");
        System.out.println("🚪 A sua sessão está sendo encerrada...");
        
        try {
            gerenciador.fazerLogout();
            System.out.println("\n✅ Sessão encerrada com sucesso!");
        } catch (SessaoJaInativaException e) {
            System.out.println("\n❌ Você não está logado. Não há sessão para encerrar.");
        } catch (Exception e) {
            System.out.println("\n❌ Erro inesperado ao encerrar sessão: " + e.getMessage());
        }
        UtilitariosInterface.pressioneEnterParaContinuar(scanner);
    }
    
    public boolean estaLogado() {
        return gerenciador.estaLogado();
    }
    
    public Usuario getUsuarioLogado() {
        try {
            return gerenciador.getUsuarioLogado();
        } catch (SessaoJaInativaException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}