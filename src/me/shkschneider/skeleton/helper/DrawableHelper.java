/**
 * Copyright 2013 ShkSchneider
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.shkschneider.skeleton.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ProgressBar;

@SuppressWarnings("unused")
public class DrawableHelper {

    public static Drawable drawableFromBitmap(final Context context, final Bitmap bitmap) {
        if (context != null) {
            return new BitmapDrawable(context.getResources(), bitmap);
        }
        else {
            LogHelper.w("Context was NULL");
        }
        return null;
    }

    public static Drawable indeterminateDrawable(final Context context) {
        if (context != null) {
            return new ProgressBar(context).getIndeterminateDrawable();
        }
        else {
            LogHelper.w("Context was NULL");
        }
        return null;
    }

}