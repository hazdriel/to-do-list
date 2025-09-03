# 🎯 Lúmina - Sistema de Gerenciamento de Tarefas

Uma aplicação robusta de gerenciamento de tarefas desenvolvida em Java, demonstrando conceitos avançados de **Programação Orientada a Objetos (POO)** com arquitetura em camadas e padrões de design.

---

## 🚀 Funcionalidades Principais

### 📝 Gerenciamento de Tarefas
- ✅ **4 tipos de tarefas**: Simples, Delegável, Recorrente e Temporizada
- 🎯 **Sistema Pomodoro** integrado para tarefas temporizadas
- 🤝 **Delegação de tarefas** entre usuários
- 📊 **Controle de status**: Pendente → Em Progresso → Concluída/Cancelada
- ⏰ **Gestão de prazos** e prioridades
- 🔄 **Tarefas recorrentes** automáticas

### 🔍 Visualização e Filtros
- 📋 **Listagem ordenada** por ID (cronológica)
- 🎯 **Filtros avançados**: por prioridade, status, tipo, categoria
- 🔍 **Busca por ID** de tarefas
- 📈 **Estatísticas detalhadas** de produtividade

### 📊 Relatórios e Análises
- 📄 **Relatórios em PDF** com iText
- 📈 **Análise de produtividade** por período
- 📊 **Estatísticas por status** e categoria
- 📅 **Relatórios temporais** personalizados

### 👤 Gestão de Usuários
- 🔐 **Sistema de autenticação** completo
- 👥 **Múltiplos usuários** com perfis individuais
- 📂 **Categorias personalizadas** por usuário
- 🔒 **Controle de sessão** seguro

---

## 🏗️ Arquitetura do Sistema

### 📁 Estrutura em Camadas

```
┌─────────────────────────────────────────────────────────────┐
│                    🎨 CAMADA DE APRESENTAÇÃO (UI)          │
├─────────────────────────────────────────────────────────────┤
│ • InterfacePrincipalRefatorada  (Coordenador central)      │
│ • InterfaceAutenticacao         (Login/Cadastro)           │
│ • InterfaceTarefas              (CRUD de tarefas)          │
│ • InterfaceVisualizacao         (Filtros e busca)          │
│ • InterfaceCategorias           (Gerenciar categorias)     │
│ • InterfaceRelatorios           (Relatórios e PDF)         │
│ • InterfacePerfil               (Perfil do usuário)        │
│ • UtilitariosInterface          (Utilitários gerais)       │
│ • FormatadorTarefas             (Formatação de exibição)   │
└─────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────┐
│                    🎭 CAMADA DE FACHADA                    │
├─────────────────────────────────────────────────────────────┤
│ • Gerenciador                  (Facade Pattern)             │
└─────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────┐
│                    💼 CAMADA DE NEGÓCIO                    │
├─────────────────────────────────────────────────────────────┤
│ • NegocioUsuario              (Regras de usuário)          │
│ • NegocioTarefa               (Regras de tarefas)          │
│ • NegocioSessao               (Controle de sessão)         │
│ • NegocioCategoria            (Regras de categorias)       │
│ • CalculadoraEstatisticas     (Cálculos estatísticos)      │
│ • ExportadorPDF               (Geração de relatórios)      │
└─────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────┐
│                    💾 CAMADA DE DADOS                      │
├─────────────────────────────────────────────────────────────┤
│ • RepositorioUsuarios         (Persistência de usuários)   │
│ • RepositorioTarefas          (Persistência de tarefas)    │
│ • RepositorioCategorias       (Persistência de categorias) │
│ • PersistenciaArquivo         (I/O de arquivos .dat)       │
└─────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────┐
│                    🏛️ CAMADA DE ENTIDADES                  │
├─────────────────────────────────────────────────────────────┤
│ • TarefaAbstrata              (Classe abstrata base)       │
│ • TarefaSimples               (Tarefa básica)              │
│ • TarefaDelegavel             (Tarefa delegável)           │
│ • TarefaRecorrente            (Tarefa recorrente)          │
│ • TarefaTemporizada           (Tarefa com Pomodoro)        │
│ • Usuario                     (Entidade usuário)           │
│ • Categoria                   (Entidade categoria)         │
│ • Interfaces: Delegavel, Temporizada, Recorrente, etc.     │
└─────────────────────────────────────────────────────────────┘
```

