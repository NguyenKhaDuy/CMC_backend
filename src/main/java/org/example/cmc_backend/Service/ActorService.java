package org.example.cmc_backend.Service;

import org.example.cmc_backend.Models.Request.ActorRequest;
import org.example.cmc_backend.Models.Response.DataPageResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.springframework.stereotype.Service;

@Service
public interface ActorService {
    DataPageResponse GetAllActor(Integer pageNo);
    Object GetActorByMovie(String movieId);
    MessageResponse AddActor(ActorRequest actorRequest);
    MessageResponse UpdateActor(ActorRequest actorRequest);
    Object GetDetailActor(Long actorId);
    MessageResponse DeleteActor(Long actorId);
}
