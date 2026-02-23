package com.javanauta.cadastrousuario.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UsuarioRequestDTOFixture {

    public static UsuarioRequestDTO build(String nome,
                                          String email,
                                          String documento,
                                          EnderecoRequestDTO endereco

    ){
        return new UsuarioRequestDTO(nome, email, documento, endereco);
    }
}
