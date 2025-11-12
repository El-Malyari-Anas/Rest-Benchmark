package org.example.dspringdata.repositories;

import org.example.dspringdata.entities.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "items", path = "items")
public interface ItemRepository extends JpaRepository<Item, Long> {


    @RestResource(path = "byCategory", rel = "byCategory")
    Page<Item> findByCategory_Id(@Param("categoryId") Long categoryId, Pageable pageable);

    @RestResource(path = "byCategoryJoin", rel = "byCategoryJoin")
    @Query(value = "select i from Item i join fetch i.category c where c.id = :categoryId",
            countQuery = "select count(i) from Item i where i.category.id = :categoryId")
    Page<Item> findByCategory_IdWithJoinFetch(@Param("categoryId") Long categoryId, Pageable pageable);

    Optional<Item> findBySku(String sku);
}
