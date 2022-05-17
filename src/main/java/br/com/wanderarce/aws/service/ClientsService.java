package br.com.wanderarce.aws.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.wanderarce.aws.model.Clients;
import br.com.wanderarce.aws.repository.ClientsRepository;

@Service
public class ClientsService {

	@Autowired
	private ClientsRepository clientsRepository;
	
	public List<Clients> list() {
		return clientsRepository.findAll();
	}
	
	public Optional<Clients> findBy(Long id) {
		return clientsRepository.findById(id);
	}
	
	public Clients save(Clients client) {
		return clientsRepository.save(client);
	}
	
	public void delete(Long id) {
		clientsRepository.deleteById(id);
	}
	
	
}
