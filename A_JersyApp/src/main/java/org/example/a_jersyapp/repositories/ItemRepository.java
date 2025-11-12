package org.example.a_jersyapp.repositories;

import org.example.a_jersyapp.entities.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findByCategory_Id(Long categoryId, Pageable pageable);



    @Query(value = "SELECT i FROM Item i JOIN FETCH i.category",
            countQuery = "SELECT COUNT(i) FROM Item i")
    Page<Item> findAllWithCategoryJoinFetch(Pageable pageable);

    @Query(value = "SELECT i FROM Item i JOIN FETCH i.category c WHERE c.id = :cid",
            countQuery = "SELECT COUNT(i) FROM Item i WHERE i.category.id = :cid")
    Page<Item> findByCategoryIdWithCategoryJoinFetch(@Param("cid") Long categoryId, Pageable pageable);
}
