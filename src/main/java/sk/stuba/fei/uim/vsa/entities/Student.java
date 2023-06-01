package sk.stuba.fei.uim.vsa.entities;

import java.io.Serializable;
import java.security.Principal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import sk.stuba.fei.uim.vsa.pr2.auth.Permission;

@Entity
public class Student implements Serializable, Principal {

    private static final long serialVersionUID = 1L;
    @Id
    private Long aisId;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String name;
    private String programme;
    private Integer year;
    private Integer term;
    @Column(nullable = false)
    private String password;
    @OneToOne(mappedBy = "author")
    @JoinColumn(nullable = true)
    private ZaverecnaPraca thesis;
    
    @Transient
    private Permission permission;
    
    public Student(){
    }
    
    public void addPermission(Permission permission){
        this.permission = permission;
    }

    public Permission getPermission() {
        return permission;
    }
    
    public Long getAisId() {
        return aisId;
    }

    public void setAisId(Long aisId) {
        this.aisId = aisId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProgramme() {
        return programme;
    }

    public void setProgramme(String programme) {
        this.programme = programme;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public ZaverecnaPraca getThesis() {
        return thesis;
    }

    public void setThesis(ZaverecnaPraca thesis) {
        this.thesis = thesis;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aisId != null ? aisId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the aisId fields are not set
        if (!(object instanceof Student)) {
            return false;
        }
        Student other = (Student) object;
        if ((this.aisId == null && other.aisId != null) || (this.aisId != null && !this.aisId.equals(other.aisId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sk.stuba.fei.uim.vsa.pr1.Student[ id=" + aisId + " ]";
    }
    
}
