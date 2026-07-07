package org.example.cmc_backend.Service.Implement;

import org.example.cmc_backend.Entity.*;
import org.example.cmc_backend.Models.DTO.*;
import org.example.cmc_backend.Models.Request.AddActorForMovieRequest;
import org.example.cmc_backend.Models.Request.AddMovieActorRequest;
import org.example.cmc_backend.Models.Request.MovieRequest;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Repository.*;
import org.example.cmc_backend.Service.MovieService;
import org.example.cmc_backend.Utils.ConvertByteToBase64;
import org.example.cmc_backend.Utils.RandomIdUtils;
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
public class MovieServiceImplement implements MovieService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    BranchRepository branchRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ActorRepository actorRepository;
    @Autowired
    ActorMovieRepository actorMovieRepository;


    @Override
    public Page<MovieDTO> getAllMovies(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 10);
        List<MovieDTO> movieDTOS = new ArrayList<>();
        Page<MovieEntity> movieEntities = movieRepository.findAll(pageable);
        for (MovieEntity movieEntity : movieEntities) {
            MovieDTO movieDTO = new MovieDTO();
            modelMapper.map(movieEntity, movieDTO);
            movieDTO.setSmallImage(ConvertByteToBase64.toBase64(movieEntity.getSmallImage()));
            movieDTO.setLargeImage(ConvertByteToBase64.toBase64(movieEntity.getLargeImage()));
            movieDTO.setIdCategory(movieEntity.getCategoryEntity().getIdCategory());
            movieDTO.setCategory(movieEntity.getCategoryEntity().getNameCategory());

            List<RatingDTO> ratingDTOS = new ArrayList<>();
            for (RatingEntity ratingEntity : movieEntity.getRatingEntities()) {
                RatingDTO ratingDTO = new RatingDTO();
                modelMapper.map(ratingEntity, ratingDTO);
                ratingDTO.setIdUser(ratingEntity.getUserEntity().getIdUser());
                ratingDTO.setNameUser(ratingEntity.getUserEntity().getFullName());
                ratingDTO.setAvatar(ConvertByteToBase64.toBase64(ratingEntity.getUserEntity().getAvatar()));
                ratingDTOS.add(ratingDTO);
            }
            movieDTO.setRatingDTOS(ratingDTOS);

            List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
            for (ScheduleEntity scheduleEntity : movieEntity.getScheduleEntities()) {
                ScheduleDTO scheduleDTO = new ScheduleDTO();
                modelMapper.map(scheduleEntity, scheduleDTO);

                RoomDTO roomDTO = new RoomDTO();
                modelMapper.map(scheduleEntity.getRoomEntity(), roomDTO);

                BranchDTO branchDTO = new BranchDTO();
                modelMapper.map(scheduleEntity.getRoomEntity().getBranchEntity(), branchDTO);
                roomDTO.setBranchDTO(branchDTO);

                scheduleDTO.setRoomDTO(roomDTO);

                scheduleDTOS.add(scheduleDTO);
            }
            movieDTO.setScheduleDTOS(scheduleDTOS);

            List<MovieActorDTO> movieActorDTOS = new ArrayList<>();
            for (ActorMovieEntity actorMovieEntity : movieEntity.getActorMovieEntities()) {
                MovieActorDTO movieActorDTO = new MovieActorDTO();
                movieActorDTO.setIdActor(actorMovieEntity.getActorEntity().getIdActor());
                movieActorDTO.setName(actorMovieEntity.getActorEntity().getName());
                movieActorDTO.setImage(ConvertByteToBase64.toBase64(actorMovieEntity.getActorEntity().getImage()));
                movieActorDTO.setDescription(actorMovieEntity.getActorEntity().getDescription());
                movieActorDTO.set_main(actorMovieEntity.isMain());
                movieActorDTOS.add(movieActorDTO);
            }
            movieDTO.setMovieActorDTOS(movieActorDTOS);

            movieDTOS.add(movieDTO);
        }
        return new PageImpl<>(movieDTOS, movieEntities.getPageable(), movieEntities.getTotalElements());
    }

    @Override
    public DataResponse getAllMovies() {
        List<MovieDTO> movieDTOS = new ArrayList<>();
        List<MovieEntity> movieEntities = movieRepository.findAll();
        DataResponse dataResponse = new DataResponse();
        for (MovieEntity movieEntity : movieEntities) {
            MovieDTO movieDTO = new MovieDTO();
            modelMapper.map(movieEntity, movieDTO);
            movieDTO.setSmallImage(ConvertByteToBase64.toBase64(movieEntity.getSmallImage()));
            movieDTO.setLargeImage(ConvertByteToBase64.toBase64(movieEntity.getLargeImage()));
            movieDTO.setIdCategory(movieEntity.getCategoryEntity().getIdCategory());
            movieDTO.setCategory(movieEntity.getCategoryEntity().getNameCategory());

            List<RatingDTO> ratingDTOS = new ArrayList<>();
            for (RatingEntity ratingEntity : movieEntity.getRatingEntities()) {
                RatingDTO ratingDTO = new RatingDTO();
                modelMapper.map(ratingEntity, ratingDTO);
                ratingDTO.setIdUser(ratingEntity.getUserEntity().getIdUser());
                ratingDTO.setNameUser(ratingEntity.getUserEntity().getFullName());
                ratingDTO.setAvatar(ConvertByteToBase64.toBase64(ratingEntity.getUserEntity().getAvatar()));
                ratingDTOS.add(ratingDTO);
            }
            movieDTO.setRatingDTOS(ratingDTOS);

            List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
            for (ScheduleEntity scheduleEntity : movieEntity.getScheduleEntities()) {
                ScheduleDTO scheduleDTO = new ScheduleDTO();
                modelMapper.map(scheduleEntity, scheduleDTO);

                RoomDTO roomDTO = new RoomDTO();
                modelMapper.map(scheduleEntity.getRoomEntity(), roomDTO);

                BranchDTO branchDTO = new BranchDTO();
                modelMapper.map(scheduleEntity.getRoomEntity().getBranchEntity(), branchDTO);
                roomDTO.setBranchDTO(branchDTO);

                scheduleDTO.setRoomDTO(roomDTO);

                scheduleDTOS.add(scheduleDTO);
            }
            movieDTO.setScheduleDTOS(scheduleDTOS);

            List<MovieActorDTO> movieActorDTOS = new ArrayList<>();
            for (ActorMovieEntity actorMovieEntity : movieEntity.getActorMovieEntities()) {
                MovieActorDTO movieActorDTO = new MovieActorDTO();
                movieActorDTO.setIdActor(actorMovieEntity.getActorEntity().getIdActor());
                movieActorDTO.setName(actorMovieEntity.getActorEntity().getName());
                movieActorDTO.setImage(ConvertByteToBase64.toBase64(actorMovieEntity.getActorEntity().getImage()));
                movieActorDTO.setDescription(actorMovieEntity.getActorEntity().getDescription());
                movieActorDTO.set_main(actorMovieEntity.isMain());
                movieActorDTOS.add(movieActorDTO);
            }
            movieDTO.setMovieActorDTOS(movieActorDTOS);

            movieDTOS.add(movieDTO);
        }
        dataResponse.setData(movieDTOS);
        dataResponse.setStatus(HttpStatus.OK);
        dataResponse.setMessage("OK");
        return dataResponse;
    }

    @Override
    public Object getAllMoviesByBranch(Long idBranch) {
        DataResponse dataResponse = new DataResponse();
        MessageResponse messageResponse = new MessageResponse();
        List<MovieEntity> movieEntities = movieRepository.findMoviesByBranch(idBranch);
        List<MovieDTO> movieDTOS = new ArrayList<>();
        for (MovieEntity movieEntity : movieEntities) {
            MovieDTO movieDTO = new MovieDTO();
            modelMapper.map(movieEntity, movieDTO);
            movieDTO.setSmallImage(ConvertByteToBase64.toBase64(movieEntity.getSmallImage()));
            movieDTO.setLargeImage(ConvertByteToBase64.toBase64(movieEntity.getLargeImage()));
            movieDTO.setIdCategory(movieEntity.getCategoryEntity().getIdCategory());
            movieDTO.setCategory(movieEntity.getCategoryEntity().getNameCategory());

            List<RatingDTO> ratingDTOS = new ArrayList<>();
            for (RatingEntity ratingEntity : movieEntity.getRatingEntities()) {
                RatingDTO ratingDTO = new RatingDTO();
                modelMapper.map(ratingEntity, ratingDTO);
                ratingDTO.setIdUser(ratingEntity.getUserEntity().getIdUser());
                ratingDTO.setNameUser(ratingEntity.getUserEntity().getFullName());
                ratingDTO.setAvatar(ConvertByteToBase64.toBase64(ratingEntity.getUserEntity().getAvatar()));
                ratingDTOS.add(ratingDTO);
            }
            movieDTO.setRatingDTOS(ratingDTOS);

            List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
            for (ScheduleEntity scheduleEntity : movieEntity.getScheduleEntities()) {
                Long soldSeat = 0L;
                ScheduleDTO scheduleDTO = new ScheduleDTO();
                modelMapper.map(scheduleEntity, scheduleDTO);

                RoomDTO roomDTO = new RoomDTO();
                modelMapper.map(scheduleEntity.getRoomEntity(), roomDTO);

                BranchDTO branchDTO = new BranchDTO();
                modelMapper.map(scheduleEntity.getRoomEntity().getBranchEntity(), branchDTO);
                roomDTO.setBranchDTO(branchDTO);
                scheduleDTO.setRoomDTO(roomDTO);

                for (StatusSeatEntity statusSeatEntity : scheduleEntity.getStatusSeatEntities()){
                    if (statusSeatEntity.getStatus().equals("BOOKED")){
                        soldSeat++;
                    }
                }
                scheduleDTO.setSoldSeats(soldSeat);

                scheduleDTOS.add(scheduleDTO);
            }
            movieDTO.setScheduleDTOS(scheduleDTOS);

            List<MovieActorDTO> movieActorDTOS = new ArrayList<>();
            for (ActorMovieEntity actorMovieEntity : movieEntity.getActorMovieEntities()) {
                MovieActorDTO movieActorDTO = new MovieActorDTO();
                movieActorDTO.setIdActor(actorMovieEntity.getActorEntity().getIdActor());
                movieActorDTO.setName(actorMovieEntity.getActorEntity().getName());
                movieActorDTO.setImage(ConvertByteToBase64.toBase64(actorMovieEntity.getActorEntity().getImage()));
                movieActorDTO.setDescription(actorMovieEntity.getActorEntity().getDescription());
                movieActorDTO.set_main(actorMovieEntity.isMain());
                movieActorDTOS.add(movieActorDTO);
            }
            movieDTO.setMovieActorDTOS(movieActorDTOS);

            movieDTOS.add(movieDTO);
        }

        dataResponse.setData(movieDTOS);
        dataResponse.setMessage("Success");
        dataResponse.setStatus(HttpStatus.OK);
        return dataResponse;
    }

    @Override
    public Object getMovieById(String idMovie) {
        MessageResponse messageResponse = new MessageResponse();
        DataResponse dataResponse = new DataResponse();
        List<RatingDTO> ratingDTOS = new ArrayList<>();
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        List<MovieActorDTO> movieActorDTOS = new ArrayList<>();
        try {
            MovieEntity movieEntity = movieRepository.findById(idMovie).get();
            MovieDTO movieDTO = new MovieDTO();
            modelMapper.map(movieEntity, movieDTO);
            movieDTO.setSmallImage(ConvertByteToBase64.toBase64(movieEntity.getSmallImage()));
            movieDTO.setLargeImage(ConvertByteToBase64.toBase64(movieEntity.getLargeImage()));
            movieDTO.setIdCategory(movieEntity.getCategoryEntity().getIdCategory());
            movieDTO.setCategory(movieEntity.getCategoryEntity().getNameCategory());

            for (RatingEntity ratingEntity : movieEntity.getRatingEntities()) {
                RatingDTO ratingDTO = new RatingDTO();
                modelMapper.map(ratingEntity, ratingDTO);
                ratingDTO.setIdUser(ratingEntity.getUserEntity().getIdUser());
                ratingDTO.setNameUser(ratingEntity.getUserEntity().getFullName());
                ratingDTO.setAvatar(ConvertByteToBase64.toBase64(ratingEntity.getUserEntity().getAvatar()));
                ratingDTOS.add(ratingDTO);
            }
            movieDTO.setRatingDTOS(ratingDTOS);

            for (ScheduleEntity scheduleEntity : movieEntity.getScheduleEntities()) {
                ScheduleDTO scheduleDTO = new ScheduleDTO();
                modelMapper.map(scheduleEntity, scheduleDTO);

                RoomDTO roomDTO = new RoomDTO();
                modelMapper.map(scheduleEntity.getRoomEntity(), roomDTO);

                BranchDTO branchDTO = new BranchDTO();
                modelMapper.map(scheduleEntity.getRoomEntity().getBranchEntity(), branchDTO);
                roomDTO.setBranchDTO(branchDTO);

                scheduleDTO.setRoomDTO(roomDTO);

                scheduleDTOS.add(scheduleDTO);
            }
            movieDTO.setScheduleDTOS(scheduleDTOS);

            for (ActorMovieEntity actorMovieEntity : movieEntity.getActorMovieEntities()) {
                MovieActorDTO movieActorDTO = new MovieActorDTO();
                movieActorDTO.setIdActor(actorMovieEntity.getActorEntity().getIdActor());
                movieActorDTO.setName(actorMovieEntity.getActorEntity().getName());
                movieActorDTO.setImage(ConvertByteToBase64.toBase64(actorMovieEntity.getActorEntity().getImage()));
                movieActorDTO.setDescription(actorMovieEntity.getActorEntity().getDescription());
                movieActorDTO.set_main(actorMovieEntity.isMain());
                movieActorDTOS.add(movieActorDTO);
            }
            movieDTO.setMovieActorDTOS(movieActorDTOS);

            dataResponse.setMessage("Success");
            dataResponse.setStatus(HttpStatus.OK);
            dataResponse.setData(movieDTO);
        } catch (NoSuchElementException ex) {
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            messageResponse.setMessage("Movie Not Found");
            return messageResponse;
        }
        return dataResponse;
    }

    @Override
    public Object getMovieByCategory(Long idCategory) {
        MessageResponse messageResponse = new MessageResponse();
        DataResponse dataResponse = new DataResponse();
        List<MovieDTO> movieDTOS = new ArrayList<>();
        try {
            CategoryEntity categoryEntity = categoryRepository.findById(idCategory).get();
            try {
                List<MovieEntity> movieEntities = movieRepository.findMoviesByCategoryEntity(categoryEntity);
                for (MovieEntity movieEntity : movieEntities) {
                    MovieDTO movieDTO = new MovieDTO();
                    modelMapper.map(movieEntity, movieDTO);
                    movieDTO.setSmallImage(ConvertByteToBase64.toBase64(movieEntity.getSmallImage()));
                    movieDTO.setLargeImage(ConvertByteToBase64.toBase64(movieEntity.getLargeImage()));
                    movieDTO.setIdCategory(movieEntity.getCategoryEntity().getIdCategory());
                    movieDTO.setCategory(movieEntity.getCategoryEntity().getNameCategory());

                    List<RatingDTO> ratingDTOS = new ArrayList<>();
                    for (RatingEntity ratingEntity : movieEntity.getRatingEntities()) {
                        RatingDTO ratingDTO = new RatingDTO();
                        modelMapper.map(ratingEntity, ratingDTO);
                        ratingDTO.setIdUser(ratingEntity.getUserEntity().getIdUser());
                        ratingDTO.setNameUser(ratingEntity.getUserEntity().getFullName());
                        ratingDTO.setAvatar(ConvertByteToBase64.toBase64(ratingEntity.getUserEntity().getAvatar()));
                        ratingDTOS.add(ratingDTO);
                    }
                    movieDTO.setRatingDTOS(ratingDTOS);

                    List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
                    for (ScheduleEntity scheduleEntity : movieEntity.getScheduleEntities()) {
                        ScheduleDTO scheduleDTO = new ScheduleDTO();
                        modelMapper.map(scheduleEntity, scheduleDTO);

                        RoomDTO roomDTO = new RoomDTO();
                        modelMapper.map(scheduleEntity.getRoomEntity(), roomDTO);

                        BranchDTO branchDTO = new BranchDTO();
                        modelMapper.map(scheduleEntity.getRoomEntity().getBranchEntity(), branchDTO);
                        roomDTO.setBranchDTO(branchDTO);

                        scheduleDTO.setRoomDTO(roomDTO);

                        scheduleDTOS.add(scheduleDTO);
                    }
                    movieDTO.setScheduleDTOS(scheduleDTOS);

                    List<MovieActorDTO> movieActorDTOS = new ArrayList<>();
                    for (ActorMovieEntity actorMovieEntity : movieEntity.getActorMovieEntities()) {
                        MovieActorDTO movieActorDTO = new MovieActorDTO();
                        movieActorDTO.setIdActor(actorMovieEntity.getActorEntity().getIdActor());
                        movieActorDTO.setName(actorMovieEntity.getActorEntity().getName());
                        movieActorDTO.setImage(ConvertByteToBase64.toBase64(actorMovieEntity.getActorEntity().getImage()));
                        movieActorDTO.setDescription(actorMovieEntity.getActorEntity().getDescription());
                        movieActorDTO.set_main(actorMovieEntity.isMain());
                        movieActorDTOS.add(movieActorDTO);
                    }
                    movieDTO.setMovieActorDTOS(movieActorDTOS);

                    movieDTOS.add(movieDTO);
                }
                dataResponse.setMessage("Success");
                dataResponse.setStatus(HttpStatus.OK);
                dataResponse.setData(movieDTOS);
            } catch (NoSuchElementException ex) {
                messageResponse.setStatus(HttpStatus.NOT_FOUND);
                messageResponse.setMessage("Movie Not Found");
                return messageResponse;
            }
        } catch (NoSuchElementException ex) {
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            messageResponse.setMessage("Category Not Found");
            return messageResponse;
        }
        return dataResponse;
    }

    @Override
    public MessageResponse addMovie(MovieRequest movieRequest) {
        MessageResponse messageResponse = new MessageResponse();
        MovieEntity movieEntity = new MovieEntity();
        CategoryEntity categoryEntity = null;
        try {
            categoryEntity = categoryRepository.findById(movieRequest.getIdCategory()).get();
        } catch (NoSuchElementException ex) {
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            messageResponse.setMessage("Category Not Found");
            return messageResponse;
        }
        modelMapper.map(movieRequest, movieEntity);
        movieEntity.setIdMovie(RandomIdUtils.generateRandomId("MV", 10));
        try {
            movieEntity.setSmallImage(movieRequest.getSmallImage().getBytes());
            movieEntity.setLargeImage(movieRequest.getLargeImage().getBytes());
        } catch (IOException e) {
            messageResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            messageResponse.setMessage("Error while adding movie");
            return messageResponse;
        }
        movieEntity.setCategoryEntity(categoryEntity);
        movieRepository.save(movieEntity);
        messageResponse.setMessage("Success");
        messageResponse.setStatus(HttpStatus.OK);
        return messageResponse;
    }

    @Override
    public MessageResponse updateMovie(MovieRequest movieRequest) {
        MessageResponse messageResponse = new MessageResponse();
        try {
            MovieEntity movieEntity = movieRepository.findById(movieRequest.getIdMovie()).get();
            CategoryEntity categoryEntity = null;
            try {
                categoryEntity = categoryRepository.findById(movieRequest.getIdCategory()).get();
            } catch (NoSuchElementException ex) {
                messageResponse.setStatus(HttpStatus.NOT_FOUND);
                messageResponse.setMessage("Category Not Found");
                return messageResponse;
            }
            modelMapper.map(movieRequest, movieEntity);
            try {
                movieEntity.setSmallImage(movieRequest.getSmallImage().getBytes());
                movieEntity.setLargeImage(movieRequest.getLargeImage().getBytes());
            } catch (IOException e) {
                messageResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                messageResponse.setMessage("Error while adding movie");
                return messageResponse;
            }
            movieEntity.setCategoryEntity(categoryEntity);
            movieEntity.getActorMovieEntities().clear();
            movieRepository.save(movieEntity);

            messageResponse.setMessage("Success");
            messageResponse.setStatus(HttpStatus.OK);

        } catch (NoSuchElementException ex) {
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            messageResponse.setMessage("Movie Not Found");
            return messageResponse;
        }
        return messageResponse;
    }

    @Override
    public MessageResponse deleteMovie(String idMovie) {
        MessageResponse messageResponse = new MessageResponse();
        try {
            MovieEntity movieEntity = movieRepository.findById(idMovie).get();
            movieRepository.delete(movieEntity);
            messageResponse.setMessage("Success");
            messageResponse.setStatus(HttpStatus.OK);
        } catch (NoSuchElementException ex) {
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            messageResponse.setMessage("Movie Not Found");
            return messageResponse;
        }
        return messageResponse;
    }

    @Override
    public MessageResponse addActorMovie(AddActorForMovieRequest addActorForMovieRequest) {
        MessageResponse messageResponse = new MessageResponse();
        MovieEntity movieEntity = null;
        try {
            movieEntity = movieRepository.findById(addActorForMovieRequest.getIdMovie()).get();
            movieEntity.getActorMovieEntities().clear();
        } catch (NoSuchElementException ex) {
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            messageResponse.setMessage("Movie Not Found");
        }
        for (AddMovieActorRequest addMovieActorRequest : addActorForMovieRequest.getAddMovieActorRequests()) {
            ActorEntity actorEntity = null;
            try {
                actorEntity = actorRepository.findById(addMovieActorRequest.getIdActor()).get();
            } catch (NoSuchElementException ex) {
                messageResponse.setStatus(HttpStatus.NOT_FOUND);
                messageResponse.setMessage("Actor Not Found");
                return messageResponse;
            }
            ActorMovieEntity actorMovieEntity = new ActorMovieEntity();
            actorMovieEntity.setMain(addMovieActorRequest.isMain());
            actorMovieEntity.setMovieEntity(movieEntity);
            actorMovieEntity.setActorEntity(actorEntity);
            actorMovieRepository.save(actorMovieEntity);
        }
        messageResponse.setMessage("Success");
        messageResponse.setStatus(HttpStatus.OK);
        return messageResponse;
    }
}
