package test.gd.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "accounts")
@Getter
@Setter
public class Account {
    @Id
    private String iban;

    private double balance;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "senderIban")
    private List<Payment> payments;
}
