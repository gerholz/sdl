package de.datalab



import de.datalab.sdl.generator.JavaGenerator
import de.datalab.sdl.model.*
import org.slf4j.LoggerFactory
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.test.Test
import kotlin.test.assertEquals


class HelloTest {
    private val logger = LoggerFactory.getLogger(javaClass)

    class Model: IModel {
        val namespace = Namespace()
        init {
            val a = ClassType(namespace, "A", null, listOf(Member("id", IntType()), Member("name", StringType())))
            val b = ClassType(namespace, "B", a, listOf(Member("value", IntType())))
            val c = ClassType(namespace, "C", a, listOf(Member("value", StringType())))
        }

        override fun getNamespaces(): List<Namespace> {
            return listOf(namespace)
        }
    }

    @Test
    fun `test `() {
        val javaGenerator = JavaGenerator(Model())
        val maven = ProcessBuilder(("mvn archetype:generate -DgroupId=com.mycompany.app -DartifactId=my-app" +
                " -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DinteractiveMode=false")
            .split(" "))
            .directory(File("generated"))
            .inheritIO()
            .start()

        maven.waitFor(1, TimeUnit.MINUTES)
        assertEquals(0, maven.exitValue())

        val filenames = javaGenerator.generate()

        val process = ProcessBuilder("/usr/bin/javac", "test/*.java").inheritIO().start()
        process.waitFor(1, TimeUnit.MINUTES)
        assertEquals(0, process.exitValue())

    }
}
