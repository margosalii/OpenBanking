package test.gd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.gd.model.Payment;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findTop10BySenderIbanIbanOrReceiverIbanOrderByDateTimeDesc(String senderIban, String receiverIban);
}
