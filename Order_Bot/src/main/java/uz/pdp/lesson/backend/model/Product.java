package uz.pdp.lesson.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    private Long userid;
    private String name;
    private double prise;
    private String photoId;
    private String category;
    private boolean isCompleted;
}
