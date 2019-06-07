package com.Faust.Bank.Entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="supplies")
@Data
@Builder(builderMethodName = "_builder")
@AllArgsConstructor
@NoArgsConstructor
public class Supply {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ;

    @Column
    private String name;
}
