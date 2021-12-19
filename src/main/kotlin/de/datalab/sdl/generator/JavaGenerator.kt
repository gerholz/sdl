package de.datalab.sdl.generator

import de.datalab.sdl.model.*
import java.io.File
import java.io.FileOutputStream
import java.io.PrintStream
import java.io.PrintWriter
import java.lang.IllegalArgumentException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


/*
Copyright 2021 Gerhard Holzmeister

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

class JavaGenerator(val model: Model) {

    fun typeToString(out: PrintStream, type: Type): String = when(type) {
        is VoidType -> "void"
        is ClassType -> type.name
        is EnumType -> type.name
        is IntType -> if (type.mandatory) "int" else "Integer"
        is StringType -> "String"
        is ListType -> "ConstList<" + typeToString(out, type.type) + ">"
        else -> throw IllegalArgumentException(type.toString())
    }

    fun membersToString(
        out: PrintStream,
        members: List<Member>,
        separator: String = ", ",
        prefix: String = "",
        postfix: String = "",
        transform: ((Member)-> CharSequence)? = {"${typeToString(out, it.type)} ${it.name}" }
    ) = members.joinToString(
        prefix = prefix,
        separator = separator,
        postfix = postfix,
        transform = transform )

    fun generateMethod(out: PrintStream, methodType: MethodType) {
        out.println("${typeToString(out, methodType.returnType)} ${methodType.name}(${membersToString(out, methodType.parameters)});")
    }

    private fun generateInterface(out: PrintStream, interfaceType: InterfaceType) {
        out.println("interface " + interfaceType.name + "{")
        interfaceType.methods.forEach({generateMethod(out, it)})
        out.println("}")
    }

    private fun generateMembers(out: PrintStream, ) {

    }
    private fun generateEnum(out: PrintStream, enumType: EnumType)
    {
        out.println("enum ${enumType.name} {")
        out.println(enumType.values.joinToString (separator = ",\n",  transform = {"  ${it}"} ))
        out.println("}")
    }

    private fun generateBuilder(out: PrintStream, classType: ClassType)
    {
        val builderClassName = "Builder"
        out.println("    public static class ${builderClassName} {")

        out.println(membersToString(out, classType.members, separator = "", transform =  {"        private ${typeToString(out, it.type)} ${it.name};\n" }))

        out.println("    }")
    }

    private fun generateClass(out: PrintStream, classType: ClassType) {

        out.println("class ${classType.name} ${if (classType.parent != null) "extends ${classType.parent.name} " else ""}{")
        // members
        out.println(
            membersToString(
                out,
                classType.members,
                separator = "",
                transform = { "    public final ${typeToString(out, it.type)} ${it.name};\n" })
        )
        // constructor
        out.println("    ${classType.name} (")
        // constructor arguments
        out.println(
            membersToString(
                out,
                classType.getAllMembers(),
                separator = ",\n        ",
                prefix = "        ",
                postfix = ") {"
            )
        )
        // initializers
        if (classType.parent != null) {
            out.println(classType.parent.getAllMembers().stream().map { member -> member.name }.toList().joinToString ( prefix="    super(" , postfix = ");"))

        }
        out.println(
            membersToString(
                out,
                classType.members,
                separator = "\n    ",
                prefix = "    ",
                postfix = "",
                transform = { "this.${it.name} = ${it.name};" })
        )
        out.println("    }\n")
        generateBuilder(out, classType)
        out.println("}\n")
    }

    fun generateAndReturnFilename(namespaceMember: NamespaceMember): String {
        val path = Path.of(namespaceMember.getPath())
        Files.createDirectories(path.parent)
        val file = path.toFile()
        FileOutputStream(file).use { fos ->
            PrintStream(fos).use { out ->
                when (namespaceMember) {
                    is InterfaceType -> generateInterface(out, namespaceMember)
                    is ClassType -> generateClass(out, namespaceMember)
                    is EnumType -> generateEnum(out, namespaceMember)
                    else -> throw IllegalArgumentException()
                }
            }
        }
        return file.path
    }

    fun generate(): List<String> {
        val modules: List<Module> = model.stream().toList()
        val namespaces: List<Namespace> = modules.stream().flatMap { module -> module.stream() }.toList()
        val namespaceMembers: List<NamespaceMember> = namespaces.stream().flatMap { namespace -> namespace.stream() }.toList()
        val filenames: List<String> = namespaceMembers.stream().map { namespaceMember -> generateAndReturnFilename(namespaceMember) }.toList()
        return filenames
    }
}