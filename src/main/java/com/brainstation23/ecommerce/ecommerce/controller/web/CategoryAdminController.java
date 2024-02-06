package com.brainstation23.ecommerce.ecommerce.controller.web;

import com.brainstation23.ecommerce.ecommerce.mapper.CategoryMapper;
import com.brainstation23.ecommerce.ecommerce.model.domain.Category;
import com.brainstation23.ecommerce.ecommerce.model.dto.category.CategoryCreateRequest;
import com.brainstation23.ecommerce.ecommerce.model.dto.category.CategoryUpdateRequest;
import com.brainstation23.ecommerce.ecommerce.service.interfaces.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/categories")
public class CategoryAdminController {
    private static final String ATTRIBUTE_CATEGORY = "category";
    private static final String REDIRECT_CATEGORY = "redirect:/admin/categories";
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;
    @GetMapping
    public String categories(Model model) {
        Page<Category> categories = categoryService.getAll(getDefaultCategoryPage());
        model.addAttribute("category_list", categories.map(categoryMapper::domainToResponse));
        model.addAttribute("pageTitle", "All Categories");
        model.addAttribute("content", "category/index");
        return "base";
    }
    @GetMapping("/addnew")
    public String addProduct(Model model) {
        CategoryCreateRequest categoryCreateRequest = new CategoryCreateRequest();
        model.addAttribute(ATTRIBUTE_CATEGORY, categoryCreateRequest);
        model.addAttribute("isNew", true);
        model.addAttribute("pageTitle", "Add New");
        model.addAttribute("content", "category/createorupdate");
        return "base";
    }
    @GetMapping("/update/{categoryId}")
    public String updateCategory(@PathVariable UUID categoryId, Model model) {
        Category category = categoryService.getOne(categoryId);
        model.addAttribute("isNew", false);
        model.addAttribute(ATTRIBUTE_CATEGORY, category);
        model.addAttribute("pageTitle", "Update Category");
        model.addAttribute("content", "category/createOrUpdate");
        return "base";
    }

    @PostMapping("/update")
    public String updateCategory(@ModelAttribute(ATTRIBUTE_CATEGORY) CategoryUpdateRequest categoryUpdateRequest) {
        categoryService.updateOne(categoryUpdateRequest.getId(),categoryUpdateRequest);
        return REDIRECT_CATEGORY;
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute(ATTRIBUTE_CATEGORY) CategoryCreateRequest categoryCreateRequest) {
        categoryService.createOne(categoryCreateRequest);
        return REDIRECT_CATEGORY;
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable(value = "id") UUID id) {
        categoryService.deleteOne(id);
        return REDIRECT_CATEGORY;
    }
    private Pageable getDefaultCategoryPage() {
        return PageRequest.of(0, 20, Sort.by(
                Sort.Order.asc("categoryName"),
                Sort.Order.desc("id")));
    }
}
