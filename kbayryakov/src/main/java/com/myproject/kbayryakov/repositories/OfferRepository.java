package com.myproject.kbayryakov.repositories;

import com.myproject.kbayryakov.models.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface OfferRepository extends JpaRepository<Offer, UUID> {
    List<Offer> findAllByUser_Username(String name);

    void deleteAllByCreatedOnBefore(LocalDate createdOn);
}
