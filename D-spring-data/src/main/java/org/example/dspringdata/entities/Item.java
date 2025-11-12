package org.example.dspringdata.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 64)
    private String sku;

    @Column(nullable = false, length = 128)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stock = 0;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    // removed READ_ONLY so nested "category": {"id":...} is accepted on POST/PUT
    private Category category;

    @PrePersist @PreUpdate
    protected void onUpdate() {
        updatedAt = Timestamp.from(Instant.now());
    }

    // getters / setters ...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
    public Category getCategory() { return category; }

    // normalize setter: if nested category contains only id, keep a Category with that id
    public void setCategory(Category category) {
        if (category == null) {
            this.category = null;
            return;
        }
        if (category.getId() != null) {
            Category c = new Category();
            c.setId(category.getId());
            this.category = c;
        } else {
            this.category = category;
        }
    }

    // allow client to send {"categoryId":123} as alternative
    @JsonProperty("categoryId")
    public void setCategoryId(Long categoryId) {
        if (categoryId != null) {
            Category cat = new Category();
            cat.setId(categoryId);
            this.category = cat;
        }
    }
}