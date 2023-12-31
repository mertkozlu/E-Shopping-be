package EShopping.EShopping.service;

import EShopping.EShopping.dataAccess.CategoryRepository;
import EShopping.EShopping.dataAccess.ProductRepository;
import EShopping.EShopping.dto.requests.CreateCategoryRequest;
import EShopping.EShopping.dto.requests.UpdateCategoryRequest;
import EShopping.EShopping.dto.responses.GetAllCategoryResponse;
import EShopping.EShopping.dto.responses.GetCategoryByIdResponse;
import EShopping.EShopping.entities.Category;
import EShopping.EShopping.entities.Product;
import EShopping.EShopping.exceptions.BusinessException;
import EShopping.EShopping.mappers.ModelMapperService;
import EShopping.EShopping.result.*;
import EShopping.EShopping.rules.CategoryBusinessRules;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapperService modelMapperService;
    private final CategoryBusinessRules categoryBusinessRules;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository, ModelMapperService modelMapperService,
                           CategoryBusinessRules categoryBusinessRules, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.modelMapperService = modelMapperService;
        this.categoryBusinessRules = categoryBusinessRules;
        this.productRepository = productRepository;
    }

    public DataResult<List<GetAllCategoryResponse>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<GetAllCategoryResponse> getAllCategoryResponses = categories.stream()
                .map(category -> this.modelMapperService.forResponse()
                        .map(category, GetAllCategoryResponse.class)).collect(Collectors.toList());

        return new SuccessDataResult<List<GetAllCategoryResponse>>
                (getAllCategoryResponses, true, "Categories successfully listed.");
    }

    public Result addCategory(CreateCategoryRequest newCategory) {
        if (this.categoryBusinessRules.validateRequest(newCategory)) {
            Category category = this.modelMapperService.forRequest().map(newCategory, Category.class);
            this.categoryBusinessRules.existsByCategoryName(category.getCategoryName());
            categoryRepository.save(category);

            return new SuccessResult("Category successfully added.");
        } else
            return new ErrorResult("Category could not added.");
    }

    public ResponseEntity<GetCategoryByIdResponse> getCategoryById(Long categoryId) {

        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        GetCategoryByIdResponse response = new GetCategoryByIdResponse();
        response.setCategoryId(category.getCategoryId());
        response.setCategoryName(category.getCategoryName());

        ResponseEntity<GetCategoryByIdResponse> result = new ResponseEntity<>(response, HttpStatus.OK);
        return result;
    }

    public ResponseEntity<Category> updateCategory(Long categoryId, UpdateCategoryRequest updateCategoryRequest) {
        Category category = categoryRepository.findById(categoryId).orElse(null);

        if (Objects.nonNull(category)) {
            category.setCategoryName(updateCategoryRequest.getCategoryName());

            Category updateCategory = categoryRepository.save(category);
            return new ResponseEntity<>(updateCategory, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public void deleteCategoryById(Long categoryId) {
        Integer categoryCount = categoryRepository.countCategory();
        if (categoryCount < 1) {
            throw new BusinessException("Category cannot be deleted there most be add list one category.");
        }
        List<Product> products = productRepository.findByCategory_CategoryId(categoryId);
        if (!CollectionUtils.isEmpty(products)) {
            throw new BusinessException("Category cannot be deleted while the user has posts.");
        }
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new BusinessException("Category can not found."));
        this.categoryRepository.delete(category);
    }
}
