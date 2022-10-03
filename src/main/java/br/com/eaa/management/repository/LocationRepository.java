package br.com.eaa.management.repository;

import br.com.eaa.management.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
