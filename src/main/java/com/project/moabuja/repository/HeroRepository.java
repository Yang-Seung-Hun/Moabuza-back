package com.project.moabuja.repository;

import com.project.moabuja.domain.hero.Hero;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeroRepository extends JpaRepository<Hero, Long> {
}
