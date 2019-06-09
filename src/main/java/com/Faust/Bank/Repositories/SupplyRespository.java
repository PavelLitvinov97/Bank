package com.Faust.Bank.Repositories;

import com.Faust.Bank.Entities.Supply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplyRespository extends JpaRepository<Supply, Long> {
}
