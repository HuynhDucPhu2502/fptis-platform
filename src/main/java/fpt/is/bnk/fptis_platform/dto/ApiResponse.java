package fpt.is.bnk.fptis_platform.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * Admin 11/25/2025
 *
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    @Builder.Default
    private int code = 1000;

    private String message;
    private T result;

    public ApiResponse(int code, String message) {
        this.code = code;
        this.message = message;
        this.result = null;
    }
}
