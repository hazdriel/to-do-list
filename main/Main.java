package main;

import fachada.Gerenciador;
import iu.InterfacePrincipalRefatorada;
import negocio.excecao.categoria.CategoriaException;
import negocio.excecao.sessao.SessaoException;
import negocio.excecao.tarefa.TarefaException;
import negocio.excecao.usuario.UsuarioException;

public class Main {
  public static void main(String[] args) throws SessaoException, CategoriaException, UsuarioException, TarefaException {
    
    Gerenciador gerenciador = new Gerenciador();
    InterfacePrincipalRefatorada interfacePrincipal = new InterfacePrincipalRefatorada(gerenciador);
    interfacePrincipal.executar();

  }
}
