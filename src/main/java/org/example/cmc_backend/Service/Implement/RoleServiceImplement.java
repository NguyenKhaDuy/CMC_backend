package org.example.cmc_backend.Service.Implement;

import org.example.cmc_backend.Entity.RoleEntity;
import org.example.cmc_backend.Entity.SizeEntity;
import org.example.cmc_backend.Models.DTO.RoleDTO;
import org.example.cmc_backend.Models.DTO.SizeDTO;
import org.example.cmc_backend.Models.Request.RoleRequest;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Repository.RoleRepository;
import org.example.cmc_backend.Service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RoleServiceImplement implements RoleService {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    ModelMapper modelMapper;


    @Override
    public DataResponse getAllRole() {
        DataResponse dataResponse = new DataResponse();
        List<RoleEntity> roleEntities = roleRepository.findAll();
        List<RoleDTO> roleDTOS = new ArrayList<>();
        for (RoleEntity roleEntity : roleEntities) {
            RoleDTO roleDTO = new RoleDTO();
            modelMapper.map(roleEntity, roleDTO);
            roleDTOS.add(roleDTO);
        }
        dataResponse.setData(roleDTOS);
        dataResponse.setMessage("Success");
        dataResponse.setStatus(HttpStatus.OK);
        return dataResponse;
    }

    @Override
    public Object getRoleById(Long idRole) {
        DataResponse dataResponse = new DataResponse();
        MessageResponse messageResponse = new MessageResponse();
        try{
            RoleEntity roleEntity = roleRepository.findById(idRole).get();
            RoleDTO roleDTO = new RoleDTO();
            modelMapper.map(roleEntity, roleDTO);
            dataResponse.setData(roleDTO);
            dataResponse.setMessage("Success");
            dataResponse.setStatus(HttpStatus.OK);
            return dataResponse;
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Role Not Found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
    }

    @Override
    public MessageResponse addRole(RoleRequest roleRequest) {
        MessageResponse messageResponse = new MessageResponse();
        RoleEntity roleEntity = new RoleEntity();
        modelMapper.map(roleRequest, roleEntity);
        roleRepository.save(roleEntity);
        messageResponse.setMessage("Success");
        messageResponse.setStatus(HttpStatus.OK);
        return messageResponse;
    }

    @Override
    public MessageResponse deleteRole(Long idRole) {
        MessageResponse messageResponse = new MessageResponse();
        try{
            RoleEntity roleEntity = roleRepository.findById(idRole).get();
            roleRepository.delete(roleEntity);
            messageResponse.setMessage("Success");
            messageResponse.setStatus(HttpStatus.OK);
            return messageResponse;
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Role Not Found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
    }

    @Override
    public MessageResponse updateRole(RoleRequest roleRequest) {
        MessageResponse messageResponse = new MessageResponse();
        try{
            RoleEntity roleEntity = roleRepository.findById(roleRequest.getIdRole()).get();
            modelMapper.map(roleRequest, roleEntity);
            messageResponse.setMessage("Success");
            messageResponse.setStatus(HttpStatus.OK);
            return messageResponse;
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Role Not Found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
    }
}
