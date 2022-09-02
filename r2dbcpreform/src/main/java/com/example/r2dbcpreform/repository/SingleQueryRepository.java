package com.example.r2dbcpreform.repository;

import com.example.r2dbcpreform.entity.SingleQuery;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SingleQueryRepository extends R2dbcRepository<SingleQuery, Integer> {
}
