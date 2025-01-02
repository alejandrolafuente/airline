package com.airline.sagas.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateEmployeeCommand {
    private String userID;
    private String name;
    private String cpf;
    private String email;
    private String phoneNumber;
    private String messageType;
}
