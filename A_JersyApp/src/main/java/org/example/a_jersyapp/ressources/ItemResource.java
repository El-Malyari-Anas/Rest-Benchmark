package org.example.a_jersyapp.ressources;


import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.a_jersyapp.entities.Category;
import org.example.a_jersyapp.entities.Item;
import org.example.a_jersyapp.repositories.CategoryRepository;
import org.example.a_jersyapp.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemResource {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    @GET
    public Response getItems(
            @QueryParam("categoryId") Long categoryId,
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("20") @QueryParam("size") int size,
            // Capture le flag "mode"
            @DefaultValue("baseline") @QueryParam("mode") String mode) {

        PageRequest pageable = PageRequest.of(page, size);
        Page<Item> itemPage;
        
        boolean useJoin = "join-fetch".equalsIgnoreCase(mode) || "join-filter".equalsIgnoreCase(mode);

        if (categoryId != null) {
            if (useJoin) {
                itemPage = itemRepository.findByCategoryIdWithCategoryJoinFetch(categoryId, pageable);
            } else {
                itemPage = itemRepository.findByCategory_Id(categoryId, pageable);
            }
        } else {
            if (useJoin) {
                itemPage = itemRepository.findAllWithCategoryJoinFetch(pageable);
            } else {
                itemPage = itemRepository.findAll(pageable);
            }
        }
        return Response.ok(itemPage.getContent()).build();
    }

    @GET
    @Path("/{id}")
    public Response getItemById(@PathParam("id") Long id) {
        return itemRepository.findById(id)
                .map(item -> Response.ok(item).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

      @POST
    public Response createItem(@Valid Item item) {
        if (item.getId() != null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ID must be null for a new item.")
                    .build();
        }
        
        // Charger la catégorie complète depuis la DB
        if (item.getCategory() != null && item.getCategory().getId() != null) {
            Category category = categoryRepository.findById(item.getCategory().getId())
                    .orElseThrow(() -> new WebApplicationException(
                        "Category with ID " + item.getCategory().getId() + " not found", 
                        Response.Status.NOT_FOUND));
            item.setCategory(category);
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Category is required")
                    .build();
        }
        
        Item savedItem = itemRepository.save(item);
        return Response.status(Response.Status.CREATED).entity(savedItem).build();
    }


    @PUT
    @Path("/{id}")
    public Response updateItem(@PathParam("id") Long id, @Valid Item itemData) {
        return itemRepository.findById(id)
                .map(existingItem -> {
                    itemData.setId(id);
                    if (itemData.getCategory() != null && itemData.getCategory().getId() != null) {
                        categoryRepository.findById(itemData.getCategory().getId())
                                .ifPresent(itemData::setCategory);
                    } else {
                        itemData.setCategory(existingItem.getCategory());
                    }

                    Item updatedItem = itemRepository.save(itemData);
                    return Response.ok(updatedItem).build();
                })
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }


    @DELETE
    @Path("/{id}")
    public Response deleteItem(@PathParam("id") Long id) {
        if (!itemRepository.existsById(id)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        itemRepository.deleteById(id);
        return Response.noContent().build();
    }
}