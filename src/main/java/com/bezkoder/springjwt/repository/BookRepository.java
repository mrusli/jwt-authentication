package com.bezkoder.springjwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bezkoder.springjwt.models.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

}
