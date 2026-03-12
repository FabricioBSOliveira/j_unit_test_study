package com.javanauta.cadastrousuario.business;


import com.javanauta.cadastrousuario.api.converter.UsuarioConverter;
import com.javanauta.cadastrousuario.api.converter.UsuarioMapper;
import com.javanauta.cadastrousuario.api.converter.UsuarioUpdateMapper;
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
import com.javanauta.cadastrousuario.infrastructure.exceptions.BusinessException;
import com.javanauta.cadastrousuario.infrastructure.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRespository;

    @Mock
    private  UsuarioConverter usuarioConverter;

    @Mock
    private  UsuarioUpdateMapper usuarioUpdateMapper;

    @Mock
    private  UsuarioMapper usuarioMapper;

    UsuarioEntity usuarioEntity;

    EnderecoEntity enderecoEntity;

    UsuarioRequestDTO usuarioRequestDTO;

    EnderecoRequestDTO enderecoRequestDTO;

    UsuarioResponseDTO usuarioResponseDTO;

    EnderecoResponseDTO enderecoResponseDTO;

    LocalDateTime dataHora;

    String email;

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

        enderecoResponseDTO = EnderecoResponseDTOFixture.build("Rua de Teste",1452L,"Bairro de teste","complemento teste",
                "cidade teste","9875588900");

        usuarioResponseDTO = UsuarioResponseDTOFixture.build(1234L,"Usuario","usuario@email.com","12345568",enderecoResponseDTO);

        email = "usuario@email.com";

    }


    @Test
    public void testLoginSuccess(){
        //Given the user is registered in the System(Given)

        //When the user tries to login (When)

        //Then the System must authorize the user(Then)


    }

    @Test
    void deveSalvarUsuarioComSucesso() {
        when(usuarioRespository.saveAndFlush(usuarioEntity)).thenReturn(usuarioEntity);

        UsuarioEntity entity = usuarioService.salvaUsuario(usuarioEntity);

        assertEquals(entity,usuarioEntity);
        verify(usuarioRespository).saveAndFlush(usuarioEntity);
        verifyNoMoreInteractions(usuarioRespository);
    }

    @Test
    void   deveGravarUsuariosComSucesso() {
        when(usuarioConverter.paraUsuarioEntity(usuarioRequestDTO)).thenReturn(usuarioEntity);
        when(usuarioRespository.saveAndFlush(usuarioEntity)).thenReturn(usuarioEntity);
        when(usuarioMapper.paraUsuarioResponseDTO(usuarioEntity)).thenReturn(usuarioResponseDTO);

        UsuarioResponseDTO dto = usuarioService.gravarUsuarios(usuarioRequestDTO);

        assertEquals(dto,usuarioResponseDTO);
        verify(usuarioConverter).paraUsuarioEntity(usuarioRequestDTO);
        verify(usuarioRespository).saveAndFlush(usuarioEntity);
        verify(usuarioMapper).paraUsuarioResponseDTO(usuarioEntity);
        verifyNoMoreInteractions(usuarioRespository, usuarioConverter, usuarioMapper);
    }

    @Test

    void naoDeveSalvarUsuarioCasoUsuarioRequestDTONull(){
        BusinessException e = assertThrows(BusinessException.class, () -> usuarioService.gravarUsuarios(null));

        assertThat(e, notNullValue());
        assertThat(e.getMessage(), is("Erro ao gravar dados de usuário"));
        assertThat(e.getCause(), notNullValue());
        assertThat(e.getCause().getMessage(), is("Os dados do usuário são obrigatórios"));
        verifyNoInteractions(usuarioMapper,usuarioRespository,usuarioConverter);

    }

    @Test

    void deveGerarExcecaoCasoOcorraErroAoGravarUsuario(){
        when(usuarioConverter.paraUsuarioEntity(usuarioRequestDTO)).thenReturn(usuarioEntity);
        when(usuarioRespository.saveAndFlush(usuarioEntity)).thenThrow(new RuntimeException("Falha ao gravar os dados de usuario"));

        BusinessException e = assertThrows(BusinessException.class, () -> usuarioService.gravarUsuarios(usuarioRequestDTO));

        assertThat(e, notNullValue());
        assertThat(e.getMessage(), is("Erro ao gravar dados de usuário"));
        assertThat(e.getCause().getClass(), is(RuntimeException.class));
        assertThat(e.getCause().getMessage(),is("Falha ao gravar os dados de usuario"));
        verify(usuarioConverter).paraUsuarioEntity(usuarioRequestDTO);
        verify(usuarioRespository).saveAndFlush(usuarioEntity);
        verifyNoInteractions(usuarioMapper);
        verifyNoMoreInteractions(usuarioRespository,usuarioConverter);

    }

    //User Update Tests

    @Test
    void   deveAtualizarCadastroDeUsuariosComSucesso() {
        when(usuarioRespository.findByEmail(email)).thenReturn(usuarioEntity);
        when(usuarioUpdateMapper.updateUsuarioFromDTO(usuarioRequestDTO, usuarioEntity)).thenReturn(usuarioEntity);
        when(usuarioRespository.saveAndFlush(usuarioEntity)).thenReturn(usuarioEntity);
        when(usuarioMapper.paraUsuarioResponseDTO(usuarioEntity)).thenReturn(usuarioResponseDTO);

        UsuarioResponseDTO dto = usuarioService.atualizaCadastro(usuarioRequestDTO);

        assertEquals(dto,usuarioResponseDTO);
        verify(usuarioRespository).findByEmail(email);
        verify(usuarioUpdateMapper).updateUsuarioFromDTO(usuarioRequestDTO,usuarioEntity);
        verify(usuarioRespository).saveAndFlush(usuarioEntity);
        verify(usuarioMapper).paraUsuarioResponseDTO(usuarioEntity);
        verifyNoMoreInteractions(usuarioRespository, usuarioMapper, usuarioUpdateMapper);
    }

    @Test

    void naoDeveAtualizarUsuarioCasoUsuarioRequestDTONull(){
        BusinessException e = assertThrows(BusinessException.class, () -> usuarioService.atualizaCadastro(null));

        assertThat(e, notNullValue());
        assertThat(e.getMessage(), is("Erro ao gravar dados de usuário"));
        assertThat(e.getCause(), notNullValue());
        assertThat(e.getCause().getMessage(), is("Os dados do usuário são obrigatórios"));
        verifyNoInteractions(usuarioMapper,usuarioRespository,usuarioUpdateMapper);

    }

    @Test

    void deveGerarExcecaoCasoOcorraErroAoBuscarUsuario(){
        when(usuarioRespository.findByEmail(email)).thenThrow(new RuntimeException("Falha ao buscar dados de Usuario"));

        BusinessException e = assertThrows(BusinessException.class, () -> usuarioService.atualizaCadastro(usuarioRequestDTO));

        assertThat(e, notNullValue());
        assertThat(e.getMessage(), is("Erro ao gravar dados de usuário"));
        assertThat(e.getCause().getClass(), is(RuntimeException.class));
        assertThat(e.getCause().getMessage(),is("Falha ao buscar dados de Usuario"));
        verify(usuarioRespository).findByEmail(email);
        verifyNoInteractions(usuarioMapper, usuarioUpdateMapper);
        verifyNoMoreInteractions(usuarioRespository);
    }

    //search user data Test

    @Test
    void deveBuscarDadosDeUsuarioComSucesso(){
        when(usuarioRespository.findByEmail(email)).thenReturn(usuarioEntity);
        when(usuarioMapper.paraUsuarioResponseDTO(usuarioEntity)).thenReturn(usuarioResponseDTO);

        UsuarioResponseDTO dto = usuarioService.buscaDadosUsuario(email);

        verify(usuarioRespository).findByEmail(email);
        verify(usuarioMapper).paraUsuarioResponseDTO(usuarioEntity);
        assertEquals(dto,usuarioResponseDTO);

    }

    @Test
    void deveRetornarNuloCasoUsuarioNaoEncontrado(){
        when(usuarioRespository.findByEmail(email)).thenReturn(null);

        UsuarioResponseDTO dto = usuarioService.buscaDadosUsuario(email);

        assertEquals(dto,null);
        verify(usuarioRespository).findByEmail(email);
        verifyNoInteractions(usuarioMapper);


    }

    //Test delete user

    @Test
    void deveDeletarDadosDeUsuarioComSucesso(){
        doNothing().when(usuarioRespository).deleteByEmail(email);

        usuarioService.deletaDadosUsuario(email);

        verify(usuarioRespository).deleteByEmail(email);
    }






}
