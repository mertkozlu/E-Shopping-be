package EShopping.EShopping.wepApi.controllers;

import EShopping.EShopping.business.CategoryService;
import EShopping.EShopping.dto.requests.CreateCategoryRequest;
import EShopping.EShopping.entities.Category;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("categories")
public class CategoriesController {
    private final CategoryService categoryService;

    public CategoriesController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/getAll")
    public List<Category> getAll() {
        return categoryService.getAllCategories();
    }

    @PostMapping("/add")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Category createOneCategory(@RequestBody @Validated CreateCategoryRequest newCategory) {
        return categoryService.createOneCategory(newCategory);
    }
}
