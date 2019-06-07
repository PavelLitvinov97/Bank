package com.Faust.Bank.Controllers;
import com.Faust.Bank.Exceptions.IssueNotFoundException;
import com.Faust.Bank.Entities.Issue;
import com.Faust.Bank.Repositories.IssueRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@RestController
public class IssueController {

    private IssueRepository issueRepository;

    public IssueController(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    @GetMapping("/v1/treasury/issues")
    public ResponseEntity GetAllIssues(){
        return ok(issueRepository.findAll());
    }

    @GetMapping("v1/treasury/issues/{id}")
    public ResponseEntity GetIssue(@PathVariable Long id){
        return ok(issueRepository.findById(id)
                .orElseThrow(() -> new IssueNotFoundException(id)));
    }

    @DeleteMapping("v1/treasury/issues/{id}")
    public ResponseEntity DeleteIssue(@PathVariable Long id){
        Issue existed = issueRepository.findById(id)
                .orElseThrow(() -> new IssueNotFoundException(id));
        issueRepository.delete(existed);
        return noContent().build();
    }

    @PostMapping("/v1/treasury/issues")
    public ResponseEntity CreateIssue(){
        Issue issue = issueRepository.save(Issue._builder().build());
        return noContent().build();
    }

}
