package com.example.library;

import com.example.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class LibraryApplicationTests {

	@Autowired
	private UserRepository userRepository;

}
