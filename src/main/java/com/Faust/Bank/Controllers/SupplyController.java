package com.Faust.Bank.Controllers;

import com.Faust.Bank.Entities.Supply;
import com.Faust.Bank.Exceptions.IssueNotFoundException;
import com.Faust.Bank.Exceptions.SupplyNotFoundException;
import com.Faust.Bank.Repositories.SupplyRespository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@RestController
public class SupplyController {

    private SupplyRespository suppleRespository;

    public SupplyController(SupplyRespository supplyRepository) {
        this.suppleRespository = supplyRepository;
    }

    @GetMapping("v1/treasury/supplies")
    public ResponseEntity GetAllSupplies(){
        return ok(suppleRespository.findAll());
    }

    @GetMapping("v1/treasury/supplies/{id}")
    public ResponseEntity GetSupply(@PathVariable Long id){
        return ok(suppleRespository.findById(id)
                .orElseThrow(() -> new SupplyNotFoundException(id)));
    }

    @DeleteMapping("/v1/treasury/supplies/{id}")
    public ResponseEntity DeleteSupply(@PathVariable Long id){
        Supply existed = suppleRespository.findById(id)
                .orElseThrow(() -> new SupplyNotFoundException(id));
        suppleRespository.delete(existed);
        return noContent().build();
    }

    @PostMapping("/v1/treasury/supplies")
    public ResponseEntity CreateSupply(){
        Supply supply = suppleRespository.save(Supply._builder().build());
        return noContent().build();
    }
}
