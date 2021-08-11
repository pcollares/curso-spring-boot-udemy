package org.example;

import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

//@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class MokitoTests {

    @Mock
    List<String> lista;

    @Test
    public void deveRetornarTamanhoLista(){
        Mockito.when(lista.size()).thenReturn(20);

        int size = lista.size();

        Assertions.assertThat(size).isEqualTo(20);
    }

    @Test
    public void deveCharmarMetodosEmOrdem(){

        lista.size();
        lista.add("");

        InOrder inOrder= Mockito.inOrder(lista);
        inOrder.verify(lista).size();
        inOrder.verify(lista).add("");
    }

    @Test
    public void deveCharmarUmMetodo(){
        lista.clear();

        Mockito.verify(lista).clear();
    }

    @Test
    public void deveCharmarUmMetodoTantasVezes(){
        lista.clear();
        lista.clear();

        Mockito.verify(lista, Mockito.times(2)).clear();
    }

    @Test
    public void naoDeveCharmarUmMetodo(){
        lista.size();

        Mockito.verify(lista, Mockito.never()).clear();
    }


}
