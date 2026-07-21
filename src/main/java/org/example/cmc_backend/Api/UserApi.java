package org.example.cmc_backend.Api;

import jakarta.persistence.OneToMany;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.cmc_backend.Entity.RoleEntity;
import org.example.cmc_backend.Entity.UserEntity;
import org.example.cmc_backend.Models.DTO.ChangePasswordRequest;
import org.example.cmc_backend.Models.DTO.LoginDTO;
import org.example.cmc_backend.Models.DTO.UserDTO;
import org.example.cmc_backend.Models.Request.*;
import org.example.cmc_backend.Models.Response.DataPageResponse;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Repository.UserRepository;
import org.example.cmc_backend.Service.MailService;
import org.example.cmc_backend.Service.UserService;
import org.example.cmc_backend.Utils.ConvertByteToBase64;
import org.example.cmc_backend.Utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Random;

@RestController
public class UserApi {
    @Autowired
    UserService userService;
    @Autowired
    JwtTokenUtils jwtTokenUtils;
    @Autowired
    MailService mailService;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/api/me")
    public ResponseEntity<?> getCurrentUser(@CookieValue("token") String token) {
        System.out.println(token);
        String email = jwtTokenUtils.getUsernameFromJWT(token);
        UserEntity user = userRepository.findByEmail(email);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        if (token == null || !jwtTokenUtils.validateToken(token, user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setMessage("Login success");
        loginDTO.setToken(token);
        loginDTO.setId_user(user.getIdUser());
        loginDTO.setFull_name(user.getFullName());
        loginDTO.setAvatarBase64(ConvertByteToBase64.toBase64(user.getAvatar()));
        for (RoleEntity roleEntity : user.getRoleEntities()) {
            loginDTO.getRoles().add(roleEntity.getRole());
        }
        loginDTO.setHttpStatus(HttpStatus.OK);

        return ResponseEntity.ok(loginDTO);
    }

    @PostMapping(value = "/api/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        DataResponse dataResponse = new DataResponse();
        Object result = userService.Login(loginRequest);
        if (result instanceof MessageResponse){
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }

        dataResponse.setData(result);
        dataResponse.setMessage("Success");
        dataResponse.setStatus(HttpStatus.OK);
        Cookie cookie = new Cookie("token", ((LoginDTO) result).getToken());
        cookie.setHttpOnly(false); // Nếu frontend cần đọc token để set Authorization header
        cookie.setPath("/");
        cookie.setMaxAge(3 * 24 * 60 * 60);
        response.addCookie(cookie);
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @PostMapping("/api/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {

        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok("Logged out");
    }

    @PostMapping(value = "/api/register")
    public ResponseEntity<Object> register(@RequestBody RegisterRequest registerRequest, HttpSession session){
        String otpCode = String.format("%06d", new Random().nextInt(999999));

        String emailContent = String.format(
                "Xin chào %s,\n\n" +
                        "Mã xác thực OTP của bạn là: %s\n" +
                        "Mã này có hiệu lực trong 5 phút, vui lòng không chia sẽ mã này cho bất kì ai\n\n" +
                        "Trân trọng!\n",
                registerRequest.getFullName(), otpCode
        );
        mailService.sendEmail(registerRequest.getEmail(), "Mã xác thực otp - CMC", emailContent);

        session.setAttribute("type", "REGISTER");
        session.setAttribute("data", registerRequest);
        session.setAttribute("otpCode", otpCode);
        session.setAttribute("otpExpiry", System.currentTimeMillis() + 300000);

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Success");
        messageResponse.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @PostMapping(value = "/api/customer/avatar")
    public ResponseEntity<Object> updateAvatar(@ModelAttribute UpdateAvatarRequest updateAvatarRequest){
        MessageResponse result = userService.UpdateAvatar(updateAvatarRequest);
        return new ResponseEntity<>(result, result.getStatus());
    }

    @PostMapping("/api/verify-otp")
    public ResponseEntity<Object> verifyOTP(@RequestParam String otpCode, HttpSession session) {
        MessageResponse result = null;
        try {
            String sessionOTP = (String) session.getAttribute("otpCode");
            Long otpExpiry = (Long) session.getAttribute("otpExpiry");
            String type = (String) session.getAttribute("type");

            if (sessionOTP == null || otpExpiry == null) {
                MessageResponse messageResponse = new MessageResponse();
                messageResponse.setMessage("Phiên làm việc đã hết hạn!");
                messageResponse.setStatus(HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
            }

            if (System.currentTimeMillis() > otpExpiry) {
                MessageResponse messageResponse = new MessageResponse();
                messageResponse.setMessage("Mã OTP đã hết hạn!");
                messageResponse.setStatus(HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
            }

            if (!sessionOTP.equals(otpCode)) {
                MessageResponse messageResponse = new MessageResponse();
                messageResponse.setMessage("Mã OTP không đúng!");
                messageResponse.setStatus(HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
            }

            if (type.equals("REGISTER")) {
                RegisterRequest registerRequest = (RegisterRequest) session.getAttribute("data");
                result = userService.Register(registerRequest);
            }

            if(type.equals("UPDATE_EMAIL")){
                UpdateEmailRequest updateEmailRequest = (UpdateEmailRequest) session.getAttribute("data");
                result = userService.updateEmailUser(updateEmailRequest);
            }

            MessageResponse response = new MessageResponse();
            if (type.equals("FORGOT_PASSWORD")) {
                session.removeAttribute("otpCode");
                session.removeAttribute("otpExpiry");
                session.removeAttribute("type");

                response.setMessage("Success");
                response.setStatus(HttpStatus.OK);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            MessageResponse messageResponse = new MessageResponse();
            if (result != null && result.getStatus() == HttpStatus.OK) {
                // Xóa thông tin khỏi session
                session.removeAttribute("data");
                session.removeAttribute("otpCode");
                session.removeAttribute("otpExpiry");
                session.removeAttribute("type");

                messageResponse.setMessage("Success");
                messageResponse.setStatus(HttpStatus.OK);
                return new ResponseEntity<>(messageResponse, HttpStatus.OK);
            } else {
                messageResponse.setMessage("Failed");
                messageResponse.setStatus(HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            e.printStackTrace();
            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setMessage("Co loi xay ra");
            messageResponse.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/api/resend-otp")
    public ResponseEntity<Object> resendOTP(HttpSession session) {
        String type = (String) session.getAttribute("type");
        RegisterRequest registerRequest = null;
        UpdateEmailRequest updateEmailRequest = null;
        String email = null;
        String fullName = null;
        MessageResponse messageResponse = new MessageResponse();
        try {
            if (type.equals("REGISTER")) {
               registerRequest = (RegisterRequest) session.getAttribute("data");
               email = registerRequest.getEmail();
               fullName = registerRequest.getFullName();
            }

            if (type.equals("UPDATE_EMAIL")) {
                updateEmailRequest = (UpdateEmailRequest) session.getAttribute("data");
                email = updateEmailRequest.getEmail();
                fullName = updateEmailRequest.getEmail();
            }

            if (type.equals("FORGOT_PASSWORD")) {
                email = (String) session.getAttribute("data");
            }

            if(!type.equals("FORGOT_PASSWORD")){
                if (registerRequest == null || updateEmailRequest == null) {
                    messageResponse.setMessage("Phiên làm việc đã hết hạn!");
                    messageResponse.setStatus(HttpStatus.BAD_REQUEST);
                    return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
                }
            }

            // Tạo mã OTP mới
            String otpCode = String.format("%06d", new Random().nextInt(999999));

            // Cập nhật session
            session.setAttribute("otpCode", otpCode);
            session.setAttribute("otpExpiry", System.currentTimeMillis() + 300000);
            session.setAttribute("type", type);

            // Gửi email OTP mới
            String emailContent = String.format(
                    "Xin chào %s,\n\n" +
                            "Mã xác thực OTP mới của bạn là: %s\n" +
                            "Mã này có hiệu lực trong 5 phút.\n\n" +
                            "Trân trọng,\n",
                    fullName, otpCode
            );

            mailService.sendEmail(email, "Mã xác thực OTP mới - Houseware Shop", emailContent);

            messageResponse.setMessage("Mã OTP mới đã được gửi!");
            messageResponse.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(messageResponse, messageResponse.getStatus());

        } catch (Exception e) {
            messageResponse.setMessage("Co loi xay ra");
            messageResponse.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
        }
    }

    @PutMapping(value = "/api/customer")
    public ResponseEntity<Object> updateUser(@RequestBody UpdateUserRequest updateUserRequest) {
        MessageResponse result = userService.updateInformation(updateUserRequest);
        return new ResponseEntity<>(result, result.getStatus());
    }

    @PutMapping(value = "/api/customer/email")
    public ResponseEntity<Object> updateUser(@RequestBody UpdateEmailRequest updateEmailRequest, HttpSession session) {
        String otpCode = String.format("%06d", new Random().nextInt(999999));

        String emailContent = String.format(
                "Xin chào %s,\n\n" +
                        "Mã xác thực OTP của bạn là: %s\n" +
                        "Mã này có hiệu lực trong 5 phút, vui lòng không chia sẽ mã này cho bất kì ai\n\n" +
                        "Trân trọng!\n",
                        updateEmailRequest.getEmail(), otpCode
        );
        mailService.sendEmail(updateEmailRequest.getEmail(), "Mã xác thực otp - CMC", emailContent);

        session.setAttribute("type", "UPDATE_EMAIL");
        session.setAttribute("data", updateEmailRequest);
        session.setAttribute("otpCode", otpCode);
        session.setAttribute("otpExpiry", System.currentTimeMillis() + 300000);

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Success");
        messageResponse.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @PostMapping("/api/send-otp/forgotPassword")
    public ResponseEntity<Object> sendOtpForgotPassword(@RequestParam(name = "email") String email, HttpSession session) {
        MessageResponse messageResponse = new MessageResponse();
        UserEntity userEntity = userService.getUserByEmail(email);
        if (userEntity == null) {
            messageResponse.setMessage("Can not found account");
            messageResponse.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
        }

        String otpCode = String.format("%06d", new Random().nextInt(999999));

        String emailContent = String.format(
                "Xin chào %s,\n\n" +
                        "Mã xác thực OTP của bạn là: %s\n" +
                        "Mã này có hiệu lực trong 5 phút, vui lòng không chia sẽ mã này cho bất kì ai\n\n" +
                        "Trân trọng!\n",
                        userEntity.getFullName(), otpCode
        );
        mailService.sendEmail(userEntity.getEmail(), "Mã xác thực otp - CMC", emailContent);

        session.setAttribute("type", "FORGOT_PASSWORD");
        session.setAttribute("data", email);
        session.setAttribute("otpCode", otpCode);
        session.setAttribute("otpExpiry", System.currentTimeMillis() + 300000);

        messageResponse.setMessage("Success");
        messageResponse.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @PostMapping(value = "/api/forgot/password")
    public ResponseEntity<Object> forgotPassword(@RequestParam String password, HttpSession session) {
        String email = (String) session.getAttribute("data");
        if (email != null) {
            ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
            forgotPasswordRequest.setEmail(email);
            forgotPasswordRequest.setNewPassword(password);
            MessageResponse result = userService.forgotPassword(forgotPasswordRequest);
            session.removeAttribute("data");
            return new ResponseEntity<>(result, result.getStatus());
        }
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Failed");
        messageResponse.setStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @PutMapping(value = "/api/password")
    public ResponseEntity<Object> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest) {
        MessageResponse result = userService.updatePassword(updatePasswordRequest);
        return new ResponseEntity<>(result, result.getStatus());
    }

    @GetMapping(value = "/api/customer/id={id}")
    public ResponseEntity<Object> getCustomerById(@PathVariable String id) {
        Object result = userService.getUserById(id);
        if(result instanceof MessageResponse) {
            MessageResponse messageResponse = (MessageResponse) result;
            return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/api/admin/id={id}")
    public ResponseEntity<Object> getCustomerByIdForAdmin(@PathVariable String id) {
        Object result = userService.getUserById(id);
        if(result instanceof MessageResponse) {
            MessageResponse messageResponse = (MessageResponse) result;
            return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/api/admin/users")
    public ResponseEntity<Object> getAdminUsers(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        DataPageResponse dataPageResponse = new DataPageResponse();
        Page<UserDTO> userDTOS = userService.getUsers(pageNo);
        dataPageResponse.setStatus(HttpStatus.OK);
        dataPageResponse.setCurrent_page(pageNo);
        dataPageResponse.setTotal_page(userDTOS.getTotalPages());
        dataPageResponse.setData(userDTOS.getContent());
        dataPageResponse.setMessage("Success");
        return new ResponseEntity<>(dataPageResponse, HttpStatus.OK);
    }
}
