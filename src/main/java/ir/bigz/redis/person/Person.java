package ir.bigz.redis.person;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@RedisHash
public class Person {
    @Id
    private String uid;
    @Indexed
    private String firstName;
    private String lastName;
    private Address address;
}
