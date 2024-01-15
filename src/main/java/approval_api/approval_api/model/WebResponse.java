package approval_api.approval_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class WebResponse<T> {

    private String status;

    private String message;

    private T data;

    private String errors;



}
