package com.beto.osworks.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beto.osworks.domain.exception.Negocioexception;
import com.beto.osworks.domain.model.Cliente;
import com.beto.osworks.domain.repository.ClienteRepository;

@Service
public class CrudClienteService {
	@Autowired
	private ClienteRepository clienteRepository;

	public Cliente salvar(Cliente cliente) {
		Cliente clienteExist = clienteRepository.findByEmail(cliente.getEmail());

		if (clienteExist != null && !clienteExist.equals(cliente)) {
			throw new Negocioexception("E-mail informado já esta cadastrado no Sistema! ");
		}
		
		return clienteRepository.save(cliente);
	}

	public void deletar(Long clienteId) {
		clienteRepository.deleteById(clienteId);
	}
}
