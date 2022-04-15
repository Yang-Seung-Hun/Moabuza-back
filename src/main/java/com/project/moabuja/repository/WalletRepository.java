package com.project.moabuja.repository;

import com.project.moabuja.domain.member.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet,Long> {
}
