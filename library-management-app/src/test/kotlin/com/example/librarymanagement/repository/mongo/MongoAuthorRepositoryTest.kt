import com.example.librarymanagement.data.AuthorDataFactory
import com.example.librarymanagement.model.mongo.MongoAuthor
import com.example.librarymanagement.repository.mongo.MongoAuthorRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class MongoAuthorRepositoryTest {
    private val reactiveMongoTemplate: ReactiveMongoTemplate = mockk()
    private val mongoAuthorRepository = MongoAuthorRepository(reactiveMongoTemplate)

    private val mongoId = AuthorDataFactory.MONGO_ID
    private val id = mongoId.toString()

    private val author = AuthorDataFactory.createAuthor(id)
    private val mongoAuthor = AuthorDataFactory.createMongoAuthor(mongoId)

    @Test
    fun `should find all authors`() {
        //GIVEN
        val expected = author

        every { reactiveMongoTemplate.findAll(MongoAuthor::class.java) } returns Flux.just(mongoAuthor)

        //WHEN
        val result = StepVerifier.create(mongoAuthorRepository.findAll())

        //THEN
        result
            .assertNext { actual ->
                Assertions.assertEquals(expected, actual)
                verify(exactly = 1) { reactiveMongoTemplate.findAll(MongoAuthor::class.java) }
            }
            .verifyComplete()
    }

    @Test
    fun `should create author`() {
        //GIVEN
        val expected = author

        every { reactiveMongoTemplate.save(mongoAuthor) } returns Mono.just(mongoAuthor)

        //WHEN
        val result = StepVerifier.create(mongoAuthorRepository.save(expected))

        //THEN
        result
            .assertNext { actual ->
                Assertions.assertEquals(expected, actual)
                verify(exactly = 1) { reactiveMongoTemplate.save(mongoAuthor) }
            }
            .verifyComplete()
    }

    @Test
    fun `should find by id`() {
        //GIVEN
        val expected = author

        every { reactiveMongoTemplate.findById(mongoId, MongoAuthor::class.java) } returns Mono.just(mongoAuthor)

        //WHEN
        val result = StepVerifier.create(mongoAuthorRepository.findById(id))

        //THEN
        result
            .assertNext { actual ->
                Assertions.assertEquals(expected, actual)
                verify(exactly = 1) { reactiveMongoTemplate.findById(mongoId, MongoAuthor::class.java) }
            }
            .verifyComplete()
    }
}
