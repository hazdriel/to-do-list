package iu;

import fachada.Gerenciador;
import negocio.entidade.Categoria;
import negocio.entidade.Usuario;
import negocio.excecao.categoria.*;
import negocio.excecao.sessao.SessaoJaInativaException;
import negocio.excecao.tarefa.CriadorVazioException;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Módulo da interface de usuário dedicado ao gerenciamento de categorias.
 * Permite ao utilizador visualizar, criar e remover as suas categorias personalizadas.
 */
public final class InterfaceCategorias {
    
    private final Scanner scanner;
    private final Gerenciador gerenciador;
    
    public InterfaceCategorias(Scanner scanner, Gerenciador gerenciador) {
        this.scanner = scanner;
        this.gerenciador = gerenciador;
    }
    
    /**
     * Exibe o menu de gerenciamento de categorias e o mantém em um loop
     * até que o utilizador decida voltar.
     */
    public void exibirMenuCategorias() {
        boolean executando = true;
        while (executando) {
            UtilitariosInterface.limparTela();
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
            // Pausa a tela após cada ação (exceto ao sair)
            if (executando) {
                UtilitariosInterface.pressioneEnterParaContinuar(scanner);
            }
        }

    }
    
    /**
     * Busca e exibe todas as categorias associadas ao utilizador.
     */
    private void exibirCategoriasExistentes() {
        UtilitariosInterface.limparTela();
        System.out.println("--- CATEGORIAS EXISTENTES ---");
        try {

            List<Categoria> categorias = null;
            categorias = gerenciador.listarCategorias();


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
            System.out.println("\n❌ Erro ao exibir categorias: " + e.getMessage());
        }

    }

        /**
     * Formata e exibe os detalhes de uma única categoria.
     * @param categoria A categoria a ser exibida.
     */
    private void exibirDetalhesCategoria(Categoria categoria) {
        System.out.printf("Nome: %s\n", categoria.getNome());
        if (categoria.isPadrao()) {
            System.out.println("Tipo: 🔒 Padrão do Sistema (não pode ser removida)");
        } else {
            System.out.println("Tipo: 👤 Personalizada");
            System.out.printf("Criador(a): %s\n", categoria.getCriador().getNome());
        }

    }
    
    /**
     * Conduz o processo de criação de uma nova categoria personalizada.
     */
    private void criarNovaCategoria() {
        UtilitariosInterface.limparTela();
        System.out.println("--- CRIAR NOVA CATEGORIA ---");
        try {

            String nome = UtilitariosInterface.lerString(scanner, "Digite o nome da nova categoria: ");

            if (nome.isEmpty()) {
                System.out.println("❌ O nome da categoria não pode ser vazio.");
                return;
            }

            // A lógica de verificação de nome duplicado deve idealmente estar na fachada/negócio.
            // A interface apenas reage ao resultado.

            Categoria novaCategoria = gerenciador.criarCategoria(nome);
            System.out.println("\n✅ Categoria '" + novaCategoria.getNome() + "' criada com sucesso!");

        } catch (CriadorVazioException e) {
            System.out.println("\n❌ Erro ao criar categoria: " + e.getMessage());
        } catch (CategoriaVaziaException e) {
            System.out.println("\n❌ Erro ao criar categoria: " + e.getMessage());
        } catch (SessaoJaInativaException e) {
            System.out.println("\n❌ Erro ao criar categoria: " + e.getMessage());
        }

    }
    
    /**
     * Conduz o processo de remoção de uma categoria personalizada.
     */
    private void removerCategoria() {
        UtilitariosInterface.limparTela();
        System.out.println("--- REMOVER CATEGORIA ---");

        try {
            // Filtra apenas as categorias que o utilizador atual pode remover.

            Usuario utilizadorAtual = gerenciador.getUsuarioLogado();
            List<Categoria> categoriasRemoviveis = gerenciador.listarCategorias().stream()
                    .filter(c -> !c.isPadrao() && c.foiCriadaPor(utilizadorAtual))
                    .collect(Collectors.toList());

            if (categoriasRemoviveis.isEmpty()) {
                System.out.println("\n📭 Nenhuma categoria criada por si para remover.");
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

                // Lógica de confirmação mais segura: apenas "s" ou "sim" confirmam.
                if (List.of("s", "sim").contains(confirmacao)) {
                    if (gerenciador.removerCategoria(categoriaParaRemover.getNome())) {
                        System.out.println("\n✅ Categoria removida com sucesso!");
                    } else {
                        System.out.println("\n❌ Erro: Não foi possível remover a categoria.");
                    }
                } else {
                    System.out.println("\nOperação cancelada.");
                }
            } else {
                System.out.println("❌ Opção inválida.");
            }

        } catch (RepositorioCategoriaRemocaoException | CategoriaNaoPertenceException | CategoriaNaoEncontrada |
                 SessaoJaInativaException | CategoriaVaziaException | CategoriaAtivaRemocaoException e) {
            System.out.println("\n❌ Erro ao remover categoria: " + e.getMessage());
        }
    }

}