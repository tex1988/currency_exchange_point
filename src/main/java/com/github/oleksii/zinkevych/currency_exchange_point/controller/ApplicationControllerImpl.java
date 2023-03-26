package com.github.oleksii.zinkevych.currency_exchange_point.controller;

import java.util.List;

import com.github.oleksii.zinkevych.currency_exchange_point.controller.common.ApplicationController;
import com.github.oleksii.zinkevych.currency_exchange_point.model.request.ApplicationConfirmRequest;
import com.github.oleksii.zinkevych.currency_exchange_point.model.request.ApplicationCreateRequest;
import com.github.oleksii.zinkevych.currency_exchange_point.model.response.ApplicationModel;
import com.github.oleksii.zinkevych.currency_exchange_point.service.common.ApplicationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ApplicationControllerImpl implements ApplicationController {

    private final ApplicationService applicationService;

    @Override
    public ApplicationModel get(Long id) {
        return applicationService.findById(id);
    }

    @Override
    public List<ApplicationModel> getAll() {
        return applicationService.findAll();
    }

    @Override
    public ApplicationModel createApplication(ApplicationCreateRequest applicationCreateRequest) {
        return applicationService.createApplication(applicationCreateRequest);
    }

    @Override
    public ApplicationModel confirmApplication(ApplicationConfirmRequest confirmRequest) {
        return applicationService.confirmApplication(confirmRequest.getId(), confirmRequest.getOtp());
    }
}
