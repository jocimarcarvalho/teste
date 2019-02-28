package com.lexter.comercial.controller;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.lexter.comercial.model.Oportunidade;
import com.lexter.comercial.repository.OportunidadeRepository;

@RestController
@RequestMapping("/oportunidades")
@CrossOrigin(origins="localhost:4200/")
public class OportunidadesController {

	@Autowired
	private OportunidadeRepository opr;
	
	@GetMapping
	public List<Oportunidade> listar() {
		/*
		 * List<Oportunidade> lista = new ArrayList<>(); lista = opr.findAll();
		 */
		return opr.findAll();
	}
	
	@GetMapping("/{id}")
	private ResponseEntity<Oportunidade> listarPorId(@PathVariable Long id) {
		Optional<Oportunidade> oportunidade = opr.findById(id);
		
		//A anotação PathVariable faz a ligação do id que está vindo da ur com o id do método
		if(oportunidade.isPresent()) {
			
			return ResponseEntity.ok(oportunidade.get());
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	private Oportunidade adicionar(@Valid @RequestBody Oportunidade oportunidade) {
		Optional<Oportunidade>oportunidadeExistente = opr.findByDescricaoAndNomeProspecto(
				oportunidade.getDescricao(), oportunidade.getNomeProspecto());
		if(oportunidadeExistente.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
					"Já existe um registro com esse nome ou esta descrição!");
		}
		return opr.save(oportunidade);
		
		//a anotação @RequestBody transforma o json que está vindo da url em um objeto do tipo Oportunidade
	}
	
	@DeleteMapping
	private String excluir(@RequestBody Oportunidade oportunidade) {
		String resposta = null;
		try {
			opr.delete(oportunidade);
			resposta = "Registro excluído com sucesso";
		}catch(Exception ex) {
			resposta = "Não foi possível excluir o registro: "+ ex;
		}
		return resposta;
	}
	
	@PutMapping
	@ResponseStatus(HttpStatus.ACCEPTED)
	private Oportunidade atualizar(@Valid @RequestBody Oportunidade oportunidade) {
		Optional<Oportunidade>oportunidadeExistente = opr.findByDescricaoAndNomeProspecto(
				oportunidade.getDescricao(), oportunidade.getNomeProspecto());
		if(oportunidadeExistente.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
					"Já existe um registro com esse nome ou esta descrição!");
		}
		return opr.save(oportunidade);
		
		//a anotação @RequestBody transforma o json que está vindo da url em um objeto do tipo Oportunidade
	}
}
