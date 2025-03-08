package br.com.elissandro.DsCatalog.tests;

import br.com.elissandro.DsCatalog.dto.ProductDTO;
import br.com.elissandro.DsCatalog.entities.Category;
import br.com.elissandro.DsCatalog.entities.Product;

public class Factory {

	public static Product createProduct() {
		Product product =  new Product(null, "Phone", 1000.0, "Good phone", "img.jpg");
		product.getCategories().add(createCategory());
		return product;
	}
	
	public static ProductDTO createProductDTO() {
		Product product = createProduct();
		return new ProductDTO(product, product.getCategories());
	}

	public static Category createCategory() {
		return new Category(1L, "Electronics");
	}
}
