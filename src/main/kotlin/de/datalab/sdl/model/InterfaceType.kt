package de.datalab.sdl.model

import java.lang.IllegalArgumentException


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

open class InterfaceType(final override val namespace: Namespace, override val name: String, val methods: List<MethodType>): NamespaceMember {
    init {
        namespace.add(this)
        val methodNames: MutableSet<String> = HashSet()
        methods.forEach({if (!methodNames.add(it.name)) throw IllegalArgumentException()})
    }
}