package com.springboot.tutorials.customerportallibrary.customer.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.springboot.tutorials.customerportallibrary.customer.entity.Customer;
import com.springboot.tutorials.customerportallibrary.customer.service.CustomerService;
import com.springboot.tutorials.customerportallibrary.exception.BookIdAndNameMismatchException;
import com.springboot.tutorials.customerportallibrary.exception.DataNotFoundException;
import com.springboot.tutorials.customerportallibrary.exception.ErrorMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("customers")
@Tag(name = "Customer Controller", description = "CRUD Operation on Customer Account")
@SecurityRequirement(name = "swaggerSecurity")
public class CustomerController {


	private static final Logger log = LoggerFactory.getLogger(CustomerController.class);
			
	@Autowired
	private CustomerService service;
	
	@Operation(summary = "Endpoint to create a Customer Account", tags = {"Customer Controller"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Saved Customer in DB", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class))}),
			@ApiResponse(responseCode = "500", description = "Error while Saving Customer in DB", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))}),
			@ApiResponse(responseCode = "400", description = "Invalid Value in Request Body ", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))}),
			@ApiResponse(responseCode = "401", description = "Unauthorized Access", content = @Content),
			@ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)	
	})
	@PostMapping("/createCustomer")
	public Customer createCustomerAccount(@Parameter(description = "Customer to be Added, cannot be null", required = true, 
			schema = @Schema(implementation = Customer.class))@RequestBody @Valid Customer customer) throws BookIdAndNameMismatchException, DataNotFoundException, JsonMappingException, JsonProcessingException {
		log.info("Inside createCustomerAccount method of CustomerController");
		return service.createCustomerAccount(customer);
	}
	
	@Operation(summary = "Endpoint to Retrieve a Customer Account", tags = {"Customer Controller"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Retrieved Customer from DB", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class))}),
			@ApiResponse(responseCode = "500", description = "Error while Retriving Customer from DB", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))}),
			@ApiResponse(responseCode = "404", description = "Customer Info Not Found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))}),
			@ApiResponse(responseCode = "401", description = "Unauthorized Access", content = @Content),
			@ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)	
	})
	@GetMapping("/getCustomer/{custId}")
	public Customer getCustomerAccount(@Parameter(description = "Customer Id of Customer", required = true)@PathVariable("custId") Long custId) throws DataNotFoundException {
		log.info("Inside getCustomerAccount method of CustomerController");
		log.info("Request Parameter",custId);
		return service.getCustomerInfo(custId);
	}
	
	@Operation(summary = "Endpoint to delete a Customer from DB", tags = {"Customer Controller"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Deleted Customer from DB", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class))}),
			@ApiResponse(responseCode = "500", description = "Error while deleting Customer from DB", content = @Content),
			@ApiResponse(responseCode = "404", description = "Customer Not Found", content = @Content),
			@ApiResponse(responseCode = "401", description = "Unauthorized Access", content = @Content),
			@ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)	
	})
	@DeleteMapping("/delete")
	public void deleteCustomer(@Parameter(description = "Customer to be deleted, cannot be null", required = true)@RequestBody Customer customer) throws DataNotFoundException {
		log.info("Inside deleteCustomer method of CustomerController");
		log.info("Customer ID to be deleted",customer.getBookId());
		service.deleteCustomer(customer);	
	}
	
	@Operation(summary = "Endpoint to Suggest a Book to Customer", tags = {"Customer Controller"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Suggestion Successful", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class))}),
			@ApiResponse(responseCode = "500", description = "Error while Suggesting Book to Customer", content = @Content),
			@ApiResponse(responseCode = "404", description = "Customer Not Found", content = @Content),
			@ApiResponse(responseCode = "401", description = "Unauthorized Access", content = @Content),
			@ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)	
	})
	@PostMapping("/suggestion")
	public Customer suggestBookToCustomer(@Parameter(description = "Customer Details For Suggestion, cannot be null", required = true)@RequestBody Customer customer) throws Exception{
		log.info("Inside suggestBookToCustomer method of CustomerController");
		log.info("Customer Age {}",customer.getAge());
		return service.bookSuggestion(customer);	
	}
}
