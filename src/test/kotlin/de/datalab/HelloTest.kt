package de.datalab



import de.datalab.sdl.generator.JavaGenerator
import de.datalab.sdl.model.*
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.TimeUnit
import kotlin.test.Test
import kotlin.test.assertEquals


class HelloTest {

    private val logger = LoggerFactory.getLogger(javaClass)

    val model = Model(Path("generated"))
    val service = Service(model, "parent")
    val module = Module(service, JavaPath("com/test"), "api")
    val namespaceDto = Namespace(module, JavaPath("api/dto"))
    val namespaceService = Namespace(module, JavaPath("api/service"))

    init {
        val a = ClassType(namespaceDto, "A", null, listOf(Member("id", IntType()), Member("name", StringType())))
        val b = ClassType(namespaceDto, "B", a, listOf(Member("value", IntType())))
        val c = ClassType(namespaceDto, "C", a, listOf(Member("value", StringType())))

        val remoteService = RemoteService(namespaceService, "IService", listOf(
            MethodType("add", listOf(Member("a", IntType()), Member("b", IntType())), IntType()),
            MethodType("getBasA", listOf(Member("id", IntType()), Member("name", StringType()), Member("value", IntType())), a)),
            RemoteServiceData("", "Request", "Response")
            )
    }

    @Test
        fun `test `() {

        val root = Files.createDirectories(Paths.get(service.path.pathString))

        val javaGenerator = JavaGenerator(service)
        val filenames = javaGenerator.generate()

        val maven = ProcessBuilder(
            ("mvn install").split(" ")
        )
            .directory(File("${service.path.pathString}"))
            .inheritIO()
            .start()

        maven.waitFor(1, TimeUnit.MINUTES)
        assertEquals(0, maven.exitValue())
    }
}
