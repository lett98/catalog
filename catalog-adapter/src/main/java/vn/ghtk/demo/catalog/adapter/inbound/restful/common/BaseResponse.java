package vn.ghtk.demo.catalog.adapter.inbound.restful.common;

import lombok.*;

import java.time.LocalDateTime;

@Getter@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {
    /**
     * The result of the response, true if successful and false otherwise
     */
    private boolean success;
    private String timestamp;


    /**
     * Response data
     */
    private T data;

    /**
     * Response message
     */
    private String message;

    public static <T> BaseResponse<T> success(T data) {
        return BaseResponse.<T>builder()
                .success(true)
                .message("Successful!")
                .timestamp(LocalDateTime.now().toString())
                .data(data)
                .build();
    }

    public static <T> BaseResponse<T> fail(String message) {
        return BaseResponse.<T>builder()
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

}
