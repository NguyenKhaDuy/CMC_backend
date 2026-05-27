package org.example.cmc_backend.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "voucher")
public class VoucherEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVoucher;

    @Column(name = "code")
    private String code;

    @Column(name = "created_at")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @Column(name = "expiration")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate expiration;

    @Column(name = "discount")
    private Long discount;

    @Column(name = "quality")
    private Long quality;

    @OneToMany(mappedBy = "voucherEntity", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<BillEntity> billEntities = new ArrayList<>();

}