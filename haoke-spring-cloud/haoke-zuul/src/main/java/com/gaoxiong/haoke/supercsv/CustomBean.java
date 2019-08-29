package com.gaoxiong.haoke.supercsv;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomBean{
        private Integer customerNo;
        private String firstName;
        private String lastName;
        private Date birthDate;
        private String mailingAddress;
        private Boolean married;
        private Integer numberOfKids;
        private String favouriteQuote;
        private String email;
        private Long loyaltyPoints;

      /*  public CustomBean () {
        }

        public CustomBean ( Integer customerNo, String firstName, String lastName, Date birthDate, String mailingAddress, Boolean married, Integer numberOfKids, String favouriteQuote, String email, Long loyaltyPoints ) {
            this.customerNo = customerNo;
            this.firstName = firstName;
            this.lastName = lastName;
            this.birthDate = birthDate;
            this.mailingAddress = mailingAddress;
            this.married = married;
            this.numberOfKids = numberOfKids;
            this.favouriteQuote = favouriteQuote;
            this.email = email;
            this.loyaltyPoints = loyaltyPoints;
        }*/
    }