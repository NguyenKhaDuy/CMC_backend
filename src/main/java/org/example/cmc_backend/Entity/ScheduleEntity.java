package org.example.cmc_backend.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "schedule")
public class ScheduleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSchedule;

    @Column(name = "base_price")
    private Double basePrice;

    @Column(name = "date")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;

    @Column(name = "time_start")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime timeStart;

    @Column(name = "time_end")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime timeEnd;

    @ManyToOne()
    @JoinColumn(name = "id_movie")
    private MovieEntity movieEntity;

    @ManyToOne()
    @JoinColumn(name = "id_room")
    private RoomEntity roomEntity;

    @OneToMany(mappedBy = "scheduleEntity", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<AIChatEntity> aiChatEntities = new ArrayList<>();

    @OneToMany(mappedBy = "scheduleEntity", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<BillEntity> billEntities = new ArrayList<>();

    @OneToMany(mappedBy = "scheduleEntity", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<StatusSeatEntity> statusSeatEntities = new ArrayList<>();
}
