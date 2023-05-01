package com.sweetpeatime.sweetpeatime.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name="Pack")
public class Pack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer flowerId;
    private Integer unitPack;
    private String unit;
    private Double packWeight;
    private String size;
    private Integer priceId; // ref > Flower price
}
