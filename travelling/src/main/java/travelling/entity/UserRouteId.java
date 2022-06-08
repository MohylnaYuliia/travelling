package travelling.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRouteId implements Serializable {

    Integer userId;

    Integer routeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRouteId that = (UserRouteId) o;
        return userId.equals(that.userId) &&
                routeId.equals(that.routeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, routeId);
    }
}
