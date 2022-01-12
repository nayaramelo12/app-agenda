package io.github.udemy.agendaapi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.Part;

import io.github.udemy.agendaapi.model.Contato;
import io.github.udemy.agendaapi.repository.ContatoRepository;

@RunWith(MockitoJUnitRunner.class) 
@EnableAutoConfiguration
@WebMvcTest(ContatoController.class)
public class ContatoControllerTest {
	
	@MockBean
	ContatoRepository contatoRepository;
	
	@Autowired
	ContatoController contatoController;
	
	 @Autowired
	 MockMvc mvc;

	@Test
	void listarContatosComSucesso() throws Exception {
		Page<Contato> pagina = Page.empty();
		Sort sort = Sort.by(Sort.Direction.ASC,"nome");
				
		when(contatoRepository.findAll(Mockito.any(PageRequest.class))).thenReturn(pagina);
		mvc.perform(get("/api/contatos?page=0&size=10").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		verify(contatoRepository, times(1)).findAll(PageRequest.of(0, 10, sort));
	}
	
	@Test
	void buscarUmContato() throws Exception{
		List<Contato> contato = new ArrayList<Contato>();
		final String nome = "nome";
		
		when(contatoRepository.findByNomeContainingIgnoreCase(Mockito.anyString())).thenReturn(contato);
		mvc.perform(get("/api/contatos/nome").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		verify(contatoRepository, times(1)).findByNomeContainingIgnoreCase(nome);
	}
	
	@Test
	void criarContatoComSucesso() throws Exception{
		Contato contato = Contato.builder().nome("Ana")
				.email("ana@gmail.com")
				.build();
		
		when(contatoRepository.save(contato)).thenReturn(contato);
		mvc.perform(post("/api/contatos", contato)
				.content(convertObjectToJson(contato))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(content().contentType("application/json"))
				.andReturn();
		verify(contatoRepository, times(1)).save(contato);
	}
	
	private String convertObjectToJson(Object obj) throws JsonProcessingException {
	    ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
	    String requestJson= ow.writeValueAsString(obj);
	    
		return requestJson;
	}
	
	@Test
	void deletarContatoComSucesso() throws Exception{		
		doNothing().when(contatoRepository).deleteById(anyInt());
		mvc.perform(delete("/api/contatos/1"))
	        .andExpect(status().isNoContent())
	        .andReturn();
	    verify(contatoRepository, times(1)).deleteById(1);    
	}
	
	@Test
	void favoritarContatoComSucesso() throws Exception{
		Optional<Contato> optionalContato = Optional.of(new Contato(1, "Ana", null, null, null));
		Contato contato = optionalContato.get();
		
		when(contatoRepository.findById(Mockito.anyInt())).thenReturn(optionalContato);
		when(contatoRepository.save(Mockito.any(Contato.class))).thenReturn(contato);
		mvc.perform(patch("/api/contatos/1/favorito")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		
		verify(contatoRepository, times(1)).save(contato);
	}
	
	@Test
	void desfavoritarContatoComSucesso() throws Exception{
		Optional<Contato> optionalContato = Optional.of(new Contato(1, "Ana", null, true, null));
		Contato contato = optionalContato.get();
		
		when(contatoRepository.findById(Mockito.anyInt())).thenReturn(optionalContato);
		when(contatoRepository.save(Mockito.any(Contato.class))).thenReturn(contato);
		mvc.perform(patch("/api/contatos/1/favorito")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		
		verify(contatoRepository, times(1)).save(contato);
	}
	
	@Test
	void adicionarFotoComSucesso() throws Exception {
//		Optional<Contato> optionalContato = Optional.of(new Contato(1, "Ana", null, null, null));
//		Contato contato = optionalContato.get();
//				
//		when(contatoRepository.findById(Mockito.anyInt())).thenReturn(optionalContato);
//		when(contatoRepository.save(Mockito.any(Contato.class))).thenReturn(contato);
//
//		MockMultipartFile multipartFile = new MockMultipartFile("foto", "filename.txt", "text/plain", "foto".getBytes());
//		
//        MockMultipartHttpServletRequestBuilder builder =
//                MockMvcRequestBuilders.multipart("/api/contatos/1/foto");
//                builder.with(request -> {
//                    request.setMethod("PUT");
//                    
//                    return request;
//                });
//
//                
//         MvcResult result = mvc.perform(builder
//        		.file("foto", multipartFile.getBytes())
//        		.file(multipartFile)
//        		.characterEncoding("UTF-8"))
//                .andExpect(status().isOk())
//                .andReturn();
	}
	
}
