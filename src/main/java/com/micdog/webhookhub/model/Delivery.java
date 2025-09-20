package com.micdog.webhookhub.model;
import jakarta.persistence.*; import java.time.Instant;
@Entity @Table(name="deliveries", indexes={@Index(name="idx_status_next", columnList="status,nextAttemptAt")})
public class Delivery {
 public enum Status { PENDING, SUCCEEDED, FAILED, DEAD }
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false) private Event eventRef; @ManyToOne(optional=false) private Endpoint endpointRef;
 @Enumerated(EnumType.STRING) private Status status=Status.PENDING; private int attempts=0; private Instant nextAttemptAt=Instant.now();
 private Integer lastResponseStatus; private Long lastDurationMs; @Lob private String lastError;
 private Instant createdAt=Instant.now(); private Instant updatedAt=Instant.now();
 @PreUpdate public void touch(){ this.updatedAt=Instant.now(); }
 public Long getId(){return id;} public void setId(Long id){this.id=id;}
 public Event getEventRef(){return eventRef;} public void setEventRef(Event e){this.eventRef=e;}
 public Endpoint getEndpointRef(){return endpointRef;} public void setEndpointRef(Endpoint e){this.endpointRef=e;}
 public Status getStatus(){return status;} public void setStatus(Status s){this.status=s;}
 public int getAttempts(){return attempts;} public void setAttempts(int a){this.attempts=a;}
 public Instant getNextAttemptAt(){return nextAttemptAt;} public void setNextAttemptAt(Instant n){this.nextAttemptAt=n;}
 public Integer getLastResponseStatus(){return lastResponseStatus;} public void setLastResponseStatus(Integer s){this.lastResponseStatus=s;}
 public Long getLastDurationMs(){return lastDurationMs;} public void setLastDurationMs(Long d){this.lastDurationMs=d;}
 public String getLastError(){return lastError;} public void setLastError(String e){this.lastError=e;}
 public Instant getCreatedAt(){return createdAt;} public void setCreatedAt(Instant t){this.createdAt=t;}
 public Instant getUpdatedAt(){return updatedAt;} public void setUpdatedAt(Instant t){this.updatedAt=t;}
}
