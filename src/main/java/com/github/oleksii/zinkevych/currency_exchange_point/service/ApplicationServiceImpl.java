package com.github.oleksii.zinkevych.currency_exchange_point.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.StreamSupport;

import com.github.oleksii.zinkevych.currency_exchange_point.constant.ApplicationStatus;
import com.github.oleksii.zinkevych.currency_exchange_point.constant.ExchangeMode;
import com.github.oleksii.zinkevych.currency_exchange_point.entity.Application;
import com.github.oleksii.zinkevych.currency_exchange_point.model.request.ApplicationCreateRequest;
import com.github.oleksii.zinkevych.currency_exchange_point.model.response.ApplicationModel;
import com.github.oleksii.zinkevych.currency_exchange_point.repository.ApplicationRepository;
import com.github.oleksii.zinkevych.currency_exchange_point.service.common.ApplicationService;
import com.github.oleksii.zinkevych.currency_exchange_point.service.exception.ApplicationServiceNotConfirmedException;
import com.github.oleksii.zinkevych.currency_exchange_point.service.exception.ApplicationServiceNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final static String APPLICATION_NOT_FOUND = "Application id: %s not found";
    private final static String NOT_CONFIRMED = "Application was not confirmed";

    private final ApplicationRepository applicationRepository;

    private final NewExchangeServiceImpl exchangeService;
    private final ModelMapper modelMapper;

    @Override
    public ApplicationModel findById(Long id) {
        Application application = applicationRepository.findById(id)
            .orElseThrow(() -> new ApplicationServiceNotFoundException(APPLICATION_NOT_FOUND));
        return modelMapper.map(application, ApplicationModel.class);
    }

    @Override
    public List<ApplicationModel> findAll() {
        Iterable<Application> applications = applicationRepository.findAll();
        return StreamSupport.stream(applications.spliterator(), false)
            .map(application -> modelMapper.map(application, ApplicationModel.class))
            .toList();
    }

    @Override
    public ApplicationModel createApplication(ApplicationCreateRequest applicationCreateRequest) {
        Application application = modelMapper.map(applicationCreateRequest, Application.class);
        application.setCreateDate(LocalDateTime.now());
        application.setDealAmount(calculateApplication(applicationCreateRequest));
        application.setStatus(ApplicationStatus.NEW);
        application.setOtp(new Random().nextInt(999999 - 100000) + 100000);
        Application savedApplication = applicationRepository.save(application);
        return modelMapper.map(savedApplication, ApplicationModel.class);
    }

    @Override
    public ApplicationModel confirmApplication(long id, int otp) {
        ApplicationStatus status;
        Application application = applicationRepository.findById(id)
            .orElseThrow(() -> new ApplicationServiceNotFoundException(String.format(APPLICATION_NOT_FOUND, id)));
        if (otp == application.getOtp()) {
            status = ApplicationStatus.CONFIRMED;
        } else {
            throw new ApplicationServiceNotConfirmedException(NOT_CONFIRMED);
        }
        application.setStatus(status);
        application.setConfirmDate(LocalDateTime.now());
        Application savedApplication = applicationRepository.save(application);
        return modelMapper.map(savedApplication, ApplicationModel.class);
    }

    private BigDecimal calculateApplication(ApplicationCreateRequest applicationCreateRequest) {
        String saleCurrency = applicationCreateRequest.getSaleCurrency();
        String purchaseCurrency = applicationCreateRequest.getPurchaseCurrency();
        BigDecimal purchaseAmount = applicationCreateRequest.getPurchaseAmount();
        ExchangeMode exchangeMode = applicationCreateRequest.getExchangeMode();
        String proxyCurrency = applicationCreateRequest.getProxyCurrency();
        return exchangeService.calculateExchange(saleCurrency, purchaseCurrency, purchaseAmount, exchangeMode, proxyCurrency);
    }
}
