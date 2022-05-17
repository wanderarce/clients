package br.com.wanderarce.aws.resources;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.hateoas.server.TypedEntityLinks;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.wanderarce.aws.model.Clients;
import br.com.wanderarce.aws.service.ClientsService;

@RestController
@ExposesResourceFor(Clients.class)
@RequestMapping(value = "/clients")
public class ClientsResource {

	@Autowired
	private ClientsService clientsService;

	public ClientsResource() {

	}

	@GetMapping
	public ResponseEntity<List<Clients>> clients() {
		List<Clients> clients = clientsService.list();
		clients.forEach((client) -> {
			client = this.setLinks(client);
		});
		return new ResponseEntity(clients, HttpStatus.OK);
	}

	@GetMapping(path = "/{id}")
	public ResponseEntity<Clients> client(@PathVariable("id") Long id) {
		Optional<Clients> client = clientsService.findBy(id);
		if (client.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			Clients response = this.setLinks(client.get());
			return new ResponseEntity<Clients>(response, HttpStatus.OK);

		}
	}

	@PostMapping
	public ResponseEntity<Clients> create(@RequestBody Clients request) {
		Clients client = clientsService.save(request);
		client.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).update(client.getId(), new Clients()))
				.withRel("update"));

		return new ResponseEntity(client, HttpStatus.CREATED);
	}

	@PutMapping(path = "/{id}")
	public ResponseEntity<Clients> update(@PathVariable("id") Long id, @RequestBody Clients client) {
		 Clients response = clientsService.save(client);
		this.setLinks(response);
		return new ResponseEntity<Clients>(response, HttpStatus.OK);
	}

	@PatchMapping(path = "/{id}")
	public ResponseEntity<Clients> partialUpdate(@PathVariable("id") Long id, @RequestBody Map<Object, Object> request) {
		Optional<Clients> client = clientsService.findBy(id);
		if (client.isPresent()) {
			request.forEach((key, value) -> {
				Field field = ReflectionUtils.findField(Clients.class, (String) key);
				field.setAccessible(true);
				ReflectionUtils.setField(field, client.get(), value);
			});
			Clients c = clientsService.save(client.get());
			this.setLinks(c);
			return new ResponseEntity<Clients>(c, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@DeleteMapping(path = "/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		Optional<Clients> search = clientsService.findBy(id);
		if (search.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		clientsService.delete(id);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	private Clients setLinks(Clients response) {
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(ClientsResource.class).update(response.getId(), new Clients()))
				.withRel("update"));
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClientsResource.class).create(new Clients()))
				.withRel("create"));
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(ClientsResource.class).client(response.getId())).withRel("details"));
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClientsResource.class).clients())
				.withRel("clients"));
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(ClientsResource.class).delete(response.getId())).withRel("delete"));
		return response;
	}

}
