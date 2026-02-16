package kevinquarta.s7l1.repositories;


import kevinquarta.s7l1.entities.Viaggio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViaggioRepository extends JpaRepository <Viaggio,Long> {
}
