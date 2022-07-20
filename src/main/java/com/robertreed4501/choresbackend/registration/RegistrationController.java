package com.robertreed4501.choresbackend.registration;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "api/v1/registration")
@CrossOrigin
@AllArgsConstructor
public class RegistrationController {

    private RegistrationService registrationService;

    @PostMapping
    @CrossOrigin
    public String register(@RequestBody RegistrationRequest request) {
        return RegistrationService.register(request);
    }


}