package com.test.service;

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

import com.test.api.dto.A;
import com.test.api.dto.B;
import com.test.api.service.IService;
import org.springframework.stereotype.Service;

@Service
public class LocalService implements IService {

    @Override
    public int add(int a, int b) {
        return a + b;
    }

    @Override
    public A getBasA(int id, String name, int value) {
        return new B(id, name, value);
    }
}
