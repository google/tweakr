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

package com.google.tweakr.annotations;

import com.google.tweakr.types.ValueType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

/**
 * Add this annotation to any field or method you want to alter in the Tweakr UI.
 *
 * Most primitive types are supported automatically. If you want to handle a custom type, you will
 * need to implement {@link com.google.tweakr.types.ValueType} and either specify it in the
 * {@link #valueType()} attribute, or register your own
 * {@link com.google.tweakr.types.ValueTypeConverter} to return your custom ValueType whenever it
 * encounters a field using your type.
 *
 * If annotating a method, that method will be called with the updated value whenever you alter it
 * in the UI. If the method is a setter, it will automatically try to derive the initial value from
 * the corresponding getter (if it can find it).
 *
 * Only methods that take 0 or 1 parameters are currently supported.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD,FIELD})
public @interface Tweak {

    /**
     * If specified, Tweaks a child member of the annotated object instead of the object itself.
     *
     * If the member is a property, Tweakr will automatically call the getters and setters as
     * appropriate, e.g.:
     *
     * if child="text", Tweakr will call "getText()" and "setText()".
     *
     * If there are multiple overloads of the method that take different parameters, you may
     * specify which overload to use by appending the parameter type in parenthesis, e.g.:
     *
     * child="setText(CharSequence)" or child="setText(int)"
     *
     * Only methods that take 0 or 1 parameters are currently supported.
     */
    String child() default "";

    /**
     * If specified, uses the given {@link com.google.tweakr.types.ValueType} instead of one
     * provided by the registered {@link com.google.tweakr.types.ValueTypeConverter}. This is
     * helpful if you want to specify a custom UI to tweak types that otherwise already have a
     * default UI.
     *
     * e.g. to present a color picker UI for an "int" field instead of the default number slider,
     * use `@Tweak(valueType = ColorValueType.class)`
     *
     */
    Class<? extends ValueType> valueType() default ValueType.class;

    /**
     * A description to display in the Tweakr UI.
     */
    String description() default "";
}
