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

class ObjectService: IModel {
    val namespace = Namespace()
    init {
        val objectType = EnumType(namespace, "ObjectType", listOf("DEVICE", "DEVICE_DIRECTORY"))
        val typedId = ClassType(namespace, "TypedId", null, listOf(Member("objectType", objectType), Member("id", IntType())))

        val request = ClassType(namespace, "Request", null, listOf())
        val getEditDataForNewObject = ClassType(namespace, "GetEditDataForNewObject", request, listOf(Member("objectType", objectType), Member("parentId", typedId)))

    }

    override fun getNamespaces(): List<Namespace> {
        return listOf(namespace)
    }
}