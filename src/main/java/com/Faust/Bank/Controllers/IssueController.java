package com.Faust.Bank.Controllers;
import com.Faust.Bank.Config.JwtTokenUtil;
import com.Faust.Bank.Exceptions.IssueNotFoundException;
import com.Faust.Bank.Entities.Issue;
import com.Faust.Bank.Repositories.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@RestController
public class IssueController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private IssueRepository issueRepository;

    public IssueController(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    @GetMapping("/v1/treasury/issues")
    public ResponseEntity GetAllIssues(@RequestHeader(value = "Authorization") String token){
        String username = jwtTokenUtil.getUsernameFromToken(token.substring(7));
        if((username == "emperor") || (username == "treasurer")) {
            return ok(issueRepository.findAll());
        }
        else return status(403).build();

    }

    @GetMapping("/v1/treasury/issues{id}")
    public ResponseEntity GetIssue(@PathVariable Long id, @RequestHeader(value = "Authorization") String token){
        String username = jwtTokenUtil.getUsernameFromToken(token.substring(7));
        if((username == "emperor") || (username == "treasurer")) {
            return ok(issueRepository.findById(id)
                    .orElseThrow(() -> new IssueNotFoundException(id)));
        }
        else {return ok(403 + username); }
    }

    @DeleteMapping("/v1/treasury/issues/{id}")
    public ResponseEntity DeleteIssue(@PathVariable Long id, @RequestHeader(value = "Authorization") String token) {
        String username = jwtTokenUtil.getUsernameFromToken(token.substring(7));
        if ((username == "emperor") || (username == "treasurer")) {
            Issue existed = issueRepository.findById(id)
                    .orElseThrow(() -> new IssueNotFoundException(id));
            issueRepository.delete(existed);
            return status(200).build();
        }
        else return status(403).build();
    }

    @PostMapping("/v1/treasury/issues")
    public ResponseEntity CreateIssue(@RequestBody Issue issue, @RequestHeader(value = "Authorization") String token){
        String username = jwtTokenUtil.getUsernameFromToken(token.substring(7));
        if ((username == "emperor") || (username == "treasurer")) {
            issue.setOwner(username);
            Issue created = issueRepository.save(issue);
            return ok(created);
        }
        else return status(403).build();
    }

    @PutMapping("/v1/treasury/issues/{id}")
    public ResponseEntity UpdateIssue(@RequestBody Issue issue, @PathVariable Long id, @RequestHeader(value = "Authorization") String token){
        String username = jwtTokenUtil.getUsernameFromToken(token.substring(7));
        Issue issue1 = issueRepository.findById(id).
                orElseThrow(() -> new IssueNotFoundException(id));

        if (username == "treasurer") {
            issue1.setAmount(issue.getAmount());
            issue1.setValue(issue.getValue());
            issue1.setStatus(issue.getStatus());
            issueRepository.saveAndFlush(issue1);
            return ok(issue1);
        }
        else if(username == "emperor") {
            issue1.setStatus("Approved!");
            issueRepository.saveAndFlush(issue1);
            return status(200).build();
        }
        return status(403).build();
    }

}
