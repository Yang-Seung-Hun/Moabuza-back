package com.project.moabuja.domain.hero;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Hero {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hero_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private HeroName heroName;

    @Enumerated(EnumType.STRING)
    private HeroLevel heroLevel;

    public Hero(HeroName heroName, HeroLevel heroLevel) {
        this.heroName = heroName;
        this.heroLevel = heroLevel;
    }
}
