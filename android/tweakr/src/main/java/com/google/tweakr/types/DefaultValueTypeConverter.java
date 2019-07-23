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

package com.google.tweakr.types;

public class DefaultValueTypeConverter implements ValueTypeConverter {

    @Override
    public ValueType getType(Class<?> fieldType) {
        if (fieldType.isArray()) {
            return new ArrayValueType(getType(fieldType.getComponentType()));
        }

        if (fieldType == float.class || fieldType == double.class || fieldType == Float.class || fieldType == Double.class) {
            return new PrimitiveValueType(PrimitiveValueType.TYPE_FLOAT);
        } else if (fieldType == int.class || fieldType == long.class || fieldType == Integer.class || fieldType == Long.class) {
            return new PrimitiveValueType(PrimitiveValueType.TYPE_INT);
        } else if (fieldType == boolean.class || fieldType == Boolean.class) {
            return new PrimitiveValueType(PrimitiveValueType.TYPE_BOOLEAN);
        } else if (fieldType.isAssignableFrom(String.class)) {
            return new PrimitiveValueType(PrimitiveValueType.TYPE_STRING);
        } else if (fieldType.isEnum()) {
            return new EnumValueType(fieldType);
        }

        return new PrimitiveValueType(PrimitiveValueType.TYPE_UNKNOWN);
    }
}
