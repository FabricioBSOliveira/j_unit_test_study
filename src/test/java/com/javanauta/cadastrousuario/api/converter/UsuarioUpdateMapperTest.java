package com.javanauta.cadastrousuario.api.converter;

import com.javanauta.cadastrousuario.api.request.EnderecoRequestDTO;
import com.javanauta.cadastrousuario.api.request.EnderecoRequestDTOFixture;
import com.javanauta.cadastrousuario.api.request.UsuarioRequestDTO;
import com.javanauta.cadastrousuario.api.request.UsuarioRequestDTOFixture;
import com.javanauta.cadastrousuario.api.response.UsuarioResponseDTO;
import com.javanauta.cadastrousuario.infrastructure.entities.EnderecoEntity;
import com.javanauta.cadastrousuario.infrastructure.entities.UsuarioEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UsuarioUpdateMapperTest {

    UsuarioUpdateMapper usuarioMapper;

    UsuarioEntity usuarioEntityEsperado;

    UsuarioEntity usuarioEntity;

    EnderecoEntity enderecoEntity;

    UsuarioRequestDTO usuarioRequestDTO;

    EnderecoRequestDTO enderecoRequestDTO;

    LocalDateTime dataHora;

    @BeforeEach
    public void setup() {

        usuarioMapper = Mappers.getMapper(UsuarioUpdateMapper.class);

        dataHora = LocalDateTime.of(2023,10,05,14,12,15);

        enderecoEntity = EnderecoEntity.builder().rua("Rua de Teste").bairro("Bairro de teste")
                .cep("9875588900").cidade("cidade teste").numero(1452L).complemento("complemento teste").build();

        usuarioEntity = UsuarioEntity.builder().id(1234L).nome("Usuario").documento("12345568")
                .email("usuario@email.com").dataCadastro(dataHora).endereco(enderecoEntity).build();

        enderecoRequestDTO = EnderecoRequestDTOFixture.build("Rua de Teste",1452L,"Bairro de teste","complemento teste",
                "cidade teste","9875588900");

        usuarioRequestDTO = UsuarioRequestDTOFixture.build("Usuario Teste",null,"123457568",enderecoRequestDTO);

        usuarioEntityEsperado = UsuarioEntity.builder().id(1234L).nome("Usuario Teste").email("usuario@email.com")
                .documento("123457568").dataCadastro(dataHora).endereco(enderecoEntity).build();



    }

    @Test
    void deveConverterParaUsuarioResponseDTO(){

        UsuarioEntity entity = usuarioMapper.updateUsuarioFromDTO(usuarioRequestDTO, usuarioEntity);

        assertEquals(usuarioEntityEsperado, entity);

    }

}
