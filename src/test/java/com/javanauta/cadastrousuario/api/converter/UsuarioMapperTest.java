package com.javanauta.cadastrousuario.api.converter;

import com.javanauta.cadastrousuario.api.request.EnderecoRequestDTO;
import com.javanauta.cadastrousuario.api.request.EnderecoRequestDTOFixture;
import com.javanauta.cadastrousuario.api.request.UsuarioRequestDTO;
import com.javanauta.cadastrousuario.api.request.UsuarioRequestDTOFixture;
import com.javanauta.cadastrousuario.api.response.EnderecoResponseDTO;
import com.javanauta.cadastrousuario.api.response.EnderecoResponseDTOFixture;
import com.javanauta.cadastrousuario.api.response.UsuarioResponseDTO;
import com.javanauta.cadastrousuario.api.response.UsuarioResponseDTOFixture;
import com.javanauta.cadastrousuario.infrastructure.entities.EnderecoEntity;
import com.javanauta.cadastrousuario.infrastructure.entities.UsuarioEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class UsuarioMapperTest {

    UsuarioMapper usuarioMapper;


    UsuarioEntity usuarioEntity;

    EnderecoEntity enderecoEntity;

    UsuarioResponseDTO usuarioResponseDTO;

    EnderecoResponseDTO enderecoResponseDTO;

    LocalDateTime dataHora;

    @BeforeEach
    public void setup() {

        usuarioMapper = Mappers.getMapper(UsuarioMapper.class);

        dataHora = LocalDateTime.of(2023,10,05,14,12,15);

        enderecoEntity = EnderecoEntity.builder().rua("Rua de Teste").bairro("Bairro de teste")
                .cep("9875588900").cidade("cidade teste").numero(1452L).complemento("complemento teste").build();

        usuarioEntity = UsuarioEntity.builder().id(1234L).nome("Usuario").documento("12345568")
                .email("usuario@email.com").dataCadastro(dataHora).endereco(enderecoEntity).build();

        enderecoResponseDTO = EnderecoResponseDTOFixture.build("Rua de Teste",1452L,"Bairro de teste","complemento teste",
                "cidade teste","9875588900");

        usuarioResponseDTO = UsuarioResponseDTOFixture.build(1234L,"Usuario","usuario@email.com","12345568",enderecoResponseDTO);



    }

    @Test
    void deveConverterParaUsuarioResponseDTO(){

        UsuarioResponseDTO dto = usuarioMapper.paraUsuarioResponseDTO(usuarioEntity);

        assertEquals(usuarioResponseDTO, dto);

    }

}
