package com.arg.swu.models;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class DigitalSignatureConsole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    private String status;
    private String message;
    private Date createdDate;
}
