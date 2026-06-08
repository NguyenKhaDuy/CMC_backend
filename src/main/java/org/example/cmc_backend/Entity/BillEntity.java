package org.example.cmc_backend.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "bill")
public class BillEntity {
    @Id
    @Column(name = "id_bill")
    private String idBill;

    @Column(name = "created_at")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Lob
    @Column(name = "qr", columnDefinition = "LONGBLOB")
    private byte[] qr;

    @OneToMany(mappedBy = "billEntity", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<TicketEntity> ticketEntities = new ArrayList<>();

    @OneToMany(mappedBy = "billEntity", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<BillFoodDetailEntity> billFoodDetailEntities = new ArrayList<>();

    @OneToMany(mappedBy = "billEntity", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<BillDrinkDetailEntity> billDrinkDetailEntities = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "id_voucher")
    private VoucherEntity voucherEntity;

    @ManyToOne
    @JoinColumn(name = "id_branch")
    private BranchEntity branchEntity;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private UserEntity userEntity;
}
