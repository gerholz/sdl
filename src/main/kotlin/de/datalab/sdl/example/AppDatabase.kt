package de.datalab.sdl.example

import de.datalab.sdl.model.*


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

class AppDatabase {
    val model = Model()
    val module = Module(model)
    val namespace= Namespace(module, listOf(JavaPath(listOf("de.datalab.sdl.test"))))
    init {
        /*
        val type = ClassType(namespace,"Type")
        type.resolve(OptionalType(type), MemberList.builder().build()))
        val namedType = ClassType(namespace, "NamedType", type, MemberList.builder().add(Member("name", StringType())).build())
        val classMember = ClassType(namespace, "ClassMember", null, MemberList.builder()
            .add(Member("name", StringType()))
            .add(Member("type", type))
            .build())
        val classType = ClassType(namespace, "ClassType", namedType, MemberList.builder()
            .add(Member("parent", StringType()))
            .add(Member("classMemberList", ListType(classMember)))
            .build())
         */

        val categoryFromPortal =
            ClassType(
                namespace,
                "CategoryFromPortal",
                null,
                listOf(
                    Member("name", StringType()),
                    Member("displayName", StringType())
                )
            )

        val applicationFromPortal =
            ClassType(
                namespace,
                "ApplicationFromPortal",
                null,
                listOf(
                    Member("name", StringType()),
                    Member("version", StringType()),
                    Member("configSchemaVersion", IntType()),
                    Member("categories", ListType(categoryFromPortal))
                )
            )

        val appDatabaseService =
            InterfaceType(namespace, "AppDatabaseService", listOf(
                MethodType(
                    "storeApplication",
                    listOf<Member>(Member("applicationData", applicationFromPortal)),
                    VoidType()),
                MethodType(
                    "setDefaultVersion",
                    listOf<Member>(
                        Member("appLineId", IntType()),
                        Member("appVersionId", IntType(false))
                    ),
                    VoidType()
                )
            ))

       // val appData = ClassType(namespace, "AppData")

    }
}