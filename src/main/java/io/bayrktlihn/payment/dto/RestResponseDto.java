package io.bayrktlihn.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestResponseDto<T> {
    private T data;
    private boolean error;
    private List<String> errorMessages;


    public static RestResponseDto createFailResponse() {
        return RestResponseDto.builder()
                .data(null)
                .error(true)
                .build();
    }

    public static RestResponseDto createFailResponse(String... messages) {
        List<String> errorMessages = Arrays.stream(messages).toList();
        return RestResponseDto.builder()
                .data(null)
                .error(true)
                .errorMessages(errorMessages)
                .build();
    }

}
