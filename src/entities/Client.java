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
import javax.persistence.UniqueConstraint;

/**
 * Client generated by hbm2java
 */
@Entity
@Table(name="client"
    ,catalog="storebd"
    , uniqueConstraints = @UniqueConstraint(columnNames="cliIdentity") 
)
public class Client  implements java.io.Serializable {


     private Long cliId;
     private String cliIdentity;
     private String cliName;
     private String cliLastName;
     private String cliPhones;
     private String cliAddress;
     private boolean cliCredit;
     private Set<Lend> lends = new HashSet<Lend>(0);
     private Set<Purchase> purchases = new HashSet<Purchase>(0);
     private Set<Pay> pays = new HashSet<Pay>(0);

    public Client() {
    }

	
    public Client(String cliIdentity, String cliName, String cliLastName, String cliPhones, String cliAddress, boolean cliCredit) {
        this.cliIdentity = cliIdentity;
        this.cliName = cliName;
        this.cliLastName = cliLastName;
        this.cliPhones = cliPhones;
        this.cliAddress = cliAddress;
        this.cliCredit = cliCredit;
    }
    public Client(String cliIdentity, String cliName, String cliLastName, String cliPhones, String cliAddress, boolean cliCredit, Set<Lend> lends, Set<Purchase> purchases, Set<Pay> pays) {
       this.cliIdentity = cliIdentity;
       this.cliName = cliName;
       this.cliLastName = cliLastName;
       this.cliPhones = cliPhones;
       this.cliAddress = cliAddress;
       this.cliCredit = cliCredit;
       this.lends = lends;
       this.purchases = purchases;
       this.pays = pays;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="cliId", unique=true, nullable=false)
    public Long getCliId() {
        return this.cliId;
    }
    
    public void setCliId(Long cliId) {
        this.cliId = cliId;
    }

    
    @Column(name="cliIdentity", unique=true, nullable=false, length=50)
    public String getCliIdentity() {
        return this.cliIdentity;
    }
    
    public void setCliIdentity(String cliIdentity) {
        this.cliIdentity = cliIdentity;
    }

    
    @Column(name="cliName", nullable=false, length=100)
    public String getCliName() {
        return this.cliName;
    }
    
    public void setCliName(String cliName) {
        this.cliName = cliName;
    }

    
    @Column(name="cliLastName", nullable=false, length=100)
    public String getCliLastName() {
        return this.cliLastName;
    }
    
    public void setCliLastName(String cliLastName) {
        this.cliLastName = cliLastName;
    }

    
    @Column(name="cliPhones", nullable=false, length=200)
    public String getCliPhones() {
        return this.cliPhones;
    }
    
    public void setCliPhones(String cliPhones) {
        this.cliPhones = cliPhones;
    }

    
    @Column(name="cliAddress", nullable=false, length=200)
    public String getCliAddress() {
        return this.cliAddress;
    }
    
    public void setCliAddress(String cliAddress) {
        this.cliAddress = cliAddress;
    }

    
    @Column(name="cliCredit", nullable=false)
    public boolean isCliCredit() {
        return this.cliCredit;
    }
    
    public void setCliCredit(boolean cliCredit) {
        this.cliCredit = cliCredit;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="client")
    public Set<Lend> getLends() {
        return this.lends;
    }
    
    public void setLends(Set<Lend> lends) {
        this.lends = lends;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="client")
    public Set<Purchase> getPurchases() {
        return this.purchases;
    }
    
    public void setPurchases(Set<Purchase> purchases) {
        this.purchases = purchases;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="client")
    public Set<Pay> getPays() {
        return this.pays;
    }
    
    public void setPays(Set<Pay> pays) {
        this.pays = pays;
    }




}


