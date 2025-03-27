package br.com.elissandro.DsCatalog.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.elissandro.DsCatalog.dto.ProductDTO;
import br.com.elissandro.DsCatalog.entities.Product;
import br.com.elissandro.DsCatalog.projections.ProductProjection;
import br.com.elissandro.DsCatalog.repositories.CategoryRepository;
import br.com.elissandro.DsCatalog.repositories.ProductRepository;
import br.com.elissandro.DsCatalog.services.exceptions.DatabaseException;
import br.com.elissandro.DsCatalog.util.Utils;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(Pageable pageable) {
		Page<Product> list = repository.findAll(pageable);
		return list.map(x -> new ProductDTO(x));
	}

	@Transactional(readOnly = true)
	public ProductDTO findByID(Long id) {
		Optional<Product> obj = repository.findById(id);
		Product entity = obj.orElseThrow(() -> new EntityNotFoundException("Entity not found"));
		return new ProductDTO(entity, entity.getCategories());
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new ProductDTO(entity, entity.getCategories());
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product entity = repository.getReferenceById(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new ProductDTO(entity, entity.getCategories());
		} catch (EntityNotFoundException e) {
			throw new EntityNotFoundException("Id not found " + id);
		}
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {

		if (!repository.existsById(id)) {
			throw new EntityNotFoundException("Id not found " + id);
		} else {
			try {
				repository.deleteById(id);
			} catch (DataIntegrityViolationException e) {
				throw new DatabaseException("");
			}
		}
	}

	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		entity.setName(dto.getName());
		entity.setPrice(dto.getPrice());
		entity.setDescription(dto.getDescription());
		entity.setImgUrl(dto.getImgUrl());
		entity.setDate(dto.getDate());

		entity.getCategories().clear();
		dto.getCategories().forEach(catDto -> {
			entity.getCategories().add(categoryRepository.getReferenceById(catDto.getId()));
		});
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(String name, String categoryId, Pageable pageable) {
		List<Long> categoryIds = Arrays.asList();
		if (!"0".equals(categoryId)) {
			categoryIds = Arrays.asList(categoryId.split(",")).stream().map(x -> Long.parseLong(x))
					.collect(Collectors.toList());
		}
		Page<ProductProjection> page = repository.searchProducts(categoryIds, name, pageable);

		List<Long> productIds = page.stream().map(x -> x.getId()).collect(Collectors.toList());
		List<Product> entities = repository.searchProductsWithCategories(productIds);
		
		entities = (List<Product>) Utils.replace(page.getContent(), entities);
		
		List<ProductDTO> dtos = entities.stream().map(p -> new ProductDTO(p, p.getCategories()))
				.collect(Collectors.toList());

		return new PageImpl<>(dtos, page.getPageable(), page.getTotalElements());
	}
}
