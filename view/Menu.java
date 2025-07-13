package view;

import java.util.List;

import model.Usuario;

public class Menu {
  
    public void exibirListaUsuarios(List<Usuario> usuarios) {
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado ainda!");
            return;
        }
        System.out.println("  Lista de Usuários  ");
        for (Usuario usuario : usuarios) {
            System.out.println(usuario);
        }
        System.out.println(" FIM DA LISTA DE USUÁRIOS!! ");
    }

    
}
