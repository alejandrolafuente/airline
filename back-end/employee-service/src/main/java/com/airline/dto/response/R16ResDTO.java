package com.airline.dto.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class R16ResDTO {
    private UUID id;
    private String userId;
    private String name;
    private String cpf;
    private String email;
    private String phoneNumber;
}
