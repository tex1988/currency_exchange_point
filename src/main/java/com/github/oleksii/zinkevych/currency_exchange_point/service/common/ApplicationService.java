package com.github.oleksii.zinkevych.currency_exchange_point.service.common;

import java.util.List;

import com.github.oleksii.zinkevych.currency_exchange_point.model.request.ApplicationCreateRequest;
import com.github.oleksii.zinkevych.currency_exchange_point.model.response.ApplicationModel;

public interface ApplicationService {

    ApplicationModel findById(Long id);

    List<ApplicationModel> findAll();

    ApplicationModel createApplication(ApplicationCreateRequest dto);

    ApplicationModel confirmApplication(long id, int otp);
}
