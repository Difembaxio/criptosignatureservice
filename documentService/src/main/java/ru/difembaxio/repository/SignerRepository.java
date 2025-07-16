package ru.difembaxio.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.difembaxio.model.Signer;

@Repository
public interface SignerRepository extends JpaRepository<Signer, Long> {

    Optional<Signer> findByUsernameIgnoreCase(String username);

}
