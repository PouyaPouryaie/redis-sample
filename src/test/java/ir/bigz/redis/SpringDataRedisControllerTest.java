package ir.bigz.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.bigz.redis.person.Person;
import ir.bigz.redis.person.PersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class SpringDataRedisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonRepository repository;

    @Test
    void personList_shouldReturnNoneFound_whenNoDataExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/persons"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @DisplayName("savePerson")
    @Test
    void savePerson_shouldReturnCreatedId() throws Exception {
        Mockito.when(repository.save(any(Person.class))).thenReturn(Person.builder().uid(UUID.randomUUID().toString()).build());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"state\":\"tehran\",\n" +
                        "\"city\":\"tehran\",\n" +
                        "\"street\":\"narmak\"\n" +
                        "}")
                .param("firstName", "first")
                .param("lastName", "last"))
                .andExpect(status().isOk())
                .andReturn();
        Mockito.verify(repository).save(any(Person.class));
        Assertions.assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @Test
    void personsByFirstName_shouldReturnListOfPersons() throws Exception {
        Mockito.when(repository.findByFirstName("first"))
                .thenReturn(asList(
                        Person.builder().uid(UUID.randomUUID().toString()).firstName("first").lastName("last").build(),
                        Person.builder().uid(UUID.randomUUID().toString()).firstName("first").lastName("userLast").build()
                ));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/byFirstName/first"))
                .andExpect(status().isOk())
                .andReturn();
        Mockito.verify(repository).findByFirstName("first");

        ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder().build();
        List personList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);

        Assertions.assertEquals(personList.size(), 2);
    }
}
