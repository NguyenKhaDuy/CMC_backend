package org.example.cmc_backend.Service.Implement;

import org.example.cmc_backend.Entity.MovieEntity;
import org.example.cmc_backend.Entity.RatingEntity;
import org.example.cmc_backend.Entity.UserEntity;
import org.example.cmc_backend.Models.Request.RatingRequest;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Repository.MovieRepository;
import org.example.cmc_backend.Repository.RatingRepository;
import org.example.cmc_backend.Repository.UserRepository;
import org.example.cmc_backend.Service.RatingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class RatingServiceImplement implements RatingService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    RatingRepository ratingRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public MessageResponse deleteRating(Long idRating) {
        MessageResponse messageResponse = new MessageResponse();
        try{
            RatingEntity ratingEntity = ratingRepository.findById(idRating).get();
            ratingRepository.delete(ratingEntity);
            messageResponse.setStatus(HttpStatus.OK);
            messageResponse.setMessage("Successfully deleted rating");
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Can not found rating");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        return messageResponse;
    }

    @Override
    public MessageResponse createRating(RatingRequest ratingRequest) {
        MessageResponse messageResponse = new MessageResponse();
        UserEntity userEntity = null;
        MovieEntity movieEntity = null;
        try {
            userEntity = userRepository.findById(ratingRequest.getIdUser()).get();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Can not found user");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }

        try {
            movieEntity = movieRepository.findById(ratingRequest.getIdMovie()).get();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Can not found movie");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }

        RatingEntity ratingEntity = new RatingEntity();
        ratingEntity.setUserEntity(userEntity);
        ratingEntity.setMovieEntity(movieEntity);
        modelMapper.map(ratingRequest, ratingEntity);
        ratingRepository.save(ratingEntity);
        messageResponse.setStatus(HttpStatus.OK);
        messageResponse.setMessage("Successfully created rating");
        return messageResponse;
    }
}
