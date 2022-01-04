package de.datalab.sdl.generator

import de.datalab.sdl.model.*
import java.io.FileOutputStream
import java.io.PrintStream
import java.lang.IllegalArgumentException
import java.nio.file.Files
import java.nio.file.Path


/*
Copyright 2022 Gerhard Holzmeister

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




class JavaNamespaceGenerator(val namespace: Namespace) {

    fun prefixWithPackage(type: Type) =
        if (type is NamespaceMember)
            if (type.namespace != namespace) type.namespace.getPackage()+"."
            else ""
        else ""

    fun typeToString(type: Type, mandatory: Boolean): String = prefixWithPackage(type) + when(type) {
        is VoidType -> "void"
        is ClassType -> type.name
        is EnumType -> type.name
        is IntType -> if (mandatory) "int" else "Integer"
        is StringType -> "String"
        is ListType -> "ConstList<" + typeToString(type.type, false) + ">"
        else -> throw IllegalArgumentException(type.toString())
    }

    fun membersToString(
        members: List<Member>,
        separator: String = ", ",
        prefix: String = "",
        postfix: String = "",
        transform: ((Member)-> CharSequence)? = {"${typeToString(it.type, it.type.mandatory)} ${it.name}" }
    ) = members.joinToString(
        prefix = prefix,
        separator = separator,
        postfix = postfix,
        transform = transform )


    fun generateMethod(out: PrintStream, methodType: MethodType) {
        out.println("${typeToString(methodType.returnType, methodType.returnType.mandatory)} ${methodType.name}(${membersToString(methodType.parameters)});")
    }

    private fun firstCharToUpper(s: String) = s[0].uppercaseChar()+s.substring(1)

    private fun generateBuilder(out: PrintStream, classType: ClassType)
    {
        val builderClassName = "Builder"
        out.println("    public static class ${builderClassName} {")

        out.println(membersToString(classType.getAllMembers(), separator = "", transform =  {"        private ${typeToString(it.type, false)} ${it.name};\n" }))

        classType.getAllMembers().forEach { member ->
            out.println("        public Builder with${firstCharToUpper(member.name)}(${typeToString(member.type, member.type.mandatory)} ${member.name}) {")
            out.println("            this.${member.name} = ${member.name};")
            out.println("            return this;")
            out.println("        }")
        }

        out.println("        public ${classType.name} build() {")
        classType.getAllMembers().forEach { member ->
            if (member.mandatory)
            {
                out.println("            if (${member.name} == null) throw new IllegalArgumentException(\"${member.name} == null\"); ")
            }
        }

        out.println("            return new ${classType.name} (")
        out.println(classType.getAllMembers().map { member -> member.name }.joinToString(separator = ",\n", transform = {"                ${it}"}))
        out.println("            );")
        out.println("        }")
        out.println("    }")

    }



    private fun generateClass(out: PrintStream, classType: ClassType) {

        out.println("public class ${classType.name} ${if (classType.parent != null) "extends ${classType.parent.name} " else ""}{")
        // members
        out.println(
            membersToString(
                classType.members,
                separator = "",
                transform = { "    public final ${typeToString(it.type, it.type.mandatory)} ${it.name};\n" })
        )
        // constructor
        out.println("    public ${classType.name} (")
        // constructor arguments
        out.println(
            membersToString(
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

    private fun generateInterface(out: PrintStream, interfaceType: InterfaceType) {
        out.println("public interface " + interfaceType.name + "{")
        interfaceType.methods.forEach { generateMethod(out, it) }
        out.println("}")
        if (interfaceType is RemoteService)
        {
            val apiModule = Module(interfaceType.namespace.module.service, interfaceType.namespace.module.groupId, "restapi")
            val requestNamespace = Namespace(apiModule, JavaPath("api/dto/request"))
            val responseNamespace = Namespace(apiModule, JavaPath("api/dto/response"))
            val requestClass = ClassType(requestNamespace, interfaceType.remoteServiceData.requestName, null, listOf())
            val responseClass = ClassType(responseNamespace, interfaceType.remoteServiceData.responseName, null, listOf())
            interfaceType.methods.forEach {
                ClassType(requestNamespace, interfaceType.remoteServiceData.requestName+it.name, requestClass, it.parameters)
            }
            JavaNamespaceGenerator(requestNamespace).generateAndReturnFilename()
            val providerModule = Module(interfaceType.namespace.module.service, interfaceType.namespace.module.groupId, "${interfaceType.namespace.module.artifactId}/restprovider")
            val clientModule = Module(interfaceType.namespace.module.service, interfaceType.namespace.module.groupId, "${interfaceType.namespace.module.artifactId}/restclient")
        }
    }
    private fun generateEnum(out: PrintStream, enumType: EnumType)
    {
        out.println("enum ${enumType.name} {")
        out.println(enumType.values.joinToString (separator = ",\n",  transform = {"  ${it}"} ))
        out.println("}")
    }


    fun generateAndReturnFilename(namespaceMember: NamespaceMember): String {
        val path = Path.of(namespaceMember.getPath())
        Files.createDirectories(path.parent)
        val file = path.toFile()
        FileOutputStream(file).use { fos ->
            PrintStream(fos).use { out ->
                out.println("package ${namespaceMember.namespace.getPackage()};")
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

    fun generateAndReturnFilename() = namespace.stream().map{ member -> generateAndReturnFilename(member) }.toList()

}