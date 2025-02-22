package br.com.elissandro.DsCatalog.dto;

import java.time.Instant;

import br.com.elissandro.DsCatalog.entities.Category;

public class CategoryDTO {

	private Long id;
	private String name;
	private Instant createAt;
	private Instant updateAt;

	public CategoryDTO() {
	}

	public CategoryDTO(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public CategoryDTO(Category entity) {
		id = entity.getId();
		name = entity.getName();
		createAt = entity.getCreatedAt();
		updateAt = entity.getUpdatedAt();
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

	public Instant getCreateAt() {
		return createAt;
	}

	public Instant getUpdateAt() {
		return updateAt;
	}
	
	
}
