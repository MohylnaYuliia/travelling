package travelling.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import travelling.entity.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {
}
