package com.github.oleksii.zinkevych.currency_exchange_point.service.common;

import com.github.oleksii.zinkevych.currency_exchange_point.entity.common.ApplicationStatus;
import com.github.oleksii.zinkevych.currency_exchange_point.model.request.ApplicationCreateRequest;
import com.github.oleksii.zinkevych.currency_exchange_point.model.response.ApplicationResponse;

public interface ApplicationService {
    ApplicationResponse createApplication(ApplicationCreateRequest dto);

    ApplicationStatus confirmApplication(long id, int otp);
}
