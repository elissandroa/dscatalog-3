package br.com.elissandro.DsCatalog.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.elissandro.DsCatalog.entities.Category;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

	@GetMapping
	public ResponseEntity<List<Category>> findAll() {
		List<Category> cat = new ArrayList<>();
		
		cat.add(new Category(1L, "Books"));
		cat.add(new Category(2L, "Electronics"));
		
		return ResponseEntity.ok().body(cat);
	}
}
