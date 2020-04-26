package com.beto.osworks.domain.service;

import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beto.osworks.api.model.Comentario;
import com.beto.osworks.domain.exception.EntidadeNaoEncontradaException;
import com.beto.osworks.domain.exception.Negocioexception;
import com.beto.osworks.domain.model.Cliente;
import com.beto.osworks.domain.model.OrdemServico;
import com.beto.osworks.domain.model.StatusOrdemServico;
import com.beto.osworks.domain.repository.ClienteRepository;
import com.beto.osworks.domain.repository.ComentarioRepository;
import com.beto.osworks.domain.repository.OrdemServicoRepository;

@Service
public class GestaoOrdemServicoService {

	@Autowired
	private OrdemServicoRepository ordemServicoRepository;

	@Autowired
	private ComentarioRepository comentarioRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	public OrdemServico criar(OrdemServico ordemservico) {
		Cliente cliente = clienteRepository.findById(ordemservico.getCliente().getId())
				.orElseThrow(() -> new Negocioexception("Cliente não encontrado!"));

		ordemservico.setCliente(cliente);
		ordemservico.setStatus(StatusOrdemServico.ABERTA);
		ordemservico.setDataAbertura(OffsetDateTime.now());
		return ordemServicoRepository.save(ordemservico);
	}

	public void finalizar(Long ordemServicoId) {
		OrdemServico ordemServico = buscar(ordemServicoId);

		ordemServico.finalizar();
		ordemServicoRepository.save(ordemServico);
	}

	private OrdemServico buscar(Long ordemServicoId) {
		return ordemServicoRepository.findById(ordemServicoId)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Ordem de Serviço não encontrada"));
	}

	public Comentario adicionarComentario(Long ordemServicoID, String descricao) {
		OrdemServico ordemServico = buscar(ordemServicoID);

		Comentario comentario = new Comentario();
		comentario.setDataEnvio(OffsetDateTime.now());
		comentario.setDescricao(descricao);
		comentario.setOrdemServico(ordemServico);

		return comentarioRepository.save(comentario);
	}
}