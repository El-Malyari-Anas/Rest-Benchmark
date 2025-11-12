package org.example.a_jersyapp;

import jakarta.transaction.Transactional;
import org.example.a_jersyapp.entities.Category;
import org.example.a_jersyapp.entities.Item;
import org.example.a_jersyapp.repositories.CategoryRepository;
import org.example.a_jersyapp.repositories.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Ce composant est exécuté une seule fois au démarrage de l'application
 * UNIQUEMENT si le profil "init-data" est actif.
 * Son rôle est de peupler la base de données avec un grand volume de données de test,
 * adapté à la nouvelle structure des entités (code, sku, stock, BigDecimal).
 */
@Component
@Profile("init-data") // Ne s'active que si on le demande explicitement
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    // --- Paramètres de la génération ---
    private static final int NOMBRE_CATEGORIES = 2000;
    private static final int NOMBRE_ITEMS = 100000;
    private static final int BATCH_SIZE = 500; // Doit correspondre à la taille du lot JDBC

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (categoryRepository.count() > 0) {
            logger.info("La base de données contient déjà des données. L'initialisation est annulée.");
            return;
        }

        logger.info("Démarrage de la génération de données pour les nouvelles entités...");

        // --- 1. Création des catégories ---
        logger.info("Création de {} catégories...", NOMBRE_CATEGORIES);
        List<Category> categoriesToSave = new ArrayList<>();
        for (int i = 1; i <= NOMBRE_CATEGORIES; i++) {
            Category category = new Category();
            category.setName("Catégorie " + i);
            // Génération d'un code unique
            category.setCode(String.format("CAT-%04d", i));
            categoriesToSave.add(category);
        }
        // Sauvegarde toutes les catégories et les récupère avec leurs IDs
        List<Category> savedCategories = categoryRepository.saveAll(categoriesToSave);
        logger.info("Catégories créées avec succès.");


        // --- 2. Création des items par lots (batch) ---
        logger.info("Création de {} items...", NOMBRE_ITEMS);
        List<Item> itemsBatch = new ArrayList<>(BATCH_SIZE);
        Random random = new Random();

        for (int i = 1; i <= NOMBRE_ITEMS; i++) {
            Item item = new Item();
            item.setName("Item " + i);
            // Génération d'un SKU (Stock Keeping Unit) unique
            item.setSku(String.format("SKU-%06d", i));

            // Génération d'un prix aléatoire de type BigDecimal
            double randomPrice = ThreadLocalRandom.current().nextDouble(5.0, 1500.0);
            item.setPrice(BigDecimal.valueOf(randomPrice).setScale(2, RoundingMode.HALF_UP));

            // Génération d'un stock aléatoire
            item.setStock(ThreadLocalRandom.current().nextInt(0, 501)); // Stock entre 0 et 500

            // Association à une catégorie aléatoire
            Category randomCategory = savedCategories.get(random.nextInt(savedCategories.size()));
            item.setCategory(randomCategory);

            itemsBatch.add(item);

            // Sauvegarde du lot lorsqu'il est plein pour optimiser les performances
            if (i % BATCH_SIZE == 0 || i == NOMBRE_ITEMS) {
                itemRepository.saveAll(itemsBatch);
                itemsBatch.clear(); // On vide la liste pour le lot suivant
                if (i % (BATCH_SIZE * 10) == 0) { // Log moins fréquent
                    logger.info("{} items insérés...", i);
                }
            }
        }

        logger.info("Génération de données terminée avec succès !");
    }
}