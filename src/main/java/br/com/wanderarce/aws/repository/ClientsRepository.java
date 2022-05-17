package br.com.wanderarce.aws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wanderarce.aws.model.Clients;

@Repository
public interface ClientsRepository extends JpaRepository<Clients, Long>{

	
}
