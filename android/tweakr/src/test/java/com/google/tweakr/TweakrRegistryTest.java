// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.tweakr;

import com.google.tweakr.annotations.Tweak;
import com.google.tweakr.types.DefaultValueTypeConverter;
import com.google.tweakr.types.ValueType;
import com.google.tweakr.types.VoidValueType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TweakrRegistryTest {

    private static final String TEST_STRING = "this is a test";
    private static final int TEST_INT = 134426;
    private static final Float TEST_FLOAT = 434.324f;

    private enum TestEnum {
        SOMETHING,
        SOMETHING_ELSE
    }

    @Mock
    public TweakrRepo repo;

    private TweakrRegistry registry;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        registry = new TweakrRegistry(repo);
    }

    @Test
    public void tweakString() {
        Object testObject = new Object() {
          @Tweak
          public String testField = TEST_STRING;
        };

        registry.register(testObject, null);

        ValueType valueType = new DefaultValueTypeConverter().getType(String.class);
        verify(repo).add(eq("testField"), eq( 1), eq(valueType), eq(TEST_STRING),
            any());
    }

    @Test
    public void tweakInt() {
        Object testObject = new Object() {
          @Tweak
          public int testField = TEST_INT;
        };

        registry.register(testObject, null);

        ValueType valueType = new DefaultValueTypeConverter().getType(int.class);
        verify(repo).add(eq("testField"), eq(1), eq(valueType), eq(TEST_INT),
            any());
    }

    @Test
    public void tweakFloat() {
        Object testObject = new Object() {
          @Tweak
          public Float testField = TEST_FLOAT;
        };

        registry.register(testObject, null);

        ValueType valueType = new DefaultValueTypeConverter().getType(Float.class);
        verify(repo).add(eq("testField"), eq(1), eq(valueType), eq(TEST_FLOAT),
            any());
    }

    @Test
    public void tweakBoolean() {
        Object testObject = new Object() {
            @Tweak
            public Boolean testField = true;
        };

        registry.register(testObject, null);

        ValueType valueType = new DefaultValueTypeConverter().getType(boolean.class);
        verify(repo).add(eq("testField"), eq(1), eq(valueType), eq(true), any());
    }

    @Test
    public void tweakEnum() {
        Object testObject = new Object() {
          @Tweak
          public TestEnum testField = TestEnum.SOMETHING_ELSE;
        };

        registry.register(testObject, null);

        ValueType valueType = new DefaultValueTypeConverter().getType(TestEnum.class);
        verify(repo).add(eq("testField"), eq(1), eq(valueType), eq(TestEnum.SOMETHING_ELSE),
            any());
    }

    @Test
    public void tweakStringMethod() {
        Object testObject = new Object() {
            @Tweak
            public void setTest(String p) {

            }

            public String getTest() {
                return TEST_STRING;
            }
        };

        registry.register(testObject, null);

        ValueType valueType = new DefaultValueTypeConverter().getType(String.class);
        verify(repo).add(eq("setTest"), eq(1), eq(valueType), eq(TEST_STRING),
            any());
    }

    @Test
    public void tweakIntMethod() {
        Object testObject = new Object() {
            @Tweak
            public void setTest(int p) {

            }

            public int getTest() {
                return TEST_INT;
            }
        };

        registry.register(testObject, null);

        ValueType valueType = new DefaultValueTypeConverter().getType(int.class);
        verify(repo).add(eq("setTest"), eq(1), eq(valueType), eq(TEST_INT),
            any());
    }

    @Test
    public void tweakFloatMethod() {
        Object testObject = new Object() {
            @Tweak
            public void setTest(Float p) {

            }

            public Float getTest() {
                return TEST_FLOAT;
            }
        };

        registry.register(testObject, null);

        ValueType valueType = new DefaultValueTypeConverter().getType(Float.class);
        verify(repo).add(eq("setTest"), eq(1), eq(valueType), eq(TEST_FLOAT),
            any());
    }

    @Test
    public void tweakEnumMethod() {
        Object testObject = new Object() {
            @Tweak
            public void setTest(TestEnum p) {

            }

            public TestEnum getTest() {
                return TestEnum.SOMETHING_ELSE;
            }
        };

        registry.register(testObject, null);

        ValueType valueType = new DefaultValueTypeConverter().getType(TestEnum.class);
        verify(repo).add(eq("setTest"), eq(1), eq(valueType), eq(TestEnum.SOMETHING_ELSE),
            any());
    }

    @Test
    public void tweakVoidMethod() {
        Object testObject = new Object() {
            @Tweak
            public void setTest() {

            }
        };

        registry.register(testObject, null);

        ValueType valueType = new VoidValueType();
        verify(repo).add(eq("setTest"), eq(1), eq(valueType), isNull(), any());
    }

    @Test
    public void findsGetter() {
        Object testObject = new Object() {
            @Tweak
            public void setTest(String p) {

            }

            public String getTest() {
                return TEST_STRING;
            }
        };

        registry.register(testObject, null);

        ValueType valueType = new DefaultValueTypeConverter().getType(String.class);
        verify(repo).add(eq("setTest"), eq(1), eq(valueType), eq(TEST_STRING),
            any());
    }

    @Test
    public void findsGetterFluent() {
        Object testObject = new Object() {
            @Tweak
            public void setTest(String p) {

            }

            public String test() {
                return TEST_STRING;
            }
        };

        registry.register(testObject, null);

        ValueType valueType = new DefaultValueTypeConverter().getType(String.class);
        verify(repo).add(eq("setTest"), eq(1), eq(valueType), eq(TEST_STRING),
            any());
    }

    @Test
    public void findsGetterIs() {
        Object testObject = new Object() {
            @Tweak
            public void setTest(boolean p) {

            }

            public boolean isTest() {
                return true;
            }
        };

        registry.register(testObject, null);

        ValueType valueType = new DefaultValueTypeConverter().getType(boolean.class);
        verify(repo).add(eq("setTest"), eq(1), eq(valueType), eq(true), any());
    }

    @Test
    public void tweakDescription() {
        Object testObject = new Object() {
            @Tweak(description = "Test description")
            public Boolean testField = true;
        };

        registry.register(testObject, null);

        ValueType valueType = new DefaultValueTypeConverter().getType(boolean.class);
        ArgumentCaptor<TweakMetadata> captor = ArgumentCaptor.forClass(TweakMetadata.class);

        verify(repo).add(eq("testField"), eq(1), eq(valueType), eq(true), captor.capture());

        assertEquals("Test description", captor.getValue().getDescription());
    }
}