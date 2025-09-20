package com.micdog.webhookhub.web;
import com.micdog.webhookhub.model.*; import com.micdog.webhookhub.repo.*; import com.micdog.webhookhub.service.DispatchService;
import org.springframework.http.ResponseEntity; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api")
public class AdminController {
 private final EndpointRepository endpoints; private final EventRepository events; private final DeliveryRepository deliveries; private final DispatchService dispatcher;
 public AdminController(EndpointRepository e, EventRepository ev, DeliveryRepository d, DispatchService x){ this.endpoints=e; this.events=ev; this.deliveries=d; this.dispatcher=x; }
 @GetMapping("/endpoints") public List<Endpoint> listEndpoints(){ return endpoints.findAll(); }
 @PostMapping("/endpoints") public Endpoint createEndpoint(@RequestBody Endpoint e){ return endpoints.save(e); }
 @PutMapping("/endpoints/{id}") public ResponseEntity<Endpoint> updateEndpoint(@PathVariable Long id, @RequestBody Endpoint patch){
   return endpoints.findById(id).map(e->{ e.setName(patch.getName()); e.setUrl(patch.getUrl()); e.setSecret(patch.getSecret()); e.setTopics(patch.getTopics()); e.setActive(patch.isActive()); return ResponseEntity.ok(endpoints.save(e)); }).orElse(ResponseEntity.notFound().build()); }
 @DeleteMapping("/endpoints/{id}") public ResponseEntity<?> deleteEndpoint(@PathVariable Long id){ return endpoints.findById(id).map(e->{ endpoints.delete(e); return ResponseEntity.noContent().build(); }).orElse(ResponseEntity.notFound().build()); }
 @GetMapping("/events") public List<Event> listEvents(){ return events.findAll(); }
 @PostMapping("/events/{id}/replay") public ResponseEntity<?> replay(@PathVariable Long id){ return events.findById(id).map(e->{ dispatcher.createDeliveriesForEvent(e); return ResponseEntity.ok().build(); }).orElse(ResponseEntity.notFound().build()); }
 @GetMapping("/deliveries") public List<Delivery> deliveries(@RequestParam(required=false) Delivery.Status status){ return status==null? deliveries.findAll(): deliveries.findAll().stream().filter(d->d.getStatus()==status).toList(); }
}
