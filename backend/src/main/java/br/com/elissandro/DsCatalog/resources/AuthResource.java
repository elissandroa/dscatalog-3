package br.com.elissandro.DsCatalog.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.elissandro.DsCatalog.dto.EmailDTO;
import br.com.elissandro.DsCatalog.dto.NewPasswordDTO;
import br.com.elissandro.DsCatalog.services.AuthService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {
	
	@Autowired
	private AuthService service;
	
	@PostMapping(value = "/recover-token")
	public ResponseEntity<Void> createRecoverToken(@Valid @RequestBody EmailDTO body){
		service.createRecoverToken(body);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(value = "/new-password")
	public ResponseEntity<Void> saveNewPassword(@Valid @RequestBody NewPasswordDTO body){
		service.saveNewPassword(body);
		return ResponseEntity.noContent().build();
	}
	
}
