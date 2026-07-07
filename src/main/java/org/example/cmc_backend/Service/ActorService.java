package org.example.cmc_backend.Service;

import org.example.cmc_backend.Models.DTO.ActorDTO;
import org.example.cmc_backend.Models.Request.ActorRequest;
import org.example.cmc_backend.Models.Response.DataPageResponse;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface ActorService {
    Page<ActorDTO> GetAllActor(Integer pageNo); //admin
    DataResponse GetAllActor(); //admin
    Object GetActorByMovie(String movieId); //all
    MessageResponse AddActor(ActorRequest actorRequest);
    MessageResponse UpdateActor(ActorRequest actorRequest); //admin
    Object GetDetailActor(Long actorId); //all
    MessageResponse DeleteActor(Long actorId); //admin
}
