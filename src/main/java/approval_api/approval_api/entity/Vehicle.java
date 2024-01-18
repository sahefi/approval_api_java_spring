package approval_api.approval_api.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO )
    private UUID id;

    private String name;

    @Column(name = "fuel_consumption")
    private BigDecimal fuelConsumption;

    @Column(name = "service_schedule")
    private LocalDate serviceSchedule;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private VehicleStatus status;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @OneToMany(mappedBy = "vehicle")
    private List<Booking> booking;

    public enum VehicleStatus{
        BOOKED,
        AVAILABLE
    }


}
