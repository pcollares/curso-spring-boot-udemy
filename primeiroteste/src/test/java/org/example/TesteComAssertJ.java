package org.example;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class TesteComAssertJ {

    @Test
    @DisplayName("Deve somar dois numeros")
    public void deveSomar2Numeros(){
        //cenário
        Calculadora calculadora = new Calculadora();
        int numero1 = 10, numero2 =  5;

        //execução
        int resultado = calculadora.somar(numero1, numero2);

        //verificações
        Assertions.assertThat(resultado).isBetween(15,16);
    }

    @Test
    public void naoDeveSomarNumerosNegativos(){
        //cenário
        Calculadora calculadora = new Calculadora();
        int numero1 = -10, numero2 =  -5;

        //execução
        org.junit.jupiter.api.Assertions
                .assertThrows(RuntimeException.class, () -> calculadora.somar(numero1, numero2));
    }

    class Calculadora{
        int somar(int num1, int num2){
            if(num1<0|| num2<0){
                throw new RuntimeException("Numero negativo");
            }
            return  num1+num2;
        }
    }

}
