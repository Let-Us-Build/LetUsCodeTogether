package com.LetUsCodeTogether.ats.repo;

import com.LetUsCodeTogether.ats.entity.ApiScheduler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiSchedulerRepository extends JpaRepository<ApiScheduler, Long> {

}
