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
    val module = Module(model, JavaPath("de/datalab/hello"), "app")
    val namespace = Namespace(module, JavaPath("api/dto"))

    init {
        val a = ClassType(namespace, "A", null, listOf(Member("id", IntType()), Member("name", StringType())))
        val b = ClassType(namespace, "B", a, listOf(Member("value", IntType())))
        val c = ClassType(namespace, "C", a, listOf(Member("value", StringType())))
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
            .directory(File("${model.path.pathString}/${module.artifactId}"))
            .inheritIO()
            .start()

        maven.waitFor(1, TimeUnit.MINUTES)
        assertEquals(0, maven.exitValue())
    }
}
