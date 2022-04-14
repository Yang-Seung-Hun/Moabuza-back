//package com.project.moabuja.domain.member;
//
//import lombok.Getter;
//
//import javax.persistence.*;
//
//@Entity
//@Getter
//public class Wallet {
//
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "wallet_id")
//    private Long id;
//
//    private int wallet;
//
//    private int currentGroupAmount;
//
//    private int currentChallengeAmount;
//
//    @OneToOne
//    @JoinColumn(name = "member_id")
//    private Member member;
//
//    public Wallet() {
//        this.wallet = 0;
//        this.currentGroupAmount = 0;
//        this.currentChallengeAmount = 0;
//    }
//
//    public void addMember(Member member){
//        this.member = member;
//    }
//
//    public void walletIncome(int income) {
//        this.wallet += income;
//    }
//
//    public void walletExpense(int expense) {
//        this.wallet -= expense;
//    }
//
//    public void updateGroup(int group) {
//        this.wallet -= group;
//        this.currentGroupAmount += group;
//    }
//
//    public void updateChallenge(int challenge) {
//        this.wallet -= challenge;
//        this.currentGroupAmount += challenge;
//    }
//}
