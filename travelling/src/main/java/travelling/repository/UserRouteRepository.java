package travelling.repository;

import org.springframework.data.repository.CrudRepository;
import travelling.entity.UserRouteEntity;
import travelling.entity.UserRouteId;

public interface UserRouteRepository extends CrudRepository<UserRouteEntity, UserRouteId> {
}
