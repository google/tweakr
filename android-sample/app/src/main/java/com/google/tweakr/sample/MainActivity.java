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

//import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.google.tweakr.Tweakr;
import com.google.tweakr.annotations.Tweak;

public class MainActivity extends Activity {

    @Tweak(child ="setText(CharSequence)")
    public TextView introText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        introText = findViewById(R.id.text);

        // Make sure to register AFTER you have set all your View fields!
        Tweakr.register(this);
    }

    @Tweak
    public void animateText() {
        introText.animate()
                .setDuration(700)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .rotationXBy(360)
                .start();
    }

    @Tweak
    public void setTextShown(boolean showText) {
        introText.setVisibility(showText? View.VISIBLE : View.INVISIBLE);
    }

    // Tweakr will check this getter when inspecting the initial value of the setTextShown() setter.
    public boolean isTextShown() {
        return introText.getVisibility() == View.VISIBLE;
    }
}
