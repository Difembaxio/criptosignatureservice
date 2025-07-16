package ru.difembaxio.repository;


import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.difembaxio.dto.DocumentDto;
import ru.difembaxio.model.Document;


@Repository
public interface DocumentRepository extends JpaRepository<Document,Long> {
    @Query("SELECT d FROM Document d where d.signer.id = :signerId")
    List<Document> getDocumentListBySigner(@Param("signerId") Long signerId);

    Document getDocumentsBySignerId(Long signerId);

}
