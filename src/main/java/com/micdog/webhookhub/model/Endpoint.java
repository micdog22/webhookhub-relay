package com.micdog.webhookhub.model;
import jakarta.persistence.*; import jakarta.validation.constraints.*;
import java.time.Instant;
@Entity @Table(name="endpoints")
public class Endpoint {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @NotBlank private String name; @NotBlank private String url; @NotBlank private String secret; @NotBlank private String topics;
 private boolean active=true; private Instant createdAt=Instant.now(); private Instant updatedAt=Instant.now();
 @PreUpdate public void touch(){ this.updatedAt=Instant.now(); }
 public Long getId(){return id;} public void setId(Long id){this.id=id;}
 public String getName(){return name;} public void setName(String name){this.name=name;}
 public String getUrl(){return url;} public void setUrl(String url){this.url=url;}
 public String getSecret(){return secret;} public void setSecret(String secret){this.secret=secret;}
 public String getTopics(){return topics;} public void setTopics(String topics){this.topics=topics;}
 public boolean isActive(){return active;} public void setActive(boolean active){this.active=active;}
 public Instant getCreatedAt(){return createdAt;} public void setCreatedAt(Instant createdAt){this.createdAt=createdAt;}
 public Instant getUpdatedAt(){return updatedAt;} public void setUpdatedAt(Instant updatedAt){this.updatedAt=updatedAt;}
}
