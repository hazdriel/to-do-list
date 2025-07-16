## 📝 To-do List em Java

Uma aplicação simples de gerenciamento de tarefas (to-do list), desenvolvida em Java com foco em conceitos de Programação Orientada a Objetos (POO).

---

### 🚀 Funcionalidades

✅ Criar tarefas com título e descrição  
🗓️ Definir prazo e prioridade  
👤 Atribuir um criador (usuário)  
✅ Marcar como concluída  
⚠️ Verificar se a tarefa está atrasada  
📋 Exibir informações completas da tarefa

---

### 📁 Estrutura do Projeto



```bash
To-do-list/
├── model/
│   ├── Prioridade.java
│   ├── Tarefa.java
│   └── Usuario.java
├── repository/
│   ├── TarefaRepository.java
│   └── UsuarioRepository.java
├── service/
│   ├── SessaoService.java
│   ├── TarefaService.java
│   └── UsuarioService.java
├── view/
│   ├── Menu.java
│   └── TarefaView.java
├── Main.java
├── .gitignore
└── README.md

---

### 💡 Observações

- Para testar, use o login: `teste@teste.com` e senha: `teste`
- O projeto não utiliza banco de dados, os dados são mantidos em memória durante a execução.

---

### 📌 Autores

- *Carlos Lucas Feitoza*, *Carlos Gabryel* e *Laura Vitória Mendes*
