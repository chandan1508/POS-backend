package com.chandan.pos.payload.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CustomerDto {

    private Long id;

    @NotBlank(message = "Full name is required")
    private String fullName;

    private String email;

    private String phone;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}