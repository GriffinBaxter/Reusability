package org.seng302.business;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.seng302.address.Address;
import org.seng302.validation.Validation;
import org.seng302.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor // generate a no-args constructor needed by JPA (lombok pre-processor)
@Entity // declare this class as a JPA entity (that can be mapped to a SQL table)
public class Business {
    @Id // this field (attribute) is the table primary key
    @GeneratedValue//(strategy = GenerationType.IDENTITY) // autoincrement the ID
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "primaryAdministratorId")
    private Integer primaryAdministratorId;

//    include name*, description, address*, type* and registration date*
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(name = "businessType", nullable = false)
    private BusinessType businessType;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @JsonBackReference
    @ManyToMany(mappedBy = "businessesAdministeredObjects", fetch = FetchType.EAGER)
    private List<User> administrators = new ArrayList<User>();


    /**
     * create new BusinessAccount object with description
     * @param name
     * @param description
     * @param address
     * @param businessType
     * @throws Exception
     */
    public Business(String name,
                    String description,
                    Address address,
                    BusinessType businessType,
                    LocalDateTime created,
                    User administrator,
                    Integer primaryAdministratorId
    ) throws Exception{
        if (!Validation.isName(name)){
            throw new Exception("Invalid business name");
        }
        if (address == null){
            throw new Exception("Invalid address");
        }
        this.name = name;
        this.address = address;
        this.businessType = businessType;
        this.description = (description.equals("")) ? null : description;
        this.created = created;
        administrators.add(administrator);
        this.primaryAdministratorId = primaryAdministratorId;
    }

    //getter

    /**
     * get id
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * get administrators
     * @return administrators
     */
    public List<User> getAdministrators() {
        return administrators;
    }

    /**
     * get name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * get address
     * @return address
     */
    public Address getAddress() throws Exception {
        return address;
    }

    /**
     * get type
     * @return type
     */
    public BusinessType getBusinessType() {
        return businessType;
    }

    /**
     * get registration date
     * @return registrationDate
     */
    public LocalDateTime getCreated() {
        return created;
    }

    /**
     * get description
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * get primary administrator Id
     * @return primaryAdministratorId
     */
    public Integer getPrimaryAdministratorId() {
        return primaryAdministratorId;
    }

    //setter
    /**
     * set name
     * @param name name
     */
    public void setName(String name) throws Exception {
        if (!Validation.isName(name)){
            throw new Exception("Invalid business name");
        }
        this.name = name;
    }

    /**
     * set address
     * @param address address
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * set type
     * @param businessType business type
     */
    public void setBusinessType(BusinessType businessType) {
        this.businessType = businessType;
    }

    /**
     * set registration date
     * @param created created date
     */
    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    /**
     * set description
     * @param description despite shop info
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * set primaryAdministratorId
     * @param primaryAdministratorId primary administrator Id
     */
    public void setPrimaryAdministratorId(Integer primaryAdministratorId) {
        this.primaryAdministratorId = primaryAdministratorId;
    }

    /**
     * Returns a list of User objects who are administrators of this business
     * @return administrators A list of User objects.
     */
    public List<User> Administrators() {
        return administrators;
    }

    /**
     * Adds a new user as an administrator for this business.
     * Also adds this business to the list of businesses administered by the user.
     * @param user A user which is an administrator for this business.
     */
    public void addAdministrators(User user) {
        this.administrators.add(user);
        user.getBusinessesAdministeredObjects().add(this);
    }

    /**
     * Removes a user from the list of administrators for this business.
     * Also removes this business from the list of businesses administered by the user.
     * @param user A user who was an administrator for this business.
     */
    public void removeAdministrators(User user) {
        this.administrators.remove(user.getId());
        user.getBusinessesAdministeredObjects().remove(this);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAdministrators(List<User> administrators) {
        this.administrators = administrators;
    }
}
