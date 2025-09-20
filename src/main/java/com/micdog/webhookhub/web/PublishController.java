package com.micdog.webhookhub.web;
import com.fasterxml.jackson.databind.ObjectMapper; import com.micdog.webhookhub.model.Event; import com.micdog.webhookhub.repo.EventRepository; import com.micdog.webhookhub.service.DispatchService;
import org.springframework.http.ResponseEntity; import org.springframework.web.bind.annotation.*; import java.util.Map;
@RestController
public class PublishController {
 private final EventRepository events; private final DispatchService dispatcher; private final ObjectMapper mapper=new ObjectMapper();
 public PublishController(EventRepository e, DispatchService d){ this.events=e; this.dispatcher=d; }
 public static record PublishReq(String topic, Map<String,Object> payload){}
 @PostMapping("/publish") public ResponseEntity<?> publish(@RequestBody PublishReq req) throws Exception {
   Event e=new Event(); e.setTopic(req.topic()); e.setPayloadJson(mapper.writeValueAsString(req.payload()==null? Map.of(): req.payload())); e=events.save(e); dispatcher.createDeliveriesForEvent(e); return ResponseEntity.ok(Map.of("eventId", e.getId())); }
 @PostMapping("/_inbox") public Map<String,Object> inbox(@RequestBody(required=false) Map<String,Object> body){ return Map.of("ok", true, "echo", body); }
}
