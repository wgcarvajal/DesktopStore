package entities;
// Generated 9/02/2021 12:32:00 PM by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Purchase generated by hbm2java
 */
@Entity
@Table(name="purchase"
    ,catalog="storebd"
)
public class Purchase  implements java.io.Serializable {


     private Long purId;
     private Client client;
     private User user;
     private Date purDate;
     private Integer purFinalAmount;
     private Integer purReceivedAmount;
     private Integer purDiscount;
     private Integer purPayment;
     private int purState;
     private Set<Purchasetotal> purchasetotals = new HashSet<Purchasetotal>(0);
     private Set<Purchaseitem> purchaseitems = new HashSet<Purchaseitem>(0);

    public Purchase() {
    }

	
    public Purchase(User user, Date purDate, int purState) {
        this.user = user;
        this.purDate = purDate;
        this.purState = purState;
    }
    public Purchase(Client client, User user, Date purDate, Integer purFinalAmount, Integer purReceivedAmount, Integer purDiscount, Integer purPayment, int purState, Set<Purchasetotal> purchasetotals, Set<Purchaseitem> purchaseitems) {
       this.client = client;
       this.user = user;
       this.purDate = purDate;
       this.purFinalAmount = purFinalAmount;
       this.purReceivedAmount = purReceivedAmount;
       this.purDiscount = purDiscount;
       this.purPayment = purPayment;
       this.purState = purState;
       this.purchasetotals = purchasetotals;
       this.purchaseitems = purchaseitems;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="purId", unique=true, nullable=false)
    public Long getPurId() {
        return this.purId;
    }
    
    public void setPurId(Long purId) {
        this.purId = purId;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="cliId")
    public Client getClient() {
        return this.client;
    }
    
    public void setClient(Client client) {
        this.client = client;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="usId", nullable=false)
    public User getUser() {
        return this.user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="purDate", nullable=false, length=19)
    public Date getPurDate() {
        return this.purDate;
    }
    
    public void setPurDate(Date purDate) {
        this.purDate = purDate;
    }

    
    @Column(name="purFinalAmount")
    public Integer getPurFinalAmount() {
        return this.purFinalAmount;
    }
    
    public void setPurFinalAmount(Integer purFinalAmount) {
        this.purFinalAmount = purFinalAmount;
    }

    
    @Column(name="purReceivedAmount")
    public Integer getPurReceivedAmount() {
        return this.purReceivedAmount;
    }
    
    public void setPurReceivedAmount(Integer purReceivedAmount) {
        this.purReceivedAmount = purReceivedAmount;
    }

    
    @Column(name="purDiscount")
    public Integer getPurDiscount() {
        return this.purDiscount;
    }
    
    public void setPurDiscount(Integer purDiscount) {
        this.purDiscount = purDiscount;
    }

    
    @Column(name="purPayment")
    public Integer getPurPayment() {
        return this.purPayment;
    }
    
    public void setPurPayment(Integer purPayment) {
        this.purPayment = purPayment;
    }

    
    @Column(name="purState", nullable=false)
    public int getPurState() {
        return this.purState;
    }
    
    public void setPurState(int purState) {
        this.purState = purState;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="purchase")
    public Set<Purchasetotal> getPurchasetotals() {
        return this.purchasetotals;
    }
    
    public void setPurchasetotals(Set<Purchasetotal> purchasetotals) {
        this.purchasetotals = purchasetotals;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="purchase")
    public Set<Purchaseitem> getPurchaseitems() {
        return this.purchaseitems;
    }
    
    public void setPurchaseitems(Set<Purchaseitem> purchaseitems) {
        this.purchaseitems = purchaseitems;
    }




}


