package com.backoffice.operations.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;

import com.backoffice.operations.entity.Product;
import com.backoffice.operations.repository.ProductRepository;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("api/product")
public class ProductController {
    @Autowired
    private ProductRepository productRepository ;
    
    @GetMapping
    public java.util.List<Product> getAllProducts(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
    	return productRepository.findAll();
    }
    
    @GetMapping("/{productCode}")
    public Product getProductByID(@PathVariable String productCode, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return productRepository.findById(productCode)
        		.orElseThrow(() -> new RuntimeException("Product not found with ProductCode: " + productCode));
    }
    
    @PostMapping
    public Product createProduct (@RequestBody Product product)  {
        //TODO: process POST request        
        return productRepository.save(product);
    }
    
    @PutMapping("/{productCode}")
    public Product putMethodName(@PathVariable String productCode, @RequestBody Product updateProduct) {
    	  Product existingProduct = productRepository.findById(productCode)
                  .orElseThrow(() -> new RuntimeException("About Us not found with id: " + productCode));

          // Update the fields based on your requirements    	  
    	  existingProduct.setProductCode(updateProduct.getProductCode());
    	  existingProduct.setProductDescription(updateProduct.getProductDescription());        

          return productRepository.save(existingProduct);              
    }
    
    @DeleteMapping("/{productCode}")
    public void deleteProduct(@PathVariable String productCode) {
        productRepository.deleteById(productCode);
    }
}
