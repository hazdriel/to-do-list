package main;

import fachada.Gerenciador;
import iu.InterfacePrincipalRefatorada;

public class Main {
  public static void main(String[] args) {
    
    Gerenciador gerenciador = new Gerenciador();
        InterfacePrincipalRefatorada interfacePrincipal = new InterfacePrincipalRefatorada(gerenciador);
        interfacePrincipal.executar();

  }
}
