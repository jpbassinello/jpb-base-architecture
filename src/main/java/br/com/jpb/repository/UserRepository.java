package br.com.jpb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.jpb.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmailAndActiveIsTrue(String email);

	User findByEmail(String email);

}
