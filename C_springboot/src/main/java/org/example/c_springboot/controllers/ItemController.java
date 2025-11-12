package org.example.c_springboot.controllers;


import jakarta.validation.Valid;
import org.example.c_springboot.entities.Item;
import org.example.c_springboot.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        return itemRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<Item>> getItems(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String mode,
            Pageable pageable) {

        Page<Item> itemPage;
        if (categoryId != null) {
            if ("join-filter".equalsIgnoreCase(mode) || "join-fetch".equalsIgnoreCase(mode)) {
                itemPage = itemRepository.findByCategory_IdWithJoinFetch(categoryId, pageable);
            } else {
                itemPage = itemRepository.findByCategory_Id(categoryId, pageable);
            }
        } else {
            itemPage = itemRepository.findAll(pageable);
        }
        return ResponseEntity.ok(itemPage);
    }

    @PostMapping
    public ResponseEntity<Item> createItem(@Valid @RequestBody Item item) {
        Item savedItem = itemRepository.save(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @Valid @RequestBody Item itemDetails) {
        Optional<Item> optionalItem = itemRepository.findById(id);

        if (optionalItem.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Item existingItem = optionalItem.get();
        existingItem.setName(itemDetails.getName());
        existingItem.setSku(itemDetails.getSku());
        existingItem.setPrice(itemDetails.getPrice());
        existingItem.setCategory(itemDetails.getCategory());

        Item updatedItem = itemRepository.save(existingItem);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        if (!itemRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        itemRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}