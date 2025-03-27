package br.com.elissandro.DsCatalog.repositories;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.elissandro.DsCatalog.entities.PasswordRecover;

@Repository
public interface PasswordRecoverRepository extends JpaRepository<PasswordRecover, Long> {

	@Query(value = "SELECT obj FROM PasswordRecover obj WHERE obj.token = :token AND obj.expiration > :now")
	List<PasswordRecover> findByToken(String token, Instant now);

}
