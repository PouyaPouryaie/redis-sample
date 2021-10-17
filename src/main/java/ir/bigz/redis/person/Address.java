package ir.bigz.redis.person;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class Address {

    private String state;
    private String city;
    private String street;
}
