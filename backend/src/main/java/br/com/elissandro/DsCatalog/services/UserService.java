package br.com.elissandro.DsCatalog.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.elissandro.DsCatalog.dto.UserDTO;
import br.com.elissandro.DsCatalog.dto.UserInsertDTO;
import br.com.elissandro.DsCatalog.dto.UserUpdateDTO;
import br.com.elissandro.DsCatalog.entities.Role;
import br.com.elissandro.DsCatalog.entities.User;
import br.com.elissandro.DsCatalog.projections.UserDetailsProjection;
import br.com.elissandro.DsCatalog.repositories.RoleRepository;
import br.com.elissandro.DsCatalog.repositories.UserRepository;
import br.com.elissandro.DsCatalog.services.exceptions.DatabaseException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private RoleRepository roleRepository;
	
	
	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(Pageable pageable) {
		Page<User> list = repository.findAll(pageable);
		return list.map(x -> new UserDTO(x));
	}

	@Transactional(readOnly = true)
	public UserDTO findByID(Long id) {
		Optional<User> obj = repository.findById(id);
		User entity = obj.orElseThrow(() -> new EntityNotFoundException("Entity not found"));
		return new UserDTO(entity);
	}
	
	@Transactional(readOnly = true)
	public UserDTO findMe() {
		User entity = authService.authenticated();
			return new UserDTO(entity);
	}

	@Transactional
	public UserDTO insert(UserInsertDTO dto) {
		User entity = new User();
		entity.setPassword(passwordEncoder.encode(dto.getPassword()));
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new UserDTO(entity);
	}

	@Transactional
	public UserDTO update(Long id, UserUpdateDTO dto) {
		try {
			User entity = repository.getReferenceById(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new UserDTO(entity);
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

	private void copyDtoToEntity(UserDTO dto, User entity) {
		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		entity.setEmail(dto.getEmail());
		entity.getRoles().clear();
		entity.getRoles().addAll(dto.getRoles().stream().map(roleDTO -> roleRepository.getReferenceById(roleDTO.getId()))
				.toList());

	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<UserDetailsProjection> result = repository.searchUserAndRolesByEmail(username);
		if (result.size() == 0) {
			throw new UsernameNotFoundException("Email not found");
		}
		
		User user = new User();
		user.setEmail(result.get(0).getUsername());
		user.setPassword(result.get(0).getPassword());
		for (UserDetailsProjection projection : result) {
			user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
		}
		
		return user;
	}
}
