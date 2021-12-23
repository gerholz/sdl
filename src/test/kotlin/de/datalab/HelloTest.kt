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

    val model = Model(Path("generated/parent"))
    val module = Module(model, JavaPath("com/test"), "api")
    val namespaceDto = Namespace(module, JavaPath("api/dto"))
    val namespaceService = Namespace(module, JavaPath("api/service"))

    init {
        val a = ClassType(namespaceDto, "A", null, listOf(Member("id", IntType()), Member("name", StringType())))
        val b = ClassType(namespaceDto, "B", a, listOf(Member("value", IntType())))
        val c = ClassType(namespaceDto, "C", a, listOf(Member("value", StringType())))

        val service = InterfaceType(namespaceService, "IService", listOf(
            MethodType("getBasA", listOf(Member("id", IntType()), Member("name", StringType()), Member("value", IntType())), a)))
    }



    @Test
    fun `test `() {

        val root = Files.createDirectories(Paths.get(model.path.pathString))

        if (false) {
            val maven = ProcessBuilder(
                ("mvn archetype:generate -DgroupId=${module.groupId.packageString} -DartifactId=${module.artifactId}" +
                        " -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DinteractiveMode=false")
                    .split(" ")
            )
                .directory(root.toFile())
                .inheritIO()
                .start()
            maven.waitFor(1, TimeUnit.MINUTES)
            assertEquals(0, maven.exitValue())
        }

        val list = model.stream().flatMap {module -> module.stream()}.map { it.path }.toList()
        val javaGenerator = JavaGenerator(model)
        val filenames = javaGenerator.generate()

        val maven = ProcessBuilder(
            ("mvn install").split(" ")
        )
            .directory(File("${model.path.pathString}"))
            .inheritIO()
            .start()

        maven.waitFor(1, TimeUnit.MINUTES)
        assertEquals(0, maven.exitValue())
    }
}
