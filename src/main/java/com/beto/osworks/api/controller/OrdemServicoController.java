package com.beto.osworks.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.beto.osworks.api.model.OrdemServicoInputModel;
import com.beto.osworks.api.model.OrdemServicoModel;
import com.beto.osworks.domain.model.OrdemServico;
import com.beto.osworks.domain.repository.OrdemServicoRepository;
import com.beto.osworks.domain.service.GestaoOrdemServicoService;

@RestController
@RequestMapping("/ordens-servico")
public class OrdemServicoController {

	@Autowired
	private GestaoOrdemServicoService gestaoOrdemServicoService;

	@Autowired
	private OrdemServicoRepository ordemServicoRepository;

	@Autowired
	private ModelMapper modelMapper;

	@GetMapping
	public List<OrdemServicoModel> buscarOrdens() {
		return toCollectionModel(ordemServicoRepository.findAll());
	}

	@GetMapping("/{ordemID}")
	public ResponseEntity<OrdemServicoModel> buscarOrdemId(@PathVariable Long ordemID) {

		Optional<OrdemServico> ordemServico = ordemServicoRepository.findById(ordemID);

		if (ordemServico.isPresent()) {
			OrdemServicoModel modelOrdem = toOrdemServicoModel(ordemServico.get());
			return ResponseEntity.ok(modelOrdem);
		}
		return ResponseEntity.notFound().build();
	}

	@PutMapping("/{ordemServicoId}/finalizacao")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void finalizar(@PathVariable Long ordemServicoId) {
		gestaoOrdemServicoService.finalizar(ordemServicoId);
	}

	private OrdemServicoModel toOrdemServicoModel(OrdemServico ordemServico) {
		return modelMapper.map(ordemServico, OrdemServicoModel.class);
	}

	private List<OrdemServicoModel> toCollectionModel(List<OrdemServico> ordensServico) {

		List<OrdemServicoModel> ordensTolistModel = new ArrayList<>();
		for (OrdemServico ordemList : ordensServico) {
			ordensTolistModel.add(toOrdemServicoModel(ordemList));
		}

		return ordensTolistModel;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public OrdemServicoModel criarOrdem(@Valid @RequestBody OrdemServicoInputModel ordemServicoInput) {
		OrdemServico ordemServico = toEntity(ordemServicoInput);

		return toOrdemServicoModel(gestaoOrdemServicoService.criar(ordemServico));
	}

	private OrdemServico toEntity(OrdemServicoInputModel ordemServico) {
		return modelMapper.map(ordemServico, OrdemServico.class);
	}
}
