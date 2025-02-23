package br.com.elissandro.DsCatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.elissandro.DsCatalog.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
