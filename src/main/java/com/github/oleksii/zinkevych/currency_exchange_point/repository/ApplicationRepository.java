package com.github.oleksii.zinkevych.currency_exchange_point.repository;

import com.github.oleksii.zinkevych.currency_exchange_point.entity.Application;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends CrudRepository<Application, Long> {
}
