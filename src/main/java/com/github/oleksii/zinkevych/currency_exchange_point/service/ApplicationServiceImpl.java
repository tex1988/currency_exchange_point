package com.github.oleksii.zinkevych.currency_exchange_point.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

import com.github.oleksii.zinkevych.currency_exchange_point.constant.ApplicationStatus;
import com.github.oleksii.zinkevych.currency_exchange_point.constant.ExchangeMode;
import com.github.oleksii.zinkevych.currency_exchange_point.entity.Application;
import com.github.oleksii.zinkevych.currency_exchange_point.model.request.ApplicationCreateRequest;
import com.github.oleksii.zinkevych.currency_exchange_point.model.response.ApplicationResponse;
import com.github.oleksii.zinkevych.currency_exchange_point.repository.ApplicationRepository;
import com.github.oleksii.zinkevych.currency_exchange_point.service.common.ApplicationService;
import com.github.oleksii.zinkevych.currency_exchange_point.service.exception.ApplicationServiceNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final static String APPLICATION_NOT_FOUND = "Application id: %s not found";

    private final ApplicationRepository applicationRepository;

    private final NewExchangeServiceImpl exchangeService;
    private final ModelMapper modelMapper;

    @Override
    public ApplicationResponse createApplication(ApplicationCreateRequest applicationCreateRequest) {
        Application application = modelMapper.map(applicationCreateRequest, Application.class);
        application.setCreateDate(LocalDateTime.now());
        application.setDealAmount(calculateApplication(applicationCreateRequest));
        application.setStatus(ApplicationStatus.NEW);
        application.setOtp(new Random().nextInt(999999 - 100000) + 100000);
        applicationRepository.save(application);
        return modelMapper.map(application, ApplicationResponse.class);
    }

    @Override
    public ApplicationStatus confirmApplication(long id, int otp) {
        ApplicationStatus status;
        Application application = applicationRepository.findById(id)
            .orElseThrow(() -> new ApplicationServiceNotFoundException(String.format(APPLICATION_NOT_FOUND, id)));
        if (otp == application.getOtp()) {
            status = ApplicationStatus.COMPLETED;
        } else {
            status = ApplicationStatus.CANCELED;
        }
        application.setStatus(status);
        applicationRepository.save(application);
        return status;
    }

    private BigDecimal calculateApplication(ApplicationCreateRequest applicationCreateRequest) {
        String saleCurrency = applicationCreateRequest.getSaleCurrency();
        String purchaseCurrency = applicationCreateRequest.getPurchaseCurrency();
        BigDecimal purchaseAmount = applicationCreateRequest.getPurchaseAmount();
        ExchangeMode exchangeMode = applicationCreateRequest.getExchangeMode();
        return exchangeService.calculateExchange(saleCurrency, purchaseCurrency, purchaseAmount, exchangeMode);
    }
}
