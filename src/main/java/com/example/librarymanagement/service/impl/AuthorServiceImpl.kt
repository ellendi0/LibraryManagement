package com.example.librarymanagement.service.impl

import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.model.entity.Author
import com.example.librarymanagement.repository.AuthorRepository
import com.example.librarymanagement.service.AuthorService
import org.springframework.stereotype.Service

@Service
class AuthorServiceImpl(private val authorRepository: AuthorRepository) : AuthorService {
    override fun createAuthor(author: Author): Author = authorRepository.save(author)

    override fun updateAuthor(updatedAuthor: Author): Author {
        val author = getAuthorById(updatedAuthor.id!!).copy(
            firstName = updatedAuthor.firstName,
            lastName = updatedAuthor.lastName
        )
        return authorRepository.save(author)
    }

    override fun getAuthorById(id: Long): Author {
        return authorRepository.findById(id).orElseThrow { throw EntityNotFoundException("Author") }
    }

    override fun getAllAuthors(): List<Author> = authorRepository.findAll()
}
