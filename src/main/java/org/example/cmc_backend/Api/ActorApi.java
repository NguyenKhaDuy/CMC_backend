package org.example.cmc_backend.Api;

import org.example.cmc_backend.Models.DTO.ActorDTO;
import org.example.cmc_backend.Models.Request.ActorRequest;
import org.example.cmc_backend.Models.Response.DataPageResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ActorApi {
    @Autowired
    ActorService actorService;

    @GetMapping(value = "/api/admin/actor/")
    public ResponseEntity<Object> getAllActors(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        DataPageResponse dataPageResponse = new DataPageResponse();
        Page<ActorDTO> actorDTOS = actorService.GetAllActor(pageNo);
        dataPageResponse.setData(actorDTOS.getContent());
        dataPageResponse.setMessage("Success");
        dataPageResponse.setTotal_page(actorDTOS.getTotalPages());
        dataPageResponse.setCurrent_page(pageNo);
        dataPageResponse.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(dataPageResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/api/actor/id-movie={idMovie}")
    public ResponseEntity<Object> getActorById(@PathVariable String idMovie) {
        Object result = actorService.GetActorByMovie(idMovie);
        if(result instanceof MessageResponse){
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/api/admin/actor")
    public ResponseEntity<Object> addActor(@ModelAttribute ActorRequest actorRequest) {
        MessageResponse result = actorService.AddActor(actorRequest);
        return new ResponseEntity<>(result, result.getStatus());
    }

    @PutMapping(value = "/api/admin/actor")
    public ResponseEntity<Object> updateActor(@ModelAttribute ActorRequest actorRequest) {
        MessageResponse result = actorService.UpdateActor(actorRequest);
        return new ResponseEntity<>(result, result.getStatus());
    }

    @GetMapping(value = "/api/actor/id-actor={idActor}")
    public ResponseEntity<Object> GetActorById(@PathVariable Long idActor) {
        Object result = actorService.GetDetailActor(idActor);
        if (result instanceof MessageResponse){
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/admin/actor/id-actor={idActor}")
    public ResponseEntity<Object> deleteActor(@PathVariable Long idActor) {
        Object result = actorService.DeleteActor(idActor);
        if (result instanceof MessageResponse){
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
