package br.com.elissandro.DsCatalog.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import br.com.elissandro.DsCatalog.entities.Category;
import br.com.elissandro.DsCatalog.entities.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ProductDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	@Size(min = 5, max = 60, message = "O nome do produto deve ter entre 5 e 60 caracteres")
	@NotBlank(message = "Campo requerido")
	private String name;
	@Positive(message = "O preço deve ser um valor positivo")
	private Double price;
	@NotBlank(message = "Campo requerido")
	private String description;
	private String imgUrl;
	@PastOrPresent(message = "A data do produto não pode ser futura")
	private Instant date;
	
	private List<CategoryDTO> categories = new ArrayList<>();
	
	public ProductDTO() {
	}

	public ProductDTO(Long id, String name, Double price, String description, String imgUrl, Instant date) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.description = description;
		this.imgUrl = imgUrl;
		this.date = date;
	}
	
	public ProductDTO(Product entity) {
		id = entity.getId();
		name = entity.getName();
		price = entity.getPrice();
		description = entity.getDescription();
		imgUrl = entity.getImgUrl();
		date = entity.getDate();
	}
	
	public ProductDTO(Product entity, Set<Category> categories) {
		this(entity);
		categories.forEach(cat -> this.categories.add(new CategoryDTO(cat)));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}
	
	public List<CategoryDTO> getCategories() {
		return categories;
	}
}
