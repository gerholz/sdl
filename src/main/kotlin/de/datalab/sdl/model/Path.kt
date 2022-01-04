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

open class Path(paths: List<String>) {

    val pathString = validate(paths)

    constructor(string: String): this(listOf(string))

    fun validate(paths: List<String>): String {
        assert(paths.size > 0)
        paths.forEach{path -> path.split("/").forEach { assert( it.matches(Regex("[a-z][a-z0-9]*")),{it})}}
        return paths.joinToString(separator = "/")
    }
}