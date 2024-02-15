package com.bezkoder.springjwt.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bezkoder.springjwt.models.Book;
import com.bezkoder.springjwt.payload.request.BookRequest;
import com.bezkoder.springjwt.repository.BookRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

	private final BookRepository repository;
	
	public void save(BookRequest request) {
		var book = Book.builder()
				.id(request.getId())
				.author(request.getAuthor())
				.isbn(request.getIsbn())
				.build();
		repository.save(book);
	}
	
	public List<Book> findAll() {
		return repository.findAll();
	}
}
