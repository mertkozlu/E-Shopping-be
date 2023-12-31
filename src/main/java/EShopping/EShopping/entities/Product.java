package EShopping.EShopping.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    private String productName;
    private String productDescription;
    private double productPrice;

    @Temporal(TemporalType.TIMESTAMP)
    Date createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonIgnore
    private Category category;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    List<Favorites> favorites;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    List<Cart> carts;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    List<Comment> comments;

}
