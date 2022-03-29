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

package com.google.tweakr.sample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.google.tweakr.Tweakr;
import com.google.tweakr.TweakrRepo;
import com.google.tweakr.annotations.Tweak;
import com.google.tweakr.types.ColorValueType;

public class CircleView extends View implements TweakrRepo.OnChangeListener {

    @Tweak
    private float centerX = 180;

    @Tweak
    private float centerY = 180;

    @Tweak
    protected float radius = 50;

    @Tweak
    Paint.Style style = Paint.Style.FILL;

    @Tweak(valueType = ColorValueType.class)
    public int color = Color.GREEN;

    private final Paint paint = new Paint();


    public CircleView(Context context) {
        super(context);
        init();
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CircleView(Context context,  AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        Tweakr.register(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        // Register onFieldChanged() to redraw when any value changes.
        Tweakr.addListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        Tweakr.removeListener(this);

        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setStyle(style);
        paint.setColor(color);

        canvas.drawCircle(centerX, centerY, radius, paint);
    }

    @Override
    public void onFieldChanged(String name, Object value) {
        // This is called whenever a field's value is changed in Tweakr's UI. We could be granular
        // here and check the name to match only the fields we care about, but for this demo it's
        // simple enough to just redraw whenever any value changes.
        invalidate();
    }
}
