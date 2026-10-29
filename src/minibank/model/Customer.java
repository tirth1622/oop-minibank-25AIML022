package minibank.model;

import minibank.model.annotation.Id;
import minibank.model.annotation.MaxLength;

import java.io.Serializable;
import java.util.Objects;

/**
 * Practicals 2, 3, 7, 11, 13: Customer entity with auto-generated ID,
 * static nested Address class, Cloneable, Serializable, and reflection @Id support.
 */
public class Customer implements Cloneable, Serializable {
    private static final long serialVersionUID = 1L;

    private static long customerCounter = 100;

    @Id
    private final String customerId;

    @MaxLength(50)
    private String name;

    @MaxLength(80)
    private String email;

    @MaxLength(15)
    private String mobile;

    private Address address;

    /**
     * Practical 3: Public static nested class representing physical address.
     */
    public static class Address implements Serializable, Cloneable {
        private static final long serialVersionUID = 1L;

        private final String line;
        private final String city;
        private final String pincode;

        public Address(String line, String city, String pincode) {
            this.line = line;
            this.city = city;
            this.pincode = pincode;
        }

        public String getLine() {
            return line;
        }

        public String getCity() {
            return city;
        }

        public String getPincode() {
            return pincode;
        }

        @Override
        public String toString() {
            return line + ", " + city + " - " + pincode;
        }

        @Override
        public Address clone() {
            try {
                return (Address) super.clone();
            } catch (CloneNotSupportedException e) {
                return new Address(line, city, pincode);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Address a)) return false;
            return Objects.equals(line, a.line) &&
                   Objects.equals(city, a.city) &&
                   Objects.equals(pincode, a.pincode);
        }

        @Override
        public int hashCode() {
            return Objects.hash(line, city, pincode);
        }
    }

    private static synchronized String generateCustomerId() {
        customerCounter++;
        return "CUST" + customerCounter;
    }

    public static synchronized void setStartingCounter(long counter) {
        if (counter > customerCounter) {
            customerCounter = counter;
        }
    }

    public Customer(String name, String email, String mobile) {
        this(generateCustomerId(), name, email, mobile, null);
    }

    public Customer(String name, String email, String mobile, Address address) {
        this(generateCustomerId(), name, email, mobile, address);
    }

    public Customer(String customerId, String name, String email, String mobile, Address address) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * Practical 3: Cloneable implementation returning a deep copy of Customer.
     */
    @Override
    public Customer clone() {
        try {
            Customer copy = (Customer) super.clone();
            if (this.address != null) {
                copy.address = this.address.clone();
            }
            return copy;
        } catch (CloneNotSupportedException e) {
            return new Customer(this.customerId, this.name, this.email, this.mobile,
                    this.address != null ? this.address.clone() : null);
        }
    }

    @Override
    public String toString() {
        return customerId + " | " + name + " | " + email + " | " + mobile +
               (address != null ? " | " + address : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer c)) return false;
        return Objects.equals(customerId, c.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }
}
