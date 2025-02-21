package br.com.elissandro.DsCatalog.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.elissandro.DsCatalog.dto.CategoryDTO;
import br.com.elissandro.DsCatalog.entities.Category;
import br.com.elissandro.DsCatalog.repositories.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {
	 List<Category> list = repository.findAll();
	 return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());

	}
}
