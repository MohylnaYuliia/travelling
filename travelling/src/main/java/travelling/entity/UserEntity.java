package travelling.entity;

import lombok.*;

import javax.persistence.CollectionTable;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @CollectionTable(name = "user_id")
    private Integer id;

    private String name;
}
