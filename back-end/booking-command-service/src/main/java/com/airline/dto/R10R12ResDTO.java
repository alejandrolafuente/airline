package com.airline.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class R10R12ResDTO {
    private UUID bookingId;
    private String bookingCode;
    private String initialStatus;
    private String currentStatus;
}
