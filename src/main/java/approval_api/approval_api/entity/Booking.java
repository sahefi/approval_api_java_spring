package approval_api.approval_api.entity;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    private String driver;

    private String applicant;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id",referencedColumnName = "id")
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.EAGER )
    @JoinColumn(name = "approver_id",referencedColumnName = "id")
    private User approver;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookStatus status;

    @Column(name = "start_book")
    private LocalDate startBook;

    @Column(name = "end_book")
    private LocalDate EndBook;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    public enum BookStatus{
        PENDING,
        APPROVED,
        REJECTED
    }
}
