package org.example;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PrimeiroTeste {

    @Test
    public void estruturaDeUmTeste(){
        //cenário
        int numero1 = 10, numero2 =  5;

        //execução
        int resultado = numero1+numero2;

        //verificações
      Assertions.assertEquals(15, resultado);

    }

}
