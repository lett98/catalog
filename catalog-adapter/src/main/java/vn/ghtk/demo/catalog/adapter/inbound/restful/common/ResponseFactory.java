package vn.ghtk.demo.catalog.adapter.inbound.restful.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class ResponseFactory {
    private ResponseFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static <I> ResponseEntity<BaseResponse<I>> success(I data) {
        BaseResponse response = BaseResponse.builder()
                .success(true)
                .data(data)
                .timestamp(LocalDateTime.now().toString())
                .message("Successful!")
                .build();
        return ResponseEntity.ok(response);
    }

    public static <I> ResponseEntity<BaseResponse<I>> error(HttpStatus httpStatus) {
        BaseResponse response = BaseResponse.builder()
                .success(false)
                .data(null)
                .timestamp(LocalDateTime.now().toString())
                .message("Something went wrong.")
                .build();
        return new ResponseEntity<>(response, httpStatus);
    }

    public static <T> ResponseEntity<BaseResponse<T>> error(HttpStatus httpStatus, String message) {
        BaseResponse response = BaseResponse.builder()
                .success(false)
                .timestamp(LocalDateTime.now().toString())
                .message(message)
                .data(null).build();
        return new ResponseEntity<>(response, httpStatus);
    }

}
