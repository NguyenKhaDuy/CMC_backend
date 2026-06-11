package org.example.cmc_backend.Service.Implement;

import org.example.cmc_backend.Config.QRCodeGenerator;
import org.example.cmc_backend.Entity.*;
import org.example.cmc_backend.Models.DTO.*;
import org.example.cmc_backend.Models.Request.BookingDrinkRequest;
import org.example.cmc_backend.Models.Request.BookingFoodRequest;
import org.example.cmc_backend.Models.Request.BookingRequest;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Repository.*;
import org.example.cmc_backend.Service.BillService;
import org.example.cmc_backend.Utils.ConvertByteToBase64;
import org.example.cmc_backend.Utils.RandomIdUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @Autowired
    SizeRepository sizeRepository;
    @Autowired
    DrinkRepository drinkRepository;
    @Autowired
    FoodRepository foodRepository;
    @Autowired
    BranchRepository branchRepository;
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    VoucherRepository voucherRepository;
    @Autowired
    SeatRepository seatRepository;
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    SizeDrinkRepository sizeDrinkRepository;
    @Autowired
    SizeFoodRepository sizeFoodRepository;


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

    @Override
    public MessageResponse booking(BookingRequest bookingRequest) {
        MessageResponse messageResponse = new MessageResponse();
        BranchEntity branchEntity = null;
        MovieEntity movieEntity = null;
        DrinkEntity drinkEntity = null;
        FoodEntity foodEntity = null;
        SizeEntity sizeEntity = null;
        UserEntity userEntity = null;
        ScheduleEntity scheduleEntity = null;
        BillEntity billEntity = new BillEntity();
        VoucherEntity voucherEntity = null;
        Long totalPriceTicket = 0L;
        if (bookingRequest.getVoucherCode() != null) {
            try{
                voucherEntity = voucherRepository.findByCode(bookingRequest.getVoucherCode());
                if (LocalDate.now().isBefore(voucherEntity.getExpiration())){
                    billEntity.setVoucherEntity(voucherEntity);
                }else{
                    billEntity.setVoucherEntity(null);
                }
            }catch (NoSuchElementException ex){
                messageResponse.setStatus(HttpStatus.NOT_FOUND);
                messageResponse.setMessage("Voucher Not Found");
                return messageResponse;
            }
        }else {
            billEntity.setVoucherEntity(null);
        }

        try {
            userEntity = userRepository.findById(bookingRequest.getIdUser()).get();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Can not found user");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        try{
            branchEntity = branchRepository.findById(bookingRequest.getIdBranch()).get();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Can not found branch");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        String idBill = RandomIdUtils.generateRandomId("B", 10);
        billEntity.setUserEntity(userEntity);
        billEntity.setBranchEntity(branchEntity);
        billEntity.setCreatedAt(LocalDateTime.now());
        billEntity.setTotalAmount(bookingRequest.getTotalAmount());
        billEntity.setIdBill(idBill);
        try {
            billEntity.setQr(QRCodeGenerator.generateQRCode(idBill));
        } catch (Exception e) {
            messageResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            messageResponse.setMessage("Can not create QR Code");
            return messageResponse;
        }

        try{
            movieEntity = movieRepository.findById(bookingRequest.getIdMovie()).get();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Can not found movie");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }

        List<TicketEntity> ticketEntities = new ArrayList<>();
        try{
            scheduleEntity = scheduleRepository.findByMovieEntityAndIdSchedule(movieEntity, bookingRequest.getIdSchedule());
            for (Long idSeat : bookingRequest.getIdSeats()){
                SeatEntity seatEntity = seatRepository.findById(idSeat).get();
                //tính tổng giá tiền ghế
//                totalPriceTicket = (long) (seatEntity.getSeatTypeEntity().getPriceMultiplier() * scheduleEntity.getBasePrice());
                TicketEntity ticketEntity = new TicketEntity();
                ticketEntity.setSeatEntity(seatEntity);
                ticketEntity.setBillEntity(billEntity);
                ticketEntity.setPriceTicket(seatEntity.getSeatTypeEntity().getPriceMultiplier() * scheduleEntity.getBasePrice());
                ticketEntity.setIdTicket(RandomIdUtils.generateRandomId("T", 10));
                ticketEntities.add(ticketEntity);
            }
            billEntity.setTicketEntities(ticketEntities);
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Can not found schedule");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }

        List<BillDrinkDetailEntity> billDrinkDetailEntities = new ArrayList<>();
        if (!bookingRequest.getBookingDrinkRequests().isEmpty()){
            for (BookingDrinkRequest bookingDrinkRequest : bookingRequest.getBookingDrinkRequests()) {
                drinkEntity = drinkRepository.findById(bookingDrinkRequest.getIdDrink()).get();
                sizeEntity = sizeRepository.findById(bookingDrinkRequest.getIdSize()).get();
                SizeDrinkEntity sizeDrinkEntity = sizeDrinkRepository.findByDrinkEntityAndSizeEntity(drinkEntity, sizeEntity);
                BillDrinkDetailEntity billDrinkDetailEntity = new BillDrinkDetailEntity();
                billDrinkDetailEntity.setQuality(bookingDrinkRequest.getQuality());
                billDrinkDetailEntity.setSizeDrinkEntity(sizeDrinkEntity);
                billDrinkDetailEntity.setBillEntity(billEntity);
                billDrinkDetailEntity.setTotal(sizeDrinkEntity.getPrice() * bookingDrinkRequest.getQuality());
                billDrinkDetailEntities.add(billDrinkDetailEntity);
            }
        }
        billEntity.setBillDrinkDetailEntities(billDrinkDetailEntities);

        List<BillFoodDetailEntity> billFoodDetailEntities = new ArrayList<>();
        if(!bookingRequest.getBookingFoodRequests().isEmpty()){
            for (BookingFoodRequest bookingFoodRequest : bookingRequest.getBookingFoodRequests()) {
                foodEntity = foodRepository.findById(bookingFoodRequest.getIdFood()).get();
                sizeEntity = sizeRepository.findById(bookingFoodRequest.getIdSize()).get();
                SizeFoodEntity sizeFoodEntity = sizeFoodRepository.findByFoodEntityAndSizeEntity(foodEntity, sizeEntity);
                BillFoodDetailEntity billFoodDetailEntity = new BillFoodDetailEntity();
                billFoodDetailEntity.setQuality(bookingFoodRequest.getQuality());
                billFoodDetailEntity.setSizeFoodEntity(sizeFoodEntity);
                billFoodDetailEntity.setBillEntity(billEntity);
                billFoodDetailEntity.setTotal(sizeFoodEntity.getPrice() * bookingFoodRequest.getQuality());
                billFoodDetailEntities.add(billFoodDetailEntity);
            }
        }
        billEntity.setBillFoodDetailEntities(billFoodDetailEntities);
        billRepository.save(billEntity);
        for (Long idSeat : bookingRequest.getIdSeats()){
            SeatEntity seatEntity = seatRepository.findById(idSeat).get();
            seatEntity.setStatus("BOOKED");
            seatRepository.save(seatEntity);
        }
        messageResponse.setStatus(HttpStatus.CREATED);
        messageResponse.setMessage("Created");
        return messageResponse;
    }
}
