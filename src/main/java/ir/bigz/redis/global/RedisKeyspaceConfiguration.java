package ir.bigz.redis.global;

import ir.bigz.redis.person.Person;
import org.springframework.data.redis.core.convert.KeyspaceConfiguration;

import java.util.Arrays;

public class RedisKeyspaceConfiguration extends KeyspaceConfiguration {

    @Override
    protected Iterable<KeyspaceSettings> initialConfiguration() {

        return Arrays.asList(
                new KeyspaceSettings(Person.class, Person.class.getSimpleName() + ":Test")
        );
    }
}
