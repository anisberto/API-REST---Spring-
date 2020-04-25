package com.beto.osworks.api.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.beto.osworks.domain.model.Cliente;
import com.beto.osworks.domain.repository.ClienteRepository;
import com.beto.osworks.domain.service.CrudClienteService;

@RestController
@RequestMapping("/clientes")
public class Clientecontroller {
	@Autowired
	private ClienteRepository clienteRepositoty;

	@Autowired
	private CrudClienteService cadastroclienteService;

	@GetMapping
	public List<Cliente> listarCliente() {
		return clienteRepositoty.findAll();
	}

	@GetMapping("/{clienteId}")
	public ResponseEntity<Cliente> buscarId(@PathVariable Long clienteId) {
		Optional<Cliente> cliente = clienteRepositoty.findById(clienteId);

		if (cliente.isPresent()) {
			return ResponseEntity.ok(cliente.get());
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public Cliente adcionarCliente(@Valid @RequestBody Cliente cliente) {
		return cadastroclienteService.salvar(cliente);
	}

	@PutMapping("/{clienteId}")
	public ResponseEntity<Cliente> update(@PathVariable Long clienteId, @Valid @RequestBody Cliente cliente) {

		if (!clienteRepositoty.existsById(clienteId)) {
			return ResponseEntity.notFound().build();
		}

		cliente.setId(clienteId);
		cliente = cadastroclienteService.salvar(cliente);

		return ResponseEntity.ok(cliente);
	}

	@DeleteMapping("/{clienteId}")
	public ResponseEntity<Void> delete(@PathVariable Long clienteId) {

		if (!clienteRepositoty.existsById(clienteId)) {
			return ResponseEntity.notFound().build();
		}
		cadastroclienteService.deletar(clienteId);

		return ResponseEntity.noContent().build();
	}
}
