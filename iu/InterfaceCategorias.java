package iu;

import fachada.Gerenciador;
import negocio.entidade.Categoria;
import negocio.entidade.Usuario;
import negocio.excecao.sessao.*;
import negocio.excecao.categoria.*;
import negocio.excecao.tarefa.*;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

// Interface para gerenciamento de categorias
public final class InterfaceCategorias {
    
    private final Scanner scanner;
    private final Gerenciador gerenciador;
    
    public InterfaceCategorias(Scanner scanner, Gerenciador gerenciador) {
        this.scanner = scanner;
        this.gerenciador = gerenciador;
    }
    

    public void exibirMenuCategorias() {
        boolean executando = true;
        while (executando) {
            
            System.out.println("--- 📂 GERENCIAR CATEGORIAS ---");
            System.out.println("1 -> Ver Categorias Existentes");
            System.out.println("2 -> Criar Nova Categoria");
            System.out.println("3 -> Remover Categoria");
            System.out.println("0 -> Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            int opcao = UtilitariosInterface.lerInteiro(scanner);

            switch (opcao) {
                case 1 -> exibirCategoriasExistentes();
                case 2 -> criarNovaCategoria();
                case 3 -> removerCategoria();
                case 0 -> {
                    System.out.println("\nVoltando ao menu principal...");
                    executando = false;
                }
                default -> System.out.println("❌ Opção inválida.");
            }
            if (executando) {
                UtilitariosInterface.pressioneEnterParaContinuar(scanner);
            }
        }
    }
    

    private void exibirCategoriasExistentes() {
        
        System.out.println("--- CATEGORIAS EXISTENTES ---");
        
        try {
            List<Categoria> categorias = gerenciador.listarCategorias();
            
            if (categorias == null || categorias.isEmpty()) {
                System.out.println("\n📭 Nenhuma categoria foi criada ainda.");
                return;
            }
            
            for (Categoria categoria : categorias) {
                exibirDetalhesCategoria(categoria);
                System.out.println("-".repeat(40));
            }
        
        System.out.printf("\nTotal: %d categoria(s) encontradas.\n", categorias.size());
        } catch (SessaoJaInativaException e) {
            System.out.println("\n❌ Você precisa estar logado para listar categorias.");
        } catch (Exception e) {
            System.out.println("\n❌ Erro inesperado ao listar categorias: " + e.getMessage());
        }
    }
    
    private void exibirDetalhesCategoria(Categoria categoria) {
        System.out.printf("Nome: %s\n", categoria.getNome());
        if (categoria.isPadrao()) {
            System.out.println("Tipo: 🔒 Padrão do Sistema (não pode ser removida)");
        } else {
            System.out.println("Tipo: 👤 Personalizada");
            System.out.printf("Criador(a): %s\n", categoria.getCriador().getNome());
        }
    }
    

    private void criarNovaCategoria() {
        
        System.out.println("--- CRIAR NOVA CATEGORIA ---");
        
        String nome = UtilitariosInterface.lerString(scanner, "Digite o nome da nova categoria: ");
        
        if (nome.isEmpty()) {
            System.out.println("❌ O nome da categoria não pode ser vazio.");
            return;
        }
        
        try {
            Categoria novaCategoria = gerenciador.criarCategoria(nome);
            System.out.println("\n✅ Categoria '" + novaCategoria.getNome() + "' criada com sucesso!");
        } catch (CategoriaVaziaException e) {
            System.out.println("\n❌ Nome da categoria não pode estar vazio. Tente novamente.");
        } catch (SessaoJaInativaException e) {
            System.out.println("\n❌ Você precisa estar logado para criar categorias.");
        } catch (CriadorVazioException e) {
            System.out.println("\n❌ Erro interno do sistema. Tente novamente.");
        } catch (Exception e) {
            System.out.println("\n❌ Erro inesperado ao criar categoria: " + e.getMessage());
        }
    }
    
    private void removerCategoria() {
        
        System.out.println("--- REMOVER CATEGORIA ---");
        try {
            Usuario utilizadorAtual = gerenciador.getUsuarioLogado();
            List<Categoria> categoriasRemoviveis = gerenciador.listarCategorias().stream()
                    .filter(c -> !c.isPadrao() && c.foiCriadaPor(utilizadorAtual))
                    .collect(Collectors.toList());
        
        if (categoriasRemoviveis.isEmpty()) {
            System.out.println("\n📭 Nenhuma categoria criada para remover.");
            return;
        }
        
        System.out.println("Categorias que você pode remover:");
        for (int i = 0; i < categoriasRemoviveis.size(); i++) {
            System.out.printf("%d -> %s\n", (i + 1), categoriasRemoviveis.get(i).getNome());
        }
        
        System.out.print("\nEscolha a categoria para remover (0 para cancelar): ");
        int opcao = UtilitariosInterface.lerInteiro(scanner);
        
        if (opcao == 0) {
            System.out.println("\nOperação cancelada.");
            return;
        }
        
        if (opcao > 0 && opcao <= categoriasRemoviveis.size()) {
            Categoria categoriaParaRemover = categoriasRemoviveis.get(opcao - 1);
            
            String prompt = String.format("Tem a certeza que deseja remover a categoria '%s'? (s/N): ", categoriaParaRemover.getNome());
            String confirmacao = UtilitariosInterface.lerString(scanner, prompt).toLowerCase();
            if (List.of("s", "sim").contains(confirmacao)) {
                try {
                    gerenciador.removerCategoria(categoriaParaRemover.getNome());
                    System.out.println("\n✅ Categoria removida com sucesso!");
                } catch (CategoriaVaziaException e) {
                    System.out.println("\n❌ Nome da categoria não pode estar vazio.");
                } catch (CategoriaNaoEncontrada e) {
                    System.out.println("\n❌ Categoria não encontrada.");
                } catch (CategoriaNaoPertenceException e) {
                    System.out.println("\n❌ Você não tem permissão para remover esta categoria.");
                } catch (CategoriaAtivaRemocaoException e) {
                    System.out.println("\n❌ Não é possível remover categoria que está sendo usada por tarefas.");
                } catch (SessaoJaInativaException e) {
                    System.out.println("\n❌ Você precisa estar logado para remover categorias.");
                } catch (RepositorioCategoriaRemocaoException e) {
                    System.out.println("\n❌ Erro interno do sistema ao remover categoria.");
                } catch (Exception e) {
                    System.out.println("\n❌ Erro inesperado ao remover categoria: " + e.getMessage());
                }
            } else {
                System.out.println("\nOperação cancelada.");
            }
        } else {
            System.out.println("❌ Opção inválida.");
        }
        } catch (SessaoJaInativaException e) {
            System.out.println("\n❌ Você precisa estar logado para remover categorias.");
        } catch (Exception e) {
            System.out.println("\n❌ Erro inesperado ao remover categoria: " + e.getMessage());
        }
        UtilitariosInterface.pressioneEnterParaContinuar(scanner);
    }
}