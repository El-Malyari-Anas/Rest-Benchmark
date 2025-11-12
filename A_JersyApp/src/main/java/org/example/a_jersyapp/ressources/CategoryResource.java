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
@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryResource {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ItemRepository itemRepository;


    @GET
    public Response getCategories(
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("20") @QueryParam("size") int size) {

        Page<Category> categoryPage = categoryRepository.findAll(PageRequest.of(page, size));
        return Response.ok(categoryPage.getContent()).build();
    }


    @GET
    @Path("/{id}")
    public Response getCategoryById(@PathParam("id") Long id) {
        return categoryRepository.findById(id)
                .map(category -> Response.ok(category).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Response createCategory(@Valid Category category) {
        if (category.getId() != null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ID must be null for a new category.")
                    .build();
        }
        Category savedCategory = categoryRepository.save(category);
        return Response.status(Response.Status.CREATED).entity(savedCategory).build();
    }


    @PUT
    @Path("/{id}")
    public Response updateCategory(@PathParam("id") Long id, @Valid Category categoryData) {
        return categoryRepository.findById(id)
                .map(existingCategory -> {
                    categoryData.setId(id);
                    Category updatedCategory = categoryRepository.save(categoryData);
                    return Response.ok(updatedCategory).build();
                })
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }


    @DELETE
    @Path("/{id}")
    public Response deleteCategory(@PathParam("id") Long id) {
        if (!categoryRepository.existsById(id)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        categoryRepository.deleteById(id);
        return Response.noContent().build();
    }


    @GET
    @Path("/{id}/items")
    public Response getItemsByCategory(
            @PathParam("id") Long categoryId,
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("20") @QueryParam("size") int size) {

        if (!categoryRepository.existsById(categoryId)) {
            return Response.status(Response.Status.NOT_FOUND).entity("Category not found").build();
        }

        Page<Item> itemPage = itemRepository.findByCategory_Id(categoryId, PageRequest.of(page, size));
        return Response.ok(itemPage.getContent()).build();
    }
}