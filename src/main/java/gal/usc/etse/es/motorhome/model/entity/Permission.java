package gal.usc.etse.es.motorhome.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@SuppressWarnings("unused")
@Entity
@Table(name = "permissions")
@Getter
@Setter
@Accessors(chain = true)
public class Permission {
    @Id
    private String id;
    private String resource;
    private String action;

    public Permission() {
    }
}
