package com.Faust.Bank.Repositories;

import com.Faust.Bank.Entities.Investment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestmentRepository extends JpaRepository<Investment, Long> {
}
