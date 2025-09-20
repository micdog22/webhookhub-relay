package com.micdog.webhookhub.service;
import com.micdog.webhookhub.model.*; import com.micdog.webhookhub.repo.*; import org.slf4j.*; import org.springframework.beans.factory.annotation.Value; import org.springframework.http.MediaType; import org.springframework.scheduling.annotation.Scheduled; import org.springframework.stereotype.Service;
import java.net.URI; import java.net.http.*; import java.time.*;
import java.util.List;
@Service public class DispatchService {
 private static final Logger log=LoggerFactory.getLogger(DispatchService.class);
 private final EndpointRepository endpoints; private final EventRepository events; private final DeliveryRepository deliveries; private final HttpClient http;
 @Value("${webhook.maxAttempts:6}") private int maxAttempts; @Value("${webhook.backoffSecondsBase:5}") private int baseBackoff;
 public DispatchService(EndpointRepository endpoints, EventRepository events, DeliveryRepository deliveries){ this.endpoints=endpoints; this.events=events; this.deliveries=deliveries; this.http=HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build(); }
 @Scheduled(fixedDelay=1000) public void tick(){ List<Delivery> list=deliveries.findTop50ByStatusAndNextAttemptAtLessThanEqualOrderByNextAttemptAtAsc(Delivery.Status.PENDING, Instant.now()); for(Delivery d:list) deliver(d); }
 public void createDeliveriesForEvent(Event e){ for(Endpoint ep:endpoints.findAll()){ if(!ep.isActive()) continue; if(!TopicMatch.match(ep.getTopics(), e.getTopic())) continue; Delivery d=new Delivery(); d.setEventRef(e); d.setEndpointRef(ep); d.setStatus(Delivery.Status.PENDING); d.setNextAttemptAt(Instant.now()); deliveries.save(d);} }
 public void deliver(Delivery d){
  Event e=d.getEventRef(); Endpoint ep=d.getEndpointRef(); String body=e.getPayloadJson(); long ts=Instant.now().getEpochSecond(); String sig=SignService.hmacSha256Hex(ep.getSecret(), ts+"."+body);
  HttpRequest req=HttpRequest.newBuilder(URI.create(ep.getUrl())).timeout(Duration.ofSeconds(15)).header("Content-Type", MediaType.APPLICATION_JSON_VALUE).header("X-Webhook-Id", String.valueOf(e.getId())).header("X-Webhook-Topic", e.getTopic()).header("X-Webhook-Timestamp", String.valueOf(ts)).header("X-Webhook-Signature", "sha256="+sig).POST(HttpRequest.BodyPublishers.ofString(body)).build();
  Instant t0=Instant.now();
  try{ HttpResponse<Void> resp=http.send(req, HttpResponse.BodyHandlers.discarding()); long dt=Duration.between(t0,Instant.now()).toMillis(); d.setLastDurationMs(dt); d.setLastResponseStatus(resp.statusCode());
       if(resp.statusCode()>=200 && resp.statusCode()<300) d.setStatus(Delivery.Status.SUCCEEDED); else failAndBackoff(d, "HTTP "+resp.statusCode()); }
  catch(Exception ex){ failAndBackoff(d, ex.getClass().getSimpleName()+": "+ex.getMessage()); }
  deliveries.save(d);
 }
 private void failAndBackoff(Delivery d,String err){ int attempt=d.getAttempts()+1; d.setAttempts(attempt); d.setLastError(err); if(attempt>=maxAttempts){ d.setStatus(Delivery.Status.DEAD);} else { d.setStatus(Delivery.Status.PENDING); int seconds=baseBackoff*attempt*attempt; d.setNextAttemptAt(Instant.now().plusSeconds(seconds)); } }
}
