package com.springboot.tutorials.customerportallibrary.customer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {

	private Long bookId;

	private String bookName;

	private String author;

	private String publisher;
}
