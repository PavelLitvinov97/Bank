package com.Faust.Bank.Controllers;

import com.Faust.Bank.Entities.Investment;
import com.Faust.Bank.Exceptions.InvestmentNotFoundException;
import com.Faust.Bank.Repositories.InvestmentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@RestController
public class InvestmentController {

    private InvestmentRepository investmentRepository;
    public InvestmentController (InvestmentRepository investmentRepository) {
        this.investmentRepository = investmentRepository;
    }

    @GetMapping("/v1/treasury/investments")
    public ResponseEntity GetAllInvestments(){
        return ok(investmentRepository.findAll());
    }

    @GetMapping("/v1/treasury/investments/{id}")
    public ResponseEntity GetInvestment(@PathVariable Long id){
        return ok(investmentRepository.findById(id)
                .orElseThrow(() -> new InvestmentNotFoundException(id)));
    }

    @PostMapping("/v1/treasury/investments")
    public ResponseEntity DoInvestment(){
        Investment investment = investmentRepository.save(Investment._builder().build());
        return noContent().build();
    }
}
