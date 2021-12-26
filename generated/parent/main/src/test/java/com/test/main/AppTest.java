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
import com.test.service.LocalService;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;




@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={LocalService.class})
public class AppTest 
{

    static final Logger logger = LoggerFactory.getLogger(AppTest.class);

    @Autowired private IService service;


    public AppTest(){
    }


    @Test
    public void shouldDeliverSumOfAandB()
    {
        int a = 24;
        int b = 39;
        assertTrue(service.add(a, b) == a + b);
        logger.info("Add succeeded");
    }

    @Test
    public void shouldDeliverBasA()
    {
        int id = 1;
        String name = "B";
        int value = 2;
        A a = service.getBasA(id, name, value);
        assertTrue( a.id == id );
        assertTrue( a.name.equals(name) );
        B b = (B)a;
        assertTrue(b.value == value);

    }
}
