package gal.usc.etse.es.motorhome.repository;

import gal.usc.etse.es.motorhome.model.entity.Role;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@NullMarked
public interface RoleRepository extends JpaRepository<Role, String> {
    Role findByRolename(String rolename);
}
