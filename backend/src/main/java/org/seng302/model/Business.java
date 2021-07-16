/**
 * Summary. This file contains the definition for the Business.
 *
 * Description. This file contains the defintion for the Business.
 *
 * @link   team-400/src/main/java/org/seng302/business/Business
 * @file   This file contains the definition for Business.
 * @author team-400.
 * @since  5.5.2021
 */
package org.seng302.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.NoArgsConstructor;
import org.seng302.exceptions.IllegalBusinessArgumentException;
import org.seng302.model.enums.BusinessType;
import org.seng302.Validation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Business Class
 *
 * Business Class for defining business by id, admins, primary admin, business name,
 * business description, address, business type and created date.
 *
 * @link   team-400/src/main/java/org/seng302/business/Business.java
 * @file   Business.java.
 * @author team-400.
 * @since  5.5.2021
 */
@NoArgsConstructor // generate a no-args constructor needed by JPA (lombok pre-processor)
@Entity // declare this class as a JPA entity (that can be mapped to a SQL table)
public class Business {
    @Id // this field (attribute) is the table primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincrement the ID
    @Column(name = "id", nullable = false)
    private int id;

    @JsonBackReference
    @ManyToMany(mappedBy = "businessesAdministeredObjects", fetch = FetchType.EAGER)
    private List<User> administrators = new ArrayList<>();

    @Column(name = "primaryAdministratorId")
    private Integer primaryAdministratorId;

//    include name*, description, address*, type* and registration date*
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = 600)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(name = "businessType", nullable = false)
    private BusinessType businessType;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    // Values need for validation.
    private static final Integer NAME_MIN_LENGTH = 1;
    private static final Integer NAME_MAX_LENGTH = 100;

    private static final Integer DESCRIPTION_MIN_LENGTH = 0;
    private static final Integer DESCRIPTION_MAX_LENGTH = 600;


    /**
     * Create new BusinessAccount object.
     * @param primaryAdministratorId the id of the primary administrator of the business
     * @param name the name of the business (mandatory)
     * @param description the description of the business (optional)
     * @param address the address of the business (mandatory)
     * @param businessType the type of the business (mandatory)
     * @param created the date the business was created
     * @param administrator the user who created this business
     * @throws IllegalBusinessArgumentException thrown when parameter is not valid.
     */
    public Business(Integer primaryAdministratorId,
                    String name,
                    String description,
                    Address address,
                    BusinessType businessType,
                    LocalDateTime created,
                    User administrator
    ) throws IllegalBusinessArgumentException {
        if (!isValidName(name)){
            throw new IllegalBusinessArgumentException("Invalid business name");
        }
        if (!isValidDescription(description)){
            throw new IllegalBusinessArgumentException("Invalid business description");
        }

        this.primaryAdministratorId = primaryAdministratorId;
        this.name = name;
        this.description = (description.equals("")) ? null : description;
        this.address = address;
        this.businessType = businessType;
        this.created = created;
        administrators.add(administrator);

    }

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
    public Address getAddress() {
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


    /**
     * set id
     * @param id id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * set name
     * @param name name
     */
    public void setName(String name) throws IllegalBusinessArgumentException {
        if (!Validation.isName(name)){
            throw new IllegalBusinessArgumentException("Invalid business name");
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
     * Adds a new user as an administrator for this business.
     * Also adds this business to the list of businesses administered by the user.
     * @param user An user which is an administrator for this business.
     */
    public void addAdministrators(User user) {
        this.administrators.add(user);
        user.getBusinessesAdministeredObjects().add(this);
    }

    /**
     * Removes a user from the list of administrators for this business.
     * Also removes this business from the list of businesses administered by the user.
     * @param user An user who was an administrator for this business.
     */
    public void removeAdministrators(User user) {
        int userId = user.getId();
        for (int i = 0; i < administrators.size(); i++){
            if (administrators.get(i).getId() == userId){
                this.administrators.remove(i);
            }
        }
        user.removeABusinessesAdministeredObjects(this);
    }

    /**
     * Set the id of business.
     * @param id the id to be set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Set the administrators of  business.
     * @param administrators - A list of users who are the administrators of business.
     */
    public void setAdministrators(List<User> administrators) {
        this.administrators = administrators;
    }

    /**
     * check if a user is an administrator of this business
     * @param user an user
     * @return return true when user is a administrator of this business
     */
    public boolean isAnAdministratorOfThisBusiness(User user) {
        for (User administrator : administrators){
            if (administrator.equals(user)){
                return true;
            }
        }
        return false;
    }

    /**
     * Override the string method for this business. This method is useful for printing the details
     * of a business for debugging purposes.
     * @return String which represents the business.
     */
    @Override
    public String toString() {
        return "{\"id\":" + id +
                ",\"administrators\":[null]" +
                ",\"primaryAdministratorId\":\"" + primaryAdministratorId + "\"" +
                ",\"name\":\"" + name + "\"" +
                ",\"description\":\"" + description + "\"" +
                ",\"address\":\"" + address + "\"" +
                ",\"businessType\":\"" + businessType + "\"" +
                ",\"created\":\"" + created + "\"" +
                "}";
    }

    /**
     * Checks to see whether business name is valid based on its constraints.
     * This method can be updated in the future if there is additional constraints.
     * @param businessName The business name to be checked.
     * @return true when the business name is valid
     */
    private boolean isValidName(String businessName) {
        return (businessName.length() >= NAME_MIN_LENGTH) &&
                (businessName.length() <= NAME_MAX_LENGTH) &&
                (businessName.matches("^[a-zA-Z0-9À-ÖØ-öø-įĴ-őŔ-žǍ-ǰǴ-ǵǸ-țȞ-ȟȤ-ȳɃɆ-ɏḀ-ẞƀ-ƓƗ-ƚƝ-ơƤ-ƥƫ-ưƲ-ƶẠ-ỿ '#,.&()-]+$"));
    }

    /**
     * Checks to see whether description is valid based on its constraints.
     * This method can be updated in the future if there is additional constraints.
     * @param description The description to be checked.
     * @return true when the description is valid.
     */
    private boolean isValidDescription(String description) {
        return (description.length() >= DESCRIPTION_MIN_LENGTH) && (description.length() <= DESCRIPTION_MAX_LENGTH);
    }
}
