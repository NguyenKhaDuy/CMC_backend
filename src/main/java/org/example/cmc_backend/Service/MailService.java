package org.example.cmc_backend.Service;

import org.springframework.stereotype.Service;

@Service
public interface MailService {
    void sendEmail(String toEmail, String subject, String content);
}