### 📂 Estrutura de Diretórios

```bash
To-do-list/
├── 📁 dados/                    # Camada de persistência
│   ├── RepositorioUsuarios.java
│   ├── RepositorioTarefas.java
│   ├── RepositorioCategorias.java
│   └── PersistenciaArquivo.java
├── 📁 fachada/                  # Camada de fachada
│   └── Gerenciador.java
├── 📁 iu/                       # Camada de interface
│   ├── InterfacePrincipalRefatorada.java
│   ├── InterfaceAutenticacao.java
│   ├── InterfaceTarefas.java
│   ├── InterfaceVisualizacao.java
│   ├── InterfaceCategorias.java
│   ├── InterfaceRelatorios.java
│   ├── InterfacePerfil.java
│   ├── UtilitariosInterface.java
│   ├── FormatadorTarefas.java
│   └── 📁 relatorio/
│       └── FormatadorRelatorio.java
├── 📁 negocio/                  # Camada de negócio
│   ├── NegocioUsuario.java
│   ├── NegocioTarefa.java
│   ├── NegocioSessao.java
│   ├── NegocioCategoria.java
│   ├── CalculadoraEstatisticas.java
│   ├── 📁 entidade/            # Entidades do domínio
│   │   ├── TarefaAbstrata.java
│   │   ├── TarefaSimples.java
│   │   ├── TarefaDelegavel.java
│   │   ├── TarefaRecorrente.java
│   │   ├── TarefaTemporizada.java
│   │   ├── Usuario.java
│   │   ├── Categoria.java
│   │   ├── Delegavel.java
│   │   ├── Temporizada.java
│   │   ├── Recorrente.java
│   │   └── ControleSessao.java
│   ├── 📁 excecao/             # Exceções personalizadas
│   │   ├── 📁 usuario/
│   │   ├── 📁 tarefa/
│   │   ├── 📁 categoria/
│   │   └── 📁 sessao/
│   └── 📁 relatorio/
│       └── ExportadorPDF.java
├── 📁 main/                     # Ponto de entrada
│   └── Main.java
├── 📁 lib/                      # Bibliotecas externas
│   ├── itext-*.jar
│   └── slf4j-*.jar
├── 📁 dados_sistema/            # Arquivos de persistência
│   ├── usuarios.dat
│   ├── tarefas.dat
│   └── categorias.dat
└── README.md
```

---

## 🚀 Como Executar

### 📋 Pré-requisitos
- ☕ **Java 8+** instalado
- 📁 **Terminal/Command Prompt** disponível

### 🔧 Compilação e Execução

```bash
# 1. Navegar para o diretório do projeto
cd To-do-list

# 2. Compilar o projeto
javac -cp ".:lib/*" main/Main.java

# 3. Executar o sistema
java -cp ".:lib/*" main.Main
```

### 👤 Credenciais de Teste
- **Email**: `joao.silva@email.com`
- **Senha**: `joao123`

---

## 🎯 Funcionalidades Avançadas

### ⏰ Sistema Pomodoro
- 🍅 **Sessões cronometradas** de 5min a 2h
- ⏸️ **Pausas configuráveis** entre sessões
- 📊 **Histórico de sessões** detalhado
- 🎯 **Múltiplas sessões** por tarefa

### 🤝 Sistema de Delegação
- 👥 **Múltiplos responsáveis** por tarefa
- 📝 **Histórico de delegações** com motivos
- 🔄 **Transferência de responsabilidade**
- 📊 **Controle de equipes**

### 📊 Relatórios Inteligentes
- 📄 **Exportação em PDF** com iText
- 📈 **Análise de produtividade** temporal
- 🎯 **Estatísticas por categoria** e prioridade
- 📊 **Métricas de conclusão** e atrasos

---

## 📌 Autores

- **Carlos Lucas Feitoza** - Desenvolvimento principal e arquitetura
- **Carlos Gabryel** - Contribuições no design e funcionalidades
- **Cauã Modesto** - Participação no desenvolvimento
- **Laura Vitória Mendes** - Colaboração no projeto
