package uz.pdp.lesson.backend.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import uz.pdp.lesson.backend.model.MyUser;
import uz.pdp.lesson.backend.model.Product;
import uz.pdp.lesson.backend.repository.FileWriterAndLoader;
import uz.pdp.lesson.backend.statics.PathConstants;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProductServise implements PathConstants {
    FileWriterAndLoader<Product> writerAndLoader;
    private final Gson gson;

    public ProductServise() {
        this.writerAndLoader = new FileWriterAndLoader<>(PRODUCT_PATH);
        this.gson = new Gson();
    }

    public void save(Product food){
        List<Product> products = writerAndLoader.load(Product.class);
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            if (Objects.equals(product.getUserid(),food.getUserid())){
                products.set(i,food);
                writerAndLoader.write(products);
                return;
            }
        }
        products.add(food);
        writerAndLoader.write(products);
    }
    public Product get(Long id){
        List<Product> products = writerAndLoader.load(Product.class);
        for (int i = 0; i < products.size(); i++) {
            Product cur = products.get(i);
            if (Objects.equals(cur.getUserid(),id)){
                return cur;
            }
        }
        return null;
    }

    public Product getNonCompletedBasked(Long userId) {
        List<Product> products = writerAndLoader.load(Product.class);
        for (int i = 0; i < products.size(); i++) {
            Product cur = products.get(i);
            if(Objects.equals(cur.getUserid(),userId)&&!cur.isCompleted()) {
                return cur;
            }
        }
        return null;
    }
//    public Product getProductByCategory(String category) {
//        List<Product> products = writerAndLoader.load(Product.class);
//        for (Product product : products) {
//            if (product.getCategory().equalsIgnoreCase(category)) {
//                return product;
//            }
//        }
//        return null;
//    }

    public List<Product> getProductsByCategory(String category) {
        List<Product> productsByCategory = new ArrayList<>();
        List<Product> allProducts = writerAndLoader.load(Product.class);

        for (Product product : allProducts) {
            if (product.getCategory().equalsIgnoreCase(category)) {
                productsByCategory.add(product);
            }
        }

        return productsByCategory;
    }
    public List<Product> loadProductsFromFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            Type productListType = new TypeToken<List<Product>>() {}.getType();
            return gson.fromJson(content.toString(), productListType);
        }
    }
}
