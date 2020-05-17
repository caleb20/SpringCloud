package academy.digitallab.store.product.controller;

import academy.digitallab.store.product.entity.Category;
import academy.digitallab.store.product.entity.Product;
import academy.digitallab.store.product.service.ProductService;
import academy.digitallab.store.product.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> listProduct(@RequestParam(name = "categoryId", required = false) Long categoryId) {

        List<Product> products = new ArrayList<>();

        if (null == categoryId) {
            products = productService.listAllProduct();
            if (products.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
        } else {
            products = productService.findByCategory(Category.builder().id(categoryId).build());
            if (products.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
        }

        return ResponseEntity.ok(products);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") Long id) {

        Product product = productService.getProduct(id);

        if (null == product) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(product);

    }

    @PostMapping()
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product, BindingResult result) {

        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Utils.formatMessage(result, "01"));
        }

        Product productCreate = productService.createProduct(product);

        return ResponseEntity.status(HttpStatus.CREATED).body(productCreate);

    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Long id, @RequestBody Product product) {

        product.setId(id);
        Product productDB = productService.updateProduct(product);

        if (null == productDB) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(productDB);

    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("id") Long id) {

        Product productDelete = productService.deleteProduct(id);

        if (null == productDelete) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(productDelete);
    }

    @GetMapping(value = "/{id}/stock")
    public ResponseEntity<Product> updateStockProduct(@PathVariable("id") Long id, @RequestParam(name = "quantity", required = true) Double quantity) {
        Product product = productService.updateStock(id, quantity);

        if (null == product) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(product);
    }


}
