package com.robertreed4501.choresbackend.email;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public interface EmailSender {
    void send(String to, String email);
}
