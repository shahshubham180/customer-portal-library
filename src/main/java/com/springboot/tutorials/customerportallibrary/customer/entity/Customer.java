package com.springboot.tutorials.customerportallibrary.customer.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Customer Information")
public class Customer {
	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Schema(description = "Auto Generated Customer ID", example = "1", hidden = true)
	private Long custId;
	
	@Schema(description = "Customer First Name", example = "Sam")
	private String firstName;
	
	@Schema(description = "Customer Last Name", example = "Michael")
	private String lastName;
	
	@Schema(description = "Customer Email ID", example = "Sam.Michael@gmail.com")
	private String email;
	
	@Schema(description = "Customer Age", example = "20")
	@Min(value = 18L, message = "Age must be equal to or greater than 18")
	private Long age;
	
	@Schema(description = "Book ID", example = "1")
	private Long bookId;
	
	@Schema(description = "Book Name", example = "Angels and Demons")
	private String bookName;
	
	private String plantype;
	
	private String occupation;

}
