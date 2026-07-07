package org.example.cmc_backend.Service.Implement;

import org.example.cmc_backend.Entity.ActorEntity;
import org.example.cmc_backend.Entity.ActorMovieEntity;
import org.example.cmc_backend.Entity.MovieEntity;
import org.example.cmc_backend.Models.DTO.ActorDTO;
import org.example.cmc_backend.Models.DTO.ActorMovieDTO;
import org.example.cmc_backend.Models.Request.ActorMovieRequest;
import org.example.cmc_backend.Models.Request.ActorRequest;
import org.example.cmc_backend.Models.Response.DataPageResponse;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Repository.ActorMovieRepository;
import org.example.cmc_backend.Repository.ActorRepository;
import org.example.cmc_backend.Repository.MovieRepository;
import org.example.cmc_backend.Service.ActorService;
import org.example.cmc_backend.Utils.ConvertByteToBase64;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ActorServiceImplement implements ActorService {
    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ActorMovieRepository actorMovieRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public Page<ActorDTO> GetAllActor(Integer pageNo) {
        List<ActorDTO> actorDTOS = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNo - 1, 10);
        Page<ActorEntity> actorEntities = actorRepository.findAll(pageable);
        for (ActorEntity actorEntity : actorEntities) {
            ActorDTO actorDTO = new ActorDTO();
            List<ActorMovieDTO> actorMovieDTOS = new ArrayList<>();
            for (ActorMovieEntity actorMovieEntity : actorEntity.getActorMovieEntities()) {
                ActorMovieDTO actorMovieDTO = new ActorMovieDTO();
                actorMovieDTO.setIdMovie(actorMovieEntity.getMovieEntity().getIdMovie());
                actorMovieDTO.setNameMovie(actorMovieEntity.getMovieEntity().getNameMovie());
                actorMovieDTO.set_main(actorMovieEntity.isMain());
                actorMovieDTOS.add(actorMovieDTO);
            }
            modelMapper.map(actorEntity, actorDTO);
            actorDTO.setImage(ConvertByteToBase64.toBase64(actorEntity.getImage()));
            actorDTO.setActorMovieDTOS(actorMovieDTOS);
            actorDTOS.add(actorDTO);
        }
        return new PageImpl<>(actorDTOS, actorEntities.getPageable(), actorEntities.getTotalElements());
    }

    @Override
    public DataResponse GetAllActor() {
        DataResponse dataResponse = new DataResponse();
        List<ActorDTO> actorDTOS = new ArrayList<>();
        List<ActorEntity> actorEntities = actorRepository.findAll();
        for (ActorEntity actorEntity : actorEntities) {
            ActorDTO actorDTO = new ActorDTO();
            List<ActorMovieDTO> actorMovieDTOS = new ArrayList<>();
            for (ActorMovieEntity actorMovieEntity : actorEntity.getActorMovieEntities()) {
                ActorMovieDTO actorMovieDTO = new ActorMovieDTO();
                actorMovieDTO.setIdMovie(actorMovieEntity.getMovieEntity().getIdMovie());
                actorMovieDTO.setNameMovie(actorMovieEntity.getMovieEntity().getNameMovie());
                actorMovieDTO.set_main(actorMovieEntity.isMain());
                actorMovieDTOS.add(actorMovieDTO);
            }
            modelMapper.map(actorEntity, actorDTO);
            actorDTO.setImage(ConvertByteToBase64.toBase64(actorEntity.getImage()));
            actorDTO.setActorMovieDTOS(actorMovieDTOS);
            actorDTOS.add(actorDTO);
        }
        dataResponse.setData(actorDTOS);
        dataResponse.setStatus(HttpStatus.OK);
        dataResponse.setMessage("success");
        return dataResponse;
    }

    @Override
    public Object GetActorByMovie(String movieId) {
        MessageResponse messageResponse = new MessageResponse();
        DataResponse dataResponse = new DataResponse();
        MovieEntity movieEntity = null;
        List<ActorDTO> actorDTOS = new ArrayList<>();

        try{
            movieEntity = movieRepository.findById(movieId).get();
        }catch (NoSuchElementException e)
        {
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            messageResponse.setMessage("Can't find movie");
            return messageResponse;
        }
        List<ActorMovieEntity> actorMovieEntities = actorMovieRepository.findByMovieEntity(movieEntity);

        if (actorMovieEntities.size() == 0)
        {
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            messageResponse.setMessage("Can't find actor for movie");
            return messageResponse;
        }

        for (ActorMovieEntity actorMovieEntity : actorMovieEntities) {
            ActorDTO actorDTO = new ActorDTO();
            ActorEntity actorEntity = actorMovieEntity.getActorEntity();

            //lấy ra danh sách phim mà diễn viên tham gia
            List<ActorMovieDTO> actorMovieDTOS = new ArrayList<>();
            List<ActorMovieEntity> actorMovies = actorMovieRepository.findByActorEntity(actorEntity);
            for (ActorMovieEntity actorMovie : actorMovies) {
                ActorMovieDTO actorMovieDTO = new ActorMovieDTO();
                actorMovieDTO.setIdMovie(actorMovie.getMovieEntity().getIdMovie());
                actorMovieDTO.setNameMovie(actorMovie.getMovieEntity().getNameMovie());
                actorMovieDTO.set_main(actorMovieEntity.isMain());
                actorMovieDTOS.add(actorMovieDTO);
                actorDTO.setActorMovieDTOS(actorMovieDTOS);
            }

            modelMapper.map(actorEntity, actorDTO);
            actorDTO.setImage(ConvertByteToBase64.toBase64(actorEntity.getImage()));
            actorDTOS.add(actorDTO);
        }
        dataResponse.setData(actorDTOS);
        dataResponse.setMessage("Success");
        dataResponse.setStatus(HttpStatus.OK);
        return dataResponse;
    }

    @Override
    public MessageResponse AddActor(ActorRequest actorRequest) {
        MessageResponse messageResponse = new MessageResponse();
        ActorEntity actorEntity = new ActorEntity();
        actorEntity.setName(actorRequest.getName());
        actorEntity.setDescription(actorRequest.getDescription());
        try {
            actorEntity.setImage(actorRequest.getImage().getBytes());
        } catch (IOException e) {
            messageResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            messageResponse.setMessage("Can't add actor");
            return messageResponse;
        }
        actorRepository.save(actorEntity);
        messageResponse.setStatus(HttpStatus.OK);
        messageResponse.setMessage("Success");
        return messageResponse;
    }

    @Override
    public MessageResponse UpdateActor(ActorRequest actorRequest) {
        MessageResponse messageResponse = new MessageResponse();
        try{
            ActorEntity actorEntity = actorRepository.findById(actorRequest.getIdActor()).get();
            actorEntity.setName(actorRequest.getName());
            actorEntity.setDescription(actorRequest.getDescription());
            try {
                actorEntity.setImage(actorRequest.getImage().getBytes());
            } catch (IOException e) {
                messageResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                messageResponse.setMessage("Can't update actor");
                return messageResponse;
            }
            actorRepository.save(actorEntity);
            messageResponse.setStatus(HttpStatus.OK);
            messageResponse.setMessage("Success");
            return messageResponse;
        }catch (NoSuchElementException e){
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            messageResponse.setMessage("Can't find actor");
            return messageResponse;
        }
    }

    @Override
    public Object GetDetailActor(Long actorId) {
        MessageResponse messageResponse = new MessageResponse();
        DataResponse dataResponse = new DataResponse();
        ActorDTO actorDTO = new ActorDTO();
        List<ActorMovieDTO> actorMovieDTOS = new ArrayList<>();
        try{
            ActorEntity actorEntity = actorRepository.findById(actorId).get();
            modelMapper.map(actorEntity, actorDTO);
            actorDTO.setImage(ConvertByteToBase64.toBase64(actorEntity.getImage()));
            for (ActorMovieEntity actorMovieEntity : actorEntity.getActorMovieEntities()){
                ActorMovieDTO actorMovieDTO = new ActorMovieDTO();
                actorMovieDTO.setNameMovie(actorMovieEntity.getMovieEntity().getNameMovie());
                actorMovieDTO.setIdMovie(actorMovieEntity.getMovieEntity().getIdMovie());
                actorMovieDTO.set_main(actorMovieEntity.isMain());
                actorMovieDTOS.add(actorMovieDTO);
                actorDTO.setActorMovieDTOS(actorMovieDTOS);
            }
            dataResponse.setData(actorDTO);
            dataResponse.setMessage("Success");
            dataResponse.setStatus(HttpStatus.OK);
            return dataResponse;
        }catch (NoSuchElementException e){
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            messageResponse.setMessage("Can't find actor");
            return messageResponse;
        }
    }

    @Override
    public MessageResponse DeleteActor(Long actorId) {
        MessageResponse messageResponse = new MessageResponse();
        try{
            ActorEntity actorEntity = actorRepository.findById(actorId).get();
            actorRepository.delete(actorEntity);
            messageResponse.setStatus(HttpStatus.OK);
            messageResponse.setMessage("Success");
            return messageResponse;
        }catch (NoSuchElementException e){
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            messageResponse.setMessage("Can't find actor");
            return messageResponse;
        }
    }
}
