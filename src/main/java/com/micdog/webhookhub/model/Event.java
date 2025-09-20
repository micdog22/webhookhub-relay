package com.micdog.webhookhub.model;
import jakarta.persistence.*; import java.time.Instant;
@Entity @Table(name="events", indexes={@Index(name="idx_topic_time", columnList="topic,createdAt")})
public class Event {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 private String topic; @Lob private String payloadJson; private Instant createdAt=Instant.now();
 public Long getId(){return id;} public void setId(Long id){this.id=id;}
 public String getTopic(){return topic;} public void setTopic(String topic){this.topic=topic;}
 public String getPayloadJson(){return payloadJson;} public void setPayloadJson(String p){this.payloadJson=p;}
 public Instant getCreatedAt(){return createdAt;} public void setCreatedAt(Instant t){this.createdAt=t;}
}
