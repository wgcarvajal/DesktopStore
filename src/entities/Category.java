package entities;
// Generated 9/02/2021 12:32:00 PM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Category generated by hbm2java
 */
@Entity
@Table(name="category"
    ,catalog="storebd"
)
public class Category  implements java.io.Serializable {


     private Long catId;
     private String catName;
     private String catDescription;
     private Set<Product> products = new HashSet<Product>(0);

    public Category() {
    }

	
    public Category(String catName) {
        this.catName = catName;
    }
    public Category(String catName, String catDescription, Set<Product> products) {
       this.catName = catName;
       this.catDescription = catDescription;
       this.products = products;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="catId", unique=true, nullable=false)
    public Long getCatId() {
        return this.catId;
    }
    
    public void setCatId(Long catId) {
        this.catId = catId;
    }

    
    @Column(name="catName", nullable=false, length=100)
    public String getCatName() {
        return this.catName;
    }
    
    public void setCatName(String catName) {
        this.catName = catName;
    }

    
    @Column(name="catDescription", length=65535)
    public String getCatDescription() {
        return this.catDescription;
    }
    
    public void setCatDescription(String catDescription) {
        this.catDescription = catDescription;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="category")
    public Set<Product> getProducts() {
        return this.products;
    }
    
    public void setProducts(Set<Product> products) {
        this.products = products;
    }




}


