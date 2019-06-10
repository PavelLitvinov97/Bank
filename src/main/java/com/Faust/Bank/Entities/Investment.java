package com.Faust.Bank.Entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="investments")
@Data
@Builder(builderMethodName = "_builder")
@AllArgsConstructor
@NoArgsConstructor
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ;

    @Column
    private Long amount;

    @Column
    private Long value;

    @Column
    private String owner;
}
