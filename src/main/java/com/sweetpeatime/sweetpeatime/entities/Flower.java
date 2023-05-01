package com.sweetpeatime.sweetpeatime.entities;
import lombok.*;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name="Flower")
public class Flower {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String flowerName;
    private String mainCategory;
    private Boolean isStock;
    private Integer lifeTime;
    private String unit;
    private Boolean isFreeze;
    private String flowerCategory;
    private String flowerType;
    private Integer capacity;
    private Integer lotNo;

}
