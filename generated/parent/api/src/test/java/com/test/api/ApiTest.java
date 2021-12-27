package com.test.api;

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

import static org.junit.Assert.assertTrue;

import com.test.api.dto.A;
import com.test.api.dto.B;
import org.junit.Test;


public class ApiTest
{

    @Test
    public void creatingBWithBuilderShouldWork()
    {
        int id = 123;
        String name = "name";
        int value = 456;
        A a = new B.Builder().withId(id).withName(name).withValue(value).build();
        assertTrue(a instanceof B);
        B b = (B)a;
        assertTrue( b.id == id );
        assertTrue( b.name.equals(name) );
        assertTrue( b.value == value );
    }
}
