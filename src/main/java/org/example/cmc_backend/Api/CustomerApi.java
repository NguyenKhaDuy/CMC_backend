package org.example.cmc_backend.Api;

import jakarta.servlet.http.HttpServletResponse;
import org.example.cmc_backend.Config.VnPayConfig;
import org.example.cmc_backend.Models.Request.BookingRequest;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Service.BillService;
import org.example.cmc_backend.Utils.RandomIdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class CustomerApi {
    @Autowired
    BillService billService;

//    @PostMapping(value = "/api/customer/booking")
//    public ResponseEntity<Object> booking(@RequestBody BookingRequest bookingRequest) {
//        MessageResponse result = billService.booking(bookingRequest);
//        if (result.getStatus() == HttpStatus.CREATED){
//            return new ResponseEntity<>(result, HttpStatus.CREATED);
//        }
//        return new ResponseEntity<>(result, result.getStatus());
//    }

    @PostMapping(value = "/api/customer/booking")
    public String PaymentInvoice(@RequestBody BookingRequest bookingRequest) throws UnsupportedEncodingException {

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";

        String idBooking = RandomIdUtils.generateRandomId("B", 10);
        bookingRequest.setIdBooking(idBooking);

        MessageResponse result = billService.booking(bookingRequest);
        if (result.getStatus() != HttpStatus.CREATED){
            return null;
        }

        //thay chổ này
        Long amount = bookingRequest.getTotalAmount().longValue() * 100;
        String bankCode = bookingRequest.getBank();

        //chổ này là mã đơn hàng
        String vnp_TxnRef =idBooking;

        String vnp_IpAddr = "127.0.0.1";

        String vnp_TmnCode = VnPayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_BankCode", bankCode);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan hoa don:" + vnp_TxnRef);

        vnp_Params.put("vnp_Locale", "vn");

        String returnUrl = "http://localhost:3000/api/payment-info";

        vnp_Params.put(
                "vnp_ReturnUrl",
                returnUrl
        );

        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VnPayConfig.vnp_PayUrl + "?" + queryUrl;

        return paymentUrl;
    }

    @GetMapping("/api/payment-info")
    public void paymentInfo(
            @RequestParam("vnp_ResponseCode")
            String responseCode,
            @RequestParam("vnp_TxnRef")
            String txnRef,
            HttpServletResponse response
    ) throws IOException {

        boolean success = "00".equals(responseCode);
        String idBooking = txnRef;
        if (success) {
            billService.updateStatusBill(idBooking, "PAIDED");
            response.sendRedirect(
                    "http://localhost:3001/payment/success"
            );
            return;
        }
        billService.updateStatusBill(idBooking, "FAILED");
        response.sendRedirect(
                "http://localhost:3001/payment/failed"
        );
    }

    @GetMapping(value = "/api/customer/bill/id-user={idUser}")
    public ResponseEntity<Object> getBillIdUser(@PathVariable String idUser) {
        Object result = billService.getAllBillsByUser(idUser);
        if (result instanceof MessageResponse) {
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
