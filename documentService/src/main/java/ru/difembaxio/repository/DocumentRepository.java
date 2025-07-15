package ru.difembaxio.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.difembaxio.model.Document;


@Repository
public interface DocumentRepository extends JpaRepository<Document,Long> {


}
