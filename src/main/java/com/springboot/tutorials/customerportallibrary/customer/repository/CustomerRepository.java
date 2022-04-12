package com.springboot.tutorials.customerportallibrary.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.tutorials.customerportallibrary.customer.entity.Customer;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	public Customer getCustomerByCustId(Long custId);

}
