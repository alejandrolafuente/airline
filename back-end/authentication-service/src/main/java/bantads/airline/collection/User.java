package bantads.airline.collection;

import java.io.Serializable;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements Serializable {

    @MongoId
    private String id;// could be reference in other services, the "key" request
    private String name;
    @Field(name = "login")
    @Indexed(unique = true)
    private String login; // email
    private String password;
    private String role;
    private String userStatus;

}
