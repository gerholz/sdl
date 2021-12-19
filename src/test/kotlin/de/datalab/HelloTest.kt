package de.datalab



import de.datalab.sdl.generator.JavaGenerator
import de.datalab.sdl.model.*
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.TimeUnit
import kotlin.test.Test
import kotlin.test.assertEquals


class HelloTest {


    private val logger = LoggerFactory.getLogger(javaClass)


    val groupId = JavaPath(listOf("de/datalab/hello"))
    private val artifactId = "app"


    val model = Model()
    val module = Module(model)
    val namespace = Namespace(module, listOf(JavaPath(listOf(groupId.pathString, "api"))))

    init {
        val a = ClassType(namespace, "A", null, listOf(Member("id", IntType()), Member("name", StringType())))
        val b = ClassType(namespace, "B", a, listOf(Member("value", IntType())))
        val c = ClassType(namespace, "C", a, listOf(Member("value", StringType())))
    }


    @Test
    fun `test `() {

        val rootJavaPath = JavaPath(listOf("generated"))
        val root = Files.createDirectories(Paths.get(rootJavaPath.pathString))

        val maven = ProcessBuilder(("mvn archetype:generate -DgroupId=${this.groupId.packageString} -DartifactId=${artifactId}" +
                " -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DinteractiveMode=false")
            .split(" "))
            .directory(root.toFile())
            .inheritIO()
            .start()

        val javaGenerator = JavaGenerator(model, rootJavaPath)
        maven.waitFor(1, TimeUnit.MINUTES)
        assertEquals(0, maven.exitValue())

        val filenames = javaGenerator.generate()

        val process = ProcessBuilder("/usr/bin/javac", "test/*.java").inheritIO().start()
        process.waitFor(1, TimeUnit.MINUTES)
        assertEquals(0, process.exitValue())

    }
}
