package br.com.elissandro.DsCatalog.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.elissandro.DsCatalog.entities.Category;
import br.com.elissandro.DsCatalog.repositories.CategoryRepository;
import jakarta.transaction.Transactional;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;
	
	@Transactional
	public List<Category> findAll() {
		return repository.findAll();
	}
}
