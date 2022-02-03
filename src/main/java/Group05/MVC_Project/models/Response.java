package Group05.MVC_Project.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Response {
    private boolean status;
    private boolean error;
    private String message;
    private String exception;
    private List<Object> dataset = new ArrayList<>();

    public Response() {
        this.status = false;
        this.error = false;
        this.message = null;
        this.exception = null;
        this.dataset = null;
    }

}
