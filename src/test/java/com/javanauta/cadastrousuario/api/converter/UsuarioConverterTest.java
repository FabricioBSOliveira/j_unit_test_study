package com.javanauta.cadastrousuario.api.converter;

import com.javanauta.cadastrousuario.api.request.EnderecoRequestDTO;
import com.javanauta.cadastrousuario.api.request.EnderecoRequestDTOFixture;
import com.javanauta.cadastrousuario.api.request.UsuarioRequestDTO;
import com.javanauta.cadastrousuario.api.request.UsuarioRequestDTOFixture;
import com.javanauta.cadastrousuario.infrastructure.entities.EnderecoEntity;
import com.javanauta.cadastrousuario.infrastructure.entities.UsuarioEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class UsuarioConverterTest {

    @InjectMocks
    UsuarioConverter usuarioConverter;

    @Mock
            Clock clock;

    UsuarioEntity usuarioEntity;

    EnderecoEntity enderecoEntity;

    UsuarioRequestDTO usuarioRequestDTO;

    EnderecoRequestDTO enderecoRequestDTO;

    LocalDateTime dataHora;

    @BeforeEach
    public void setup() {
        dataHora = LocalDateTime.of(2023,10,05,14,12,15);

        enderecoEntity = EnderecoEntity.builder().rua("Rua de Teste").bairro("Bairro de teste")
                .cep("9875588900").cidade("cidade teste").numero(1452L).complemento("complemento teste").build();

        usuarioEntity = UsuarioEntity.builder().nome("Usuario").documento("12345568")
                .email("usuario@email.com").dataCadastro(dataHora).endereco(enderecoEntity).build();

        enderecoRequestDTO = EnderecoRequestDTOFixture.build("Rua de Teste",1452L,"Bairro de teste","complemento teste",
                "cidade teste","9875588900");

        usuarioRequestDTO = UsuarioRequestDTOFixture.build("Usuario","usuario@email.com","12345568",enderecoRequestDTO);

        ZoneId zoneId = ZoneId.systemDefault();
        Clock fixedClock = Clock.fixed(dataHora.atZone(zoneId).toInstant(),zoneId);
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();


    }

    @Test
    void deveConverterParaUsuarioEntity(){

        UsuarioEntity entity = usuarioConverter.paraUsuarioEntity(usuarioRequestDTO);

        assertEquals(usuarioEntity, entity);

    }

}
