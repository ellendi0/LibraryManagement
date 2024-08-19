package com.example.librarymanagement.repository.jpa

import com.example.librarymanagement.model.domain.Author
import com.example.librarymanagement.model.jpa.JpaAuthor
import com.example.librarymanagement.repository.AuthorRepository
import com.example.librarymanagement.repository.jpa.mapper.JpaAuthorMapper
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class JpaAuthorRepository(
        private val authorRepositorySpring: AuthorRepositorySpring,
): AuthorRepository {
    private fun Author.toEntity() = JpaAuthorMapper.toEntity(this)
    private fun JpaAuthor.toDomain() = JpaAuthorMapper.toDomain(this)

    override fun save(author: Author): Author {
        return authorRepositorySpring.save(author.toEntity()).toDomain()
    }

    override fun findById(authorId: String): Author? {
        return authorRepositorySpring.findByIdOrNull(authorId.toLong())?.toDomain()
    }

    override fun findAll(): List<Author> {
        return authorRepositorySpring.findAll().map { it.toDomain() }
    }
}

@Repository
interface AuthorRepositorySpring: JpaRepository<JpaAuthor, Long>
