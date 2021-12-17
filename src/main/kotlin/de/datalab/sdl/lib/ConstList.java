package de.datalab.sdl.lib;
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ConstList<T> {
    private final List<T> list;
    public ConstList(List<T> list) {
        this.list = new ArrayList<T>(list);
    }

    List<T> get()
    {
        return new ArrayList<T>(list);
    }

    T get(int idx)
    {
        return list.get(idx);
    }

    void forEach(Consumer<? super T> action) {
        list.forEach(action);
    }

    Stream<T> stream() {
        return list.stream();
    }

}
