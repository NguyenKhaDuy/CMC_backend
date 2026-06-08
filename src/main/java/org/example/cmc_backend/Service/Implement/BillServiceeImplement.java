package org.example.cmc_backend.Service.Implement;

import org.example.cmc_backend.Entity.*;
import org.example.cmc_backend.Models.DTO.*;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Repository.BillRepository;
import org.example.cmc_backend.Repository.UserRepository;
import org.example.cmc_backend.Service.BillService;
import org.example.cmc_backend.Utils.ConvertByteToBase64;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BillServiceeImplement implements BillService {
    @Autowired
    BillRepository billRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public Object getAllBillsByUser(String idUser) {
        MessageResponse messageResponse = new MessageResponse();
        DataResponse dataResponse = new DataResponse();
        UserEntity userEntity = null;
        try{
            userEntity = userRepository.findById(idUser).get();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Can not found user");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        List<BillEntity> billEntities = billRepository.findAllByUserEntity(userEntity);
        List<BillDTO> billDTOS = new ArrayList<>();
        for (BillEntity billEntity : billEntities) {
            BillDTO billDTO = new BillDTO();
            modelMapper.map(billEntity, billDTO);
            billDTO.setQr(ConvertByteToBase64.toBase64(billEntity.getQr()));

            //set voucher
            VoucherDTO voucherDTO = new VoucherDTO();
            modelMapper.map(billEntity.getVoucherEntity(), voucherDTO);
            billDTO.setVoucherDTO(voucherDTO);

            //set branch
            BranchDTO branchDTO = new BranchDTO();
            branchDTO.setRoomDTOS(null);
            modelMapper.map(billEntity.getBranchEntity(), branchDTO);
            billDTO.setBranchDTO(branchDTO);

            //set ticket
            TicketDTO ticketDTO = new TicketDTO();
            List<SeatDTO> seatDTOS = new ArrayList<>();
            for (TicketEntity ticketEntity : billEntity.getTicketEntities()) {
                SeatDTO seatDTO = new SeatDTO();
                modelMapper.map(ticketEntity.getSeatEntity(), seatDTO);
                seatDTO.setIdRoom(ticketEntity.getSeatEntity().getRoomEntity().getIdRoom());
                seatDTO.setNameRoom(ticketEntity.getSeatEntity().getRoomEntity().getName());
                SeatTypeDTO seatTypeDTO = new SeatTypeDTO();
                modelMapper.map(ticketEntity.getSeatEntity().getSeatTypeEntity(), seatTypeDTO);
                seatDTO.setSeatTypeDTO(seatTypeDTO);
                seatDTOS.add(seatDTO);
                ticketDTO.setSeatDTOS(seatDTOS);
            }
            billDTO.setTicketDTO(ticketDTO);


            //Set list drink
            List<BillDrinkDetailDTO> billDrinkDetailDTOS = new ArrayList<>();
            for (BillDrinkDetailEntity billDrinkDetailEntity : billEntity.getBillDrinkDetailEntities()) {
                BillDrinkDetailDTO billDrinkDetailDTO = new BillDrinkDetailDTO();
                modelMapper.map(billDrinkDetailEntity, billDrinkDetailDTO);
                billDrinkDetailDTO.setNameDrink(billDrinkDetailEntity.getSizeDrinkEntity().getDrinkEntity().getName());
                billDrinkDetailDTO.setIdDrink(billDrinkDetailEntity.getSizeDrinkEntity().getDrinkEntity().getId());
                billDrinkDetailDTO.setSize(billDrinkDetailEntity.getSizeDrinkEntity().getSizeEntity().getSize());
                billDrinkDetailDTO.setPrice(billDrinkDetailEntity.getSizeDrinkEntity().getPrice());
                billDrinkDetailDTOS.add(billDrinkDetailDTO);
            }
            billDTO.setBillDrinkDetailDTOS(billDrinkDetailDTOS);

            //set list food
            List<BillFoodDetailDTO> billFoodDetailDTOS = new ArrayList<>();
            for (BillFoodDetailEntity billFoodDetailEntity : billEntity.getBillFoodDetailEntities()) {
                BillFoodDetailDTO billFoodDetailDTO = new BillFoodDetailDTO();
                modelMapper.map(billFoodDetailEntity, billFoodDetailDTO);
                billFoodDetailDTO.setNameFood(billFoodDetailEntity.getSizeFoodEntity().getFoodEntity().getName());
                billFoodDetailDTO.setIdFood(billFoodDetailEntity.getSizeFoodEntity().getFoodEntity().getIdFood());
                billFoodDetailDTO.setSize(billFoodDetailEntity.getSizeFoodEntity().getSizeEntity().getSize());
                billFoodDetailDTO.setPrice(billFoodDetailEntity.getSizeFoodEntity().getPrice());
                billFoodDetailDTOS.add(billFoodDetailDTO);
            }
            billDTO.setBillFoodDetailDTOS(billFoodDetailDTOS);

            billDTOS.add(billDTO);
        }

        dataResponse.setData(billDTOS);
        dataResponse.setMessage("Success");
        dataResponse.setStatus(HttpStatus.OK);
        return dataResponse;
    }
}
