package com.beto.osworks.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.beto.osworks.api.model.Comentario;
import com.beto.osworks.api.model.ComentarioInput;
import com.beto.osworks.api.model.ComentarioModel;
import com.beto.osworks.domain.exception.EntidadeNaoEncontradaException;
import com.beto.osworks.domain.model.OrdemServico;
import com.beto.osworks.domain.repository.OrdemServicoRepository;
import com.beto.osworks.domain.service.GestaoOrdemServicoService;

@RestController
@RequestMapping("/ordem-servico/{ordemServicoId}/comentarios")
public class ComentarioController {

	@Autowired
	private ModelMapper modelMapper;


	private OrdemServicoRepository ordemServicoRepository;

	@Autowired
	private GestaoOrdemServicoService gestaoOrdemServicoService;

	@GetMapping
	public List<ComentarioModel> buscarComentario(@PathVariable @Valid Long ordemServicoId) {
		
		OrdemServico ordemServico = ordemServicoRepository.findById(ordemServicoId)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Ordem de Serviço não encontrada!"));

		return toCollectionModel(ordemServico.getComentarios());
	}

	private List<ComentarioModel> toCollectionModel(List<Comentario> comentarios) {
		return comentarios.stream().map(comentario -> toModel(comentario)).collect(Collectors.toList());
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ComentarioModel add(@PathVariable Long ordemServicoId, @RequestBody @Valid ComentarioInput comentarioInput) {

		Comentario comentario = gestaoOrdemServicoService.adicionarComentario(ordemServicoId,
				comentarioInput.getDescricao());

		return toModel(comentario);
	}

	private ComentarioModel toModel(Comentario comentario) {
		return modelMapper.map(comentario, ComentarioModel.class);
	}
}
