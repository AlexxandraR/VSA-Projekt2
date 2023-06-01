package sk.stuba.fei.uim.vsa.entities;

import java.io.Serializable;
import java.security.Principal;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import sk.stuba.fei.uim.vsa.pr2.auth.Permission;

@Entity
public class Pedagog implements Serializable, Principal {

    private static final long serialVersionUID = 1L;
    @Id
    private Long aisId;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String name;
    private String institute;
    private String department;
    @Column(nullable = false)
    private String password;
    @OneToMany(mappedBy = "supervisor", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(nullable = true)
    private List<ZaverecnaPraca> theses;
    
    @Transient
    private Permission permission;
    
    public Pedagog(){
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

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<ZaverecnaPraca> getTheses() {
        return theses;
    }

    public void setTheses(List<ZaverecnaPraca> theses) {
        this.theses = theses;
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
        if (!(object instanceof Pedagog)) {
            return false;
        }
        Pedagog other = (Pedagog) object;
        if ((this.aisId == null && other.aisId != null) || (this.aisId != null && !this.aisId.equals(other.aisId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sk.stuba.fei.uim.vsa.pr1.Pedagog[ id=" + aisId + " ]";
    }
    
}
