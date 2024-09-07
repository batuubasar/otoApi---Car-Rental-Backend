package com.otocom.repositories;

import com.otocom.entities.Oto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtoRepository extends JpaRepository<Oto, Integer> {
}
