package travelling.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import travelling.entity.RouteEntity;

@Repository
public interface RouteRepository extends CrudRepository<RouteEntity, Integer> {
}
