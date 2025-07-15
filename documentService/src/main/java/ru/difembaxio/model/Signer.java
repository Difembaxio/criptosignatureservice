package ru.difembaxio.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "signers", schema = "public")
public class Signer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  @Column(name = "username")
  private String username;
  @Column(name = "email")
  private String email;
  @Column(name = "firstname")
  private String firstname;
  @NotNull
  @Column(name = "lastname")
  private String lastname;
  @NotNull
  @Column(name = "phone")
  private String phone;
  @NotNull
  @Column(name = "public_key")
  private String public_key;
  @NotNull
  @Column(name = "private_key")
  private String private_key;
  @JsonIgnore
  @OneToMany(mappedBy = "signer", cascade = CascadeType.ALL,orphanRemoval = true)
  private List<Document> documents = new ArrayList<>();


}
