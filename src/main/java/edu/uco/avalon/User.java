package edu.uco.avalon;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class User {
    /**
     * Contains validation patterns, values, and messages
     */
    public interface validation {
        interface email {
            /**
             * Crazy long regex string which supposedly matches 99.99% of valid emails
             * http://emailregex.com/
             **/
            String pattern = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
            String message = "Not a valid email address";
        }

        interface name {
            /**
             * Do not allow certain characters in name fields
             */
            String pattern = "^[^±!@£$%^&*_+§¡€#¢§¶•ªº«\\\\/<>?:;|=.,]*$";
            String message = "Invalid character";
        }

        interface required {
            int value = 1;
            String message = "This is a required field";
        }

        interface max {
            int value = 50;
            String message = "This field allows up to {max} characters";
        }
    }

    protected int userID;

    @Size.List({
        @Size(min = validation.required.value, message = validation.required.message),
        @Size(max = validation.max.value, message = validation.max.message)
    })
    @Pattern(regexp = validation.name.pattern, message = validation.name.message)
    protected String firstName;

    @Size.List({
        @Size(min = validation.required.value, message = validation.required.message),
        @Size(max = validation.max.value, message = validation.max.message)
    })
    @Pattern(regexp = validation.name.pattern, message = validation.name.message)
    protected String lastName;

    @Size.List({
        @Size(min = validation.required.value, message = validation.required.message),
        @Size(max = validation.max.value, message = validation.max.message)
    })
    @Pattern(regexp = validation.email.pattern, message = validation.email.message)
    protected String email;

    @Size.List({
        @Size(min = validation.required.value, message = validation.required.message),
        @Size(max = validation.max.value, message = validation.max.message)
    })
    protected String password;

    protected boolean locked = false;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getFullname() {
        return this.firstName + ' ' + this.lastName;
    }
}
