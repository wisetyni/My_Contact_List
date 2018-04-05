package mycontactlist.example.com.my_contact_list;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity
public class Contact {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String contactName;
    private String phoneNumber;
    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;
    private String email;
    private String cellNumber;
    private String birthday;

    public Contact() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contact)) return false;

        Contact contact = (Contact) o;

        if (getId() != contact.getId()) return false;
        if (!getContactName().equals(contact.getContactName())) return false;
        if (getPhoneNumber() != null ? !getPhoneNumber().equals(contact.getPhoneNumber()) : contact.getPhoneNumber() != null)
            return false;
        if (getStreetAddress() != null ? !getStreetAddress().equals(contact.getStreetAddress()) : contact.getStreetAddress() != null)
            return false;
        if (getCity() != null ? !getCity().equals(contact.getCity()) : contact.getCity() != null)
            return false;
        if (getState() != null ? !getState().equals(contact.getState()) : contact.getState() != null)
            return false;
        if (getZipCode() != null ? !getZipCode().equals(contact.getZipCode()) : contact.getZipCode() != null)
            return false;
        if (getEmail() != null ? !getEmail().equals(contact.getEmail()) : contact.getEmail() != null)
            return false;
        if (getCellNumber() != null ? !getCellNumber().equals(contact.getCellNumber()) : contact.getCellNumber() != null)
            return false;
        return getBirthday() != null ? getBirthday().equals(contact.getBirthday()) : contact.getBirthday() == null;
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getContactName().hashCode();
        result = 31 * result + (getPhoneNumber() != null ? getPhoneNumber().hashCode() : 0);
        result = 31 * result + (getStreetAddress() != null ? getStreetAddress().hashCode() : 0);
        result = 31 * result + (getCity() != null ? getCity().hashCode() : 0);
        result = 31 * result + (getState() != null ? getState().hashCode() : 0);
        result = 31 * result + (getZipCode() != null ? getZipCode().hashCode() : 0);
        result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
        result = 31 * result + (getCellNumber() != null ? getCellNumber().hashCode() : 0);
        result = 31 * result + (getBirthday() != null ? getBirthday().hashCode() : 0);
        return result;
    }
}
