package com.airline.sagas.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmpDeletedEvent {
    private String userId;
    private String name;
    private String employeeStatus;
    private String messageType;
}
