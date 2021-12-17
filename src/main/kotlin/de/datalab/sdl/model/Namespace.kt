package de.datalab.sdl.model

import java.lang.IllegalArgumentException
import java.util.function.Consumer

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

class Namespace {
    private val map = LinkedHashMap<String, NamespaceMember>();
    fun add(namespaceMember: NamespaceMember){
        if (map.put(namespaceMember.name, namespaceMember) != null)
            throw IllegalArgumentException()
    }

    fun forEach(action: Consumer<in NamespaceMember>){
        with(map) { values.forEach(action) }
    }

    fun stream() = with(map) { values.stream() }
}