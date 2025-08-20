package com.modernized.entities;

import jakarta.persistence.*;
import java.util.Objects;

/**
 * System users with different permission levels who manage customer accounts and operations
 * Source: CSUSR01Y.cpy, lines 17-23
 */
@Entity
@Table(name = "app_user")
public class User {

    @Id
    @Column(name = "sec_usr_id", length = 8)
    private String secUsrId;

    @Column(name = "sec_usr_fname", length = 20)
    private String secUsrFname;

    @Column(name = "sec_usr_lname", length = 20)
    private String secUsrLname;

    @Column(name = "sec_usr_pwd", length = 8)
    private String secUsrPwd;

    @Column(name = "sec_usr_type", length = 1)
    private String secUsrType;

    public User() {}

    public User(String secUsrId, String secUsrFname, String secUsrLname,
               String secUsrPwd, String secUsrType) {
        this.secUsrId = secUsrId;
        this.secUsrFname = secUsrFname;
        this.secUsrLname = secUsrLname;
        this.secUsrPwd = secUsrPwd;
        this.secUsrType = secUsrType;
    }

    public String getSecUsrId() { return secUsrId; }
    public void setSecUsrId(String secUsrId) { this.secUsrId = secUsrId; }

    public String getSecUsrFname() { return secUsrFname; }
    public void setSecUsrFname(String secUsrFname) { this.secUsrFname = secUsrFname; }

    public String getSecUsrLname() { return secUsrLname; }
    public void setSecUsrLname(String secUsrLname) { this.secUsrLname = secUsrLname; }

    public String getSecUsrPwd() { return secUsrPwd; }
    public void setSecUsrPwd(String secUsrPwd) { this.secUsrPwd = secUsrPwd; }

    public String getSecUsrType() { return secUsrType; }
    public void setSecUsrType(String secUsrType) { this.secUsrType = secUsrType; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(secUsrId, user.secUsrId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(secUsrId);
    }

    @Override
    public String toString() {
        return "User{" +
                "secUsrId='" + secUsrId + '\'' +
                ", secUsrFname='" + secUsrFname + '\'' +
                ", secUsrLname='" + secUsrLname + '\'' +
                ", secUsrType='" + secUsrType + '\'' +
                '}';
    }
}
