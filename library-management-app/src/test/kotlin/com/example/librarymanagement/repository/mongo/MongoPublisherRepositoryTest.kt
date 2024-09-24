import com.example.librarymanagement.data.PublisherDataFactory
import com.example.librarymanagement.model.mongo.MongoPublisher
import com.example.librarymanagement.repository.mongo.MongoPublisherRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class MongoPublisherRepositoryTest {
    private val reactiveMongoTemplate: ReactiveMongoTemplate = mockk()
    private val mongoPublisherRepository = MongoPublisherRepository(reactiveMongoTemplate)

    private val mongoId = PublisherDataFactory.MONGO_ID
    private val id = mongoId.toString()

    private val publisher = PublisherDataFactory.createPublisher(id)
    private val mongoPublisher = PublisherDataFactory.createMongoPublisher(mongoId)

    @Test
    fun `should find all publishers`() {
        //GIVEN
        val expected = publisher

        every { reactiveMongoTemplate.findAll(MongoPublisher::class.java) } returns Flux.just(mongoPublisher)

        //WHEN
        val result = StepVerifier.create(mongoPublisherRepository.findAll())

        //THEN
        result
            .assertNext { actual ->
                Assertions.assertEquals(expected, actual)
                verify(exactly = 1) { reactiveMongoTemplate.findAll(MongoPublisher::class.java) }
            }
            .verifyComplete()
    }

    @Test
    fun `should create publisher`() {
        //GIVEN
        val expected = publisher

        every { reactiveMongoTemplate.save(mongoPublisher) } returns Mono.just(mongoPublisher)

        //WHEN
        val result = StepVerifier.create(mongoPublisherRepository.save(expected))

        //THEN
        result
            .assertNext { actual ->
                Assertions.assertEquals(expected, actual)
                verify(exactly = 1) { reactiveMongoTemplate.save(mongoPublisher) }
            }
            .verifyComplete()
    }

    @Test
    fun `should find by id`() {
        //GIVEN
        val expected = publisher

        every { reactiveMongoTemplate.findById(mongoId, MongoPublisher::class.java) } returns Mono.just(mongoPublisher)

        //WHEN
        val result = StepVerifier.create(mongoPublisherRepository.findById(id))

        //THEN
        result
            .assertNext { actual ->
                Assertions.assertEquals(expected, actual)
                verify(exactly = 1) { reactiveMongoTemplate.findById(mongoId, MongoPublisher::class.java) }
            }
            .verifyComplete()
    }
}
