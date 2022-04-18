package com.springboot.tutorials.customerportallibrary.customer.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.tutorials.customerportallibrary.customer.entity.Customer;
import com.springboot.tutorials.customerportallibrary.customer.model.Book;
import com.springboot.tutorials.customerportallibrary.customer.repository.CustomerRepository;
import com.springboot.tutorials.customerportallibrary.customer.utils.CustomerUtils;
import com.springboot.tutorials.customerportallibrary.exception.BookIdAndNameMismatchException;
import com.springboot.tutorials.customerportallibrary.exception.DataNotFoundException;


@Service
@RefreshScope
public class CustomerService {
	
	private static final Logger log = LoggerFactory.getLogger(CustomerService.class);
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private CustomerUtils utils;
	
	@Autowired
	private RestTemplate template;
	
	private List<String> fullNames = new ArrayList<>();
	
	@Value("${senior.citizen}")
	private String seniorAgeGroup;
	
	@Value("${junior.citizen}")
	private String juniorAgeGroup;
	
	@Value("${library-app.uri}")
	private String libraryUri;
	
	@Value("${auth.password}")
	private String password;

	public Customer createCustomerAccount(Customer customer) throws BookIdAndNameMismatchException, DataNotFoundException, JsonMappingException, JsonProcessingException {
		log.info("Inside createCustomerAccount method of CustomerService");
		if(StringUtils.isNotBlank(customer.getBookName())) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String username = ((UserDetails)principal).getUsername();
			HttpHeaders headers = new HttpHeaders();
			headers.setBasicAuth(username, password);
			HttpEntity request = new HttpEntity(headers);
			ResponseEntity<Book> response = template.exchange(libraryUri+"/books/getBookInfo/"+customer.getBookName(),HttpMethod.GET, request, Book.class);
			Book book = response.getBody();
			if(Objects.isNull(book)) {
				log.info("Book is not registered. Please register the Book");
				throw new DataNotFoundException("Book is not registered. Please register the Book");
			}
			if(!StringUtils.equals(String.valueOf(customer.getBookId()), String.valueOf(book.getBookId()))) {
				log.info("The Book Id and Book Name does not match in DB");
				throw new BookIdAndNameMismatchException("Book Id is invalid for the Book "+customer.getBookName());
			}
		}else
			throw new DataNotFoundException("The customer must register with a Book");
		return customerRepository.save(customer);
	}

	public Customer getCustomerInfo(Long custId) throws DataNotFoundException {
		log.info("Inside getCustomerInfo method of CustomerService");
		Customer customer = customerRepository.getCustomerByCustId(custId);
		if(Objects.isNull(customer)) {
			log.info("Customer does not Exist with Cust Id : {}",custId);
			throw new DataNotFoundException("Customer does not Exist with Cust Id :"+custId);
		}
		String fullName = utils.createFullName(customer.getFirstName(), customer.getLastName());
		fullNames.add(fullName);
		return customer;
	}
	
	public void deleteCustomer(Customer customer) throws DataNotFoundException {
		Customer customerdb = customerRepository.getCustomerByCustId(customer.getCustId());
		if(Objects.isNull(customerdb))
			throw new DataNotFoundException("Customer Does Not Exist To Delete");
		customerRepository.delete(customer);
	}

	public Customer bookSuggestion(Customer customer) throws JsonMappingException, JsonProcessingException {
		log.info("Inside bookSuggestion method of CustomerService");
		if(Objects.isNull(customer.getAge())) {
			log.info("Calling Public API");
			ResponseEntity<String> response = template.getForEntity("https://api.agify.io?name="+customer.getFirstName(), String.class);
			String jsonString = response.getBody();
			log.info(jsonString.toString());
			ObjectMapper mapper = new ObjectMapper();
				Map<String,Object> map = mapper.readValue(jsonString, Map.class);
				Integer age = (Integer) map.get("age");
				customer.setAge(age.longValue());
		}
		if(customer.getAge() < 60)
			customer.setBookName(juniorAgeGroup);
		else
			customer.setBookName(seniorAgeGroup);
		return customer;
	}
}
