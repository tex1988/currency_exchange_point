package com.github.oleksii.zinkevych.currency_exchange_point.controller;

import com.github.oleksii.zinkevych.currency_exchange_point.controller.common.ApplicationController;
import com.github.oleksii.zinkevych.currency_exchange_point.model.request.ApplicationConfirmRequest;
import com.github.oleksii.zinkevych.currency_exchange_point.model.request.ApplicationCreateRequest;
import com.github.oleksii.zinkevych.currency_exchange_point.model.response.ApplicationResponse;
import com.github.oleksii.zinkevych.currency_exchange_point.service.common.ApplicationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ApplicationControllerImpl implements ApplicationController {
    private final ApplicationService applicationService;

    @Override
    public ApplicationResponse createApplication(ApplicationCreateRequest applicationCreateRequest) {
        return  applicationService.createApplication(applicationCreateRequest);
    }

    @Override
    public void confirmApplication(ApplicationConfirmRequest confirmRequest) {
        applicationService.confirmApplication(confirmRequest.getId(), confirmRequest.getOtp());
    }
}
