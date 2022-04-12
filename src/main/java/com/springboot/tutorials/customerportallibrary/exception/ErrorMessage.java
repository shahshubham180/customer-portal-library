package com.springboot.tutorials.customerportallibrary.exception;

import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Error Object")
public class ErrorMessage {

	@Schema(description = "Http Response Status", example = "404,400,500")
	private HttpStatus status;
	
	@Schema(description = "Error Message", example = "Data Not Found in DB, Bad Request")
	private String errormessage;
}
