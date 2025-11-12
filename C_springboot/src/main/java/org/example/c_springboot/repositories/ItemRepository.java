package org.example.c_springboot.repositories;

import org.example.c_springboot.entities.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
        Page<Item> findByCategory_Id(Long categoryId, Pageable pageable);
        Optional<Item> findBySku(String sku);

        @Query(value = "select i from Item i join fetch i.category c where c.id = :categoryId",
           countQuery = "select count(i) from Item i where i.category.id = :categoryId")
    Page<Item> findByCategory_IdWithJoinFetch(@Param("categoryId") Long categoryId, Pageable pageable);

}
