package com.Faust.Bank.Repositories;

import com.Faust.Bank.Entities.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, Long> {

}
