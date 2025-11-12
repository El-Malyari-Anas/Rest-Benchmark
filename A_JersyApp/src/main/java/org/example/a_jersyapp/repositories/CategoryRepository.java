package org.example.a_jersyapp.repositories;

import org.example.a_jersyapp.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
