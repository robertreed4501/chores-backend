package com.robertreed4501.choresbackend.registration;

import com.robertreed4501.choresbackend.email.EmailSender;
import com.robertreed4501.choresbackend.registration.token.ConfirmationToken;
import com.robertreed4501.choresbackend.registration.token.ConfirmationTokenService;
import com.robertreed4501.choresbackend.user.AppUser;
import com.robertreed4501.choresbackend.user.AppUserRole;
import com.robertreed4501.choresbackend.user.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;

    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if (!isValidEmail) {
            throw new IllegalStateException("Email not valid");
        }
        String token = appUserService.signUpUser(new AppUser(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword(),
                        AppUserRole.USER
                )
        );
        emailSender.send(request.getEmail(), buildEmail(request.getFirstName(), "http://10.0.0.18:8080/api/v1/registration/confirm?token=" + token));
        return token;
    }

    private String buildEmail(String firstName, String s) {
        return "<a href=" + s + ">Click here to confirm email address</a>";
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token)
                .orElseThrow(() -> new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            //TODO if all attributes match and email is not confirmed, send another verification email
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())){
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(
                confirmationToken.getAppUser().getEmail()
        );

        return "confirmed";
    }
}
