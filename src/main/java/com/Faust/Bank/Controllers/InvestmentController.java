package com.Faust.Bank.Controllers;

import com.Faust.Bank.Config.JwtTokenUtil;
import com.Faust.Bank.Entities.Investment;
import com.Faust.Bank.Exceptions.InvestmentNotFoundException;
import com.Faust.Bank.Repositories.InvestmentRepository;
import jdk.internal.cmm.SystemResourcePressureImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.*;

@RestController
public class InvestmentController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private InvestmentRepository investmentRepository;

    public InvestmentController(InvestmentRepository investmentRepository) {
        this.investmentRepository = investmentRepository;
    }

    @GetMapping("/v1/treasury/investments")
    public ResponseEntity GetAllInvestments(@RequestHeader(value = "Authorization") String token) {
        String username = jwtTokenUtil.getUsernameFromToken(token.substring(7));
        System.out.println(username);
        if (username.equals("jester")) {
            return ok(investmentRepository.findAll());
        } else {
            return status(403).build();
        }
    }

    @GetMapping("/v1/treasury/investments/{id}")
    public ResponseEntity GetInvestment(@PathVariable Long id, @RequestHeader(value = "Authorization") String token) {
        String username = jwtTokenUtil.getUsernameFromToken(token.substring(7));
        if (username.equals("jester")) {
            return ok(investmentRepository.findById(id)
                    .orElseThrow(() -> new InvestmentNotFoundException(id)));
        } else {
            return status(403).build();
        }
    }

    @PostMapping("/v1/treasury/investments")
    public ResponseEntity CreateInvestment(@RequestBody Investment investment,@RequestHeader(value = "Authorization") String token) {
        String username = jwtTokenUtil.getUsernameFromToken(token.substring(7));
        if (username.equals("jester")) {
            investment.setOwner(username);
            Investment created = investmentRepository.saveAndFlush(investment);
            return ok(created);
        }
        else {
            return status(403).build();
        }
    }
}
