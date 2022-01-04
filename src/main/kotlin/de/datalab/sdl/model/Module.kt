package de.datalab.sdl.model


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

class Module(val service: Service, val groupId: JavaPath, val artifactId: String) {

    private val namespaces: MutableList<Namespace> = mutableListOf()

    init { service.add(this) }

    fun add(namespace: Namespace) = namespaces.add(namespace)

    fun stream() = namespaces.stream()

    fun getPath() = Path(listOf(service.path.pathString, artifactId, "src/main/java", groupId.pathString))

}