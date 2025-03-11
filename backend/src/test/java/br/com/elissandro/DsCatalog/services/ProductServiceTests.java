package br.com.elissandro.DsCatalog.services;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.elissandro.DsCatalog.dto.ProductDTO;
import br.com.elissandro.DsCatalog.entities.Category;
import br.com.elissandro.DsCatalog.entities.Product;
import br.com.elissandro.DsCatalog.repositories.CategoryRepository;
import br.com.elissandro.DsCatalog.repositories.ProductRepository;
import br.com.elissandro.DsCatalog.tests.Factory;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;

	@Mock
	private ProductRepository repository;
	
	@Mock
	private CategoryRepository categoryRepository;

	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	private Product product;
	private PageImpl<Product> Page;
	private ProductDTO productDTO;
	private Category category;
	
	

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		product = Factory.createProduct();
		Page = new PageImpl<>(List.of(product));
		productDTO = Factory.createProductDTO();
		category = Factory.createCategory();
		
		when(repository.save(ArgumentMatchers.any())).thenReturn(product);

		when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(Page);
	
		when(repository.getReferenceById(existingId)).thenReturn(product);
		when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);	
		
		when(categoryRepository.getReferenceById(existingId)).thenReturn(category);
		when(categoryRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		when(repository.findById(existingId)).thenReturn(Optional.of(product));
		when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		when(repository.existsById(existingId)).thenReturn(true);
		doThrow(EntityNotFoundException.class).when(repository).deleteById(nonExistingId);
		when(repository.existsById(dependentId)).thenThrow(DataIntegrityViolationException.class);

	}
	
	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() {
		ProductDTO result = service.findByID(existingId);
		
		Assertions.assertNotNull(result);
		verify(repository, times(1)).findById(existingId);
	}
	
	@Test
	public void findByIdShouldThrowEntityNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(EntityNotFoundException.class, () -> {
			service.findByID(nonExistingId);
			});
	}
	
	@Test
	public void updateShouldReturnProductDTOWhenIdExists() {
		ProductDTO result = service.update(existingId, productDTO);
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void updateShouldThrowEntityNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(EntityNotFoundException.class, () -> {
			service.update(nonExistingId, productDTO);
			});
	}
	
	@Test
	public void findAllPagedShouldReturnPage() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<ProductDTO> result = service.findAllPaged(pageable);
		
		Assertions.assertNotNull(result);
		verify(repository, times(1)).findAll(pageable);
	}	

	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});

		verify(repository, times(1)).deleteById(existingId);
	}

	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(EntityNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});
	}

	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentIdExists() {
		Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
			service.delete(dependentId);
		});
	}

}
