package org.example.cmc_backend.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "room")
public class RoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRoom;

    @Column(name = "capacity")
    private Long capacity;

    @Column(name = "name")
    private String name;

    @Column(name = "total_area")
    private String totalArea;

    @ManyToOne()
    @JoinColumn(name = "id_branch")
    private BranchEntity branchEntity;

    @OneToMany(mappedBy = "roomEntity", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    List<SeatEntity> seatEntities = new ArrayList<>();

    @OneToMany(mappedBy = "roomEntity", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    List<ScheduleEntity> scheduleEntities = new ArrayList<>();
}
