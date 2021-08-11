package org.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeuResource {

    @GetMapping("api/clientes/{id}")
    public Cliente obterDadosCliente(@PathVariable Long id){
        Cliente c = new Cliente("Fulano", "123456789");
        return c;
    }

}
