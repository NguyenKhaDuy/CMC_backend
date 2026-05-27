package org.example.cmc_backend.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "branch")
public class BranchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBranch;

    @Column(name = "address")
    private String address;

    @Column(name = "name_branch")
    private String nameBranch;

    @Column(name = "phone")
    private String phone;

    @OneToMany(mappedBy = "branchEntity", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    List<ScheduleEntity> scheduleEntities = new ArrayList<>();
}
