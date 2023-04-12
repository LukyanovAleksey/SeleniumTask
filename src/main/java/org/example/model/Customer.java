package org.example.model;

public class Customer {
    private int customerID;
    private String customerName;
    private String contactName;
    private String address;
    private String city;
    private String postalCode;
    private String country;

    public Customer(CustomerBuilder builder) {
        this.customerID = builder.customerID;
        this.customerName = builder.customerName;
        this.contactName = builder.contactName;
        this.address = builder.address;
        this.city = builder.city;
        this.postalCode = builder.postalCode;
        this.country = builder.country;
    }

    public static class CustomerBuilder {
        private int customerID;
        private String customerName;
        private String contactName;
        private String address;
        private String city;
        private String postalCode;
        private String country;

        public CustomerBuilder(String customerName) {
            this.customerName = customerName;
        }

        public CustomerBuilder setCustomerID(int customerID) {
            this.customerID = customerID;
            return this;
        }

        public CustomerBuilder setContactName(String contactName) {
            this.contactName = contactName;
            return this;
        }

        public CustomerBuilder setAddress(String address) {
            this.address = address;
            return this;
        }

        public CustomerBuilder setCity(String city) {
            this.city = city;
            return this;
        }

        public CustomerBuilder setPostalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public CustomerBuilder setCountry(String country) {
            this.country = country;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
