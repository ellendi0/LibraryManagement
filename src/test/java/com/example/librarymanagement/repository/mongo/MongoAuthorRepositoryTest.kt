import com.example.librarymanagement.data.AuthorDataFactory
import com.example.librarymanagement.model.mongo.MongoAuthor
import com.example.librarymanagement.repository.mongo.MongoAuthorRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.data.mongodb.core.MongoTemplate

//@DataMongoTest
//@ActiveProfiles(profiles = ["test", "mongo"])
//@Import(MongoAuthorRepository::class)
class MongoAuthorRepositoryTest {
    private val mongoTemplate: MongoTemplate = mockk()
    private val mongoAuthorRepository = MongoAuthorRepository(mongoTemplate)

    private val mongoId = AuthorDataFactory.MONGO_ID
    private val id = mongoId.toString()

    private val author = AuthorDataFactory.createAuthor(id)
    private val mongoAuthor = AuthorDataFactory.createMongoAuthor(mongoId)

    @Test
    fun shouldFindAllAuthors() {
        //GIVEN
        val expected = listOf(author)

        every { mongoTemplate.findAll(MongoAuthor::class.java) } returns listOf(mongoAuthor)

        //WHEN
        val actual = mongoAuthorRepository.findAll()

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldCreateAuthor() {
        //GIVEN
        val expected = author

        every { mongoTemplate.save(mongoAuthor) } returns mongoAuthor

        //WHEN
        val actual = mongoAuthorRepository.save(expected)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { mongoTemplate.save(mongoAuthor) }
    }

    @Test
    fun shouldFindById() {
        //GIVEN
        val expected = author

        every { mongoTemplate.findById(mongoId, MongoAuthor::class.java) } returns mongoAuthor

        //WHEN
        val actual = mongoAuthorRepository.findById(id)

        //THEN
        Assertions.assertEquals(expected, actual)
    }
//    @Autowired
//    lateinit var mongoAuthorRepository: MongoAuthorRepository
//
//    private val author = AuthorDataFactory.createAuthor().copy(id = null)
//
//    @Test
//    fun shouldSaveAuthor() {
//        //given
//        val expected = author
//
//        //when
//        val actual = mongoAuthorRepository.save(author)
//
//        //then
//        Assertions.assertEquals(expected.firstName, actual.firstName)
//        Assertions.assertEquals(expected.lastName, actual.lastName)
//        Assertions.assertNotNull(actual.id)
//    }
//
//    @Test
//    fun shouldById() {
//        //given
//        val saved = mongoAuthorRepository.save(author)
//        val expectedId = saved.id
//
//        //when
//        val actual = mongoAuthorRepository.findById(expectedId!!)
//
//        //then
//        Assertions.assertNotNull(actual)
//        Assertions.assertEquals(author.firstName, actual?.firstName)
//        Assertions.assertEquals(author.lastName, actual?.lastName)
//    }
//
//    @Test
//    fun shouldFindAllAuthors() {
//        //given
//        val savedAuthor = mongoAuthorRepository.save(author)
//
//        //when
//        val allAuthors = mongoAuthorRepository.findAll()
//
//        //then
//        Assertions.assertTrue(allAuthors.isNotEmpty())
//        val foundAuthor = allAuthors.firstOrNull()
//        Assertions.assertNotNull(foundAuthor)
//        Assertions.assertEquals(author.firstName, foundAuthor?.firstName)
//        Assertions.assertEquals(author.lastName, foundAuthor?.lastName)
//    }
}
