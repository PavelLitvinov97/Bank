package com.Faust.Bank.Controllers;

import com.Faust.Bank.Config.JwtTokenUtil;
import com.Faust.Bank.Entities.Supply;
import com.Faust.Bank.Exceptions.IssueNotFoundException;
import com.Faust.Bank.Exceptions.SupplyNotFoundException;
import com.Faust.Bank.Repositories.SupplyRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.*;

@RestController
public class SupplyController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private SupplyRespository suppleRespository;

    public SupplyController(SupplyRespository supplyRepository) {
        this.suppleRespository = supplyRepository;
    }

    @GetMapping("/v1/treasury/supplies")
    public ResponseEntity GetAllSupplies(@RequestHeader(value = "Authorization") String token){
        String username = jwtTokenUtil.getUsernameFromToken(token.substring(7));
        if((username == "provider") || (username == "treasurer")) {
            return ok(suppleRespository.findAll());
        }
        else return status(403).build();
    }

    @GetMapping("/v1/treasury/supplies/{id}")
    public ResponseEntity GetSupply(@PathVariable Long id, @RequestHeader(value = "Authorization") String token){
        String username = jwtTokenUtil.getUsernameFromToken(token.substring(7));
        if((username == "provider") || (username == "treasurer")) {
            return ok(suppleRespository.findById(id)
                    .orElseThrow(() -> new SupplyNotFoundException(id)));
        }
        else {
            return status(403).build();
        }
    }

    @DeleteMapping("/v1/treasury/supplies/{id}")
    public ResponseEntity DeleteSupply(@PathVariable Long id, @RequestHeader(value = "Authorization") String token){
        String username = jwtTokenUtil.getUsernameFromToken(token.substring(7));
        if((username == "provider") || (username == "treasurer")) {
            Supply existed = suppleRespository.findById(id)
                    .orElseThrow(() -> new SupplyNotFoundException(id));
            suppleRespository.delete(existed);
            return status(200).build();
        }
        else {
            return status(403).build();
        }
    }

    @PostMapping("/v1/treasury/supplies")
    public ResponseEntity CreateSupply(@RequestBody Supply supply, @RequestHeader(value = "Authorization") String token){
        String username = jwtTokenUtil.getUsernameFromToken(token.substring(7));
        if((username == "provider") || (username == "treasurer")) {
            supply.setOwner(username);
            Supply created = suppleRespository.saveAndFlush(supply);
            return ok(created);
        }
        else {
            return status(403).build();
        }
    }

    @PutMapping("/v1/treasury/supplies/{id}")
    public ResponseEntity UpdateSupply(@PathVariable Long id, @RequestBody Supply supply, @RequestHeader(value = "Authorization") String token) {
        String username = jwtTokenUtil.getUsernameFromToken(token.substring(7));
        Supply supply1 = suppleRespository.findById(id)
                .orElseThrow(() -> new SupplyNotFoundException(id));
        if(username == "provider"){
            supply1.setGood(supply.getGood());
            supply1.setPrice(supply.getPrice());
            supply1.setStatus(supply.getStatus());
            suppleRespository.saveAndFlush(supply1);
            return ok(supply1);
        }
        else if (username == "treasurer") {
            supply.setStatus("Approved");
            suppleRespository.saveAndFlush(supply1);
            return ok(supply1);
        }
        else return status(403).build();

    }

}


