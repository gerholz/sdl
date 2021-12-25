package com.test.main;

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
import com.test.api.service.IService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class )
@SpringBootTest
public class AppTest 
{

    private final IService service;

    @Autowired
    AppTest(IService service){
        this.service = service;
    }


    @Test
    public void shouldDeliverBasA()
    {
        int id = 1;
        String name = "B";
        int value = 2;
        A a = service.getBasA(id, name, value);
        assertTrue( a.id == id );
        assertTrue( a.name == name );
        B b = (B)a;
        assertTrue(b.value == value);

    }
}
