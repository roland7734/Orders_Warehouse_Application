package com.orders_management.logic;

import com.orders_management.data_access.CustomerDAO;
import com.orders_management.models.Product;
import com.orders_management.data_access.ProductDAO;
import java.util.NoSuchElementException;
import java.util.List;
import java.math.BigDecimal;

public class ProductBLL {

    public static List<String> getProductAttributes()
    {
        ProductDAO productDAO=new ProductDAO();
        return productDAO.getAttributeNamesWithoutId();
    }
    public static Product findProductById(int id) {
        ProductDAO productDAO = new ProductDAO();
        Product product = productDAO.findById(id);
        if (product == null) {
            throw new NoSuchElementException("The product with id = " + id + " was not found!");
        }
        return product;
    }

    public static List<Product> findAllProducts() {
        ProductDAO productDAO = new ProductDAO();
        List<Product> products = productDAO.findAll();
        return products;
    }

    public static Product insertProduct(Product product) {
        if (!Validator.isPositiveNumber(product.getPrice())) {
            throw new IllegalArgumentException("Price must be a positive number.");
        }

        if(!Validator.isNonNegative(product.getStock_quantity()))
        {
            throw new IllegalArgumentException("Invalid Stock Quantity.");
        }
        ProductDAO productDAO = new ProductDAO();
        Product newProduct = productDAO.insert(product);
        return newProduct;
    }

    public static Product updateProduct(Product product) {
        if (!Validator.isPositiveNumber(product.getPrice())) {
            throw new IllegalArgumentException("Price must be a positive number.");
        }

        if(!Validator.isNonNegative(product.getStock_quantity()))
        {
            throw new IllegalArgumentException("Invalid Stock Quantity.");
        }

        ProductDAO productDAO = new ProductDAO();
        Product updatedProduct = productDAO.update(product);
        return updatedProduct;
    }

    public static boolean deleteProduct(Product product) {
        ProductDAO productDAO = new ProductDAO();
        boolean flag = productDAO.delete(product);
        return flag;
    }
}
