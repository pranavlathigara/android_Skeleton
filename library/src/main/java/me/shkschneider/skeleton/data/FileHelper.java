package me.shkschneider.skeleton.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import me.shkschneider.skeleton.helper.ApplicationHelper;
import me.shkschneider.skeleton.helper.LogHelper;
import me.shkschneider.skeleton.helper.SystemProperties;

public class FileHelper {

    public static final String PREFIX_ASSETS = "file:///android_asset/";
    public static final String PREFIX_RES = "file:///android_res/";

    public static String join(@NonNull final String dirname, @NonNull final String basename) {
        return String.format("%s%s%s", dirname, SystemProperties.property(SystemProperties.SYSTEM_PROPERTY_PATH_SEPARATOR), basename);
    }

    public static File get(@NonNull final String path) {
        return new File(path);
    }

    public static InputStream openFile(@NonNull final File file) {
        try {
            return new FileInputStream(file);
        }
        catch (final Exception e) {
            LogHelper.wtf(e);
            return null;
        }
    }

    public static InputStream openRaw(final int rawId) {
        try {
            return ApplicationHelper.resources().openRawResource(rawId);
        }
        catch (final Exception e) {
            LogHelper.wtf(e);
            return null;
        }
    }

    public static InputStream openAsset(@NonNull final String assetName) {
        try {
            return ApplicationHelper.assetManager().open(assetName);
        }
        catch (final Exception e) {
            LogHelper.wtf(e);
            return null;
        }
    }

    public static String readString(@NonNull final InputStream inputStream) {
        // TRICK: The stream gets tokenized, \A meaning the beginning, \\A means the second beginning... so its end.
        return new Scanner(inputStream).useDelimiter("\\A").next();
    }

    public static String readString(@NonNull final File file) {
        try {
            return readString(new FileInputStream(file));
        }
        catch (final Exception e) {
            LogHelper.wtf(e);
            return null;
        }
    }

    public static Bitmap readBitmap(@NonNull final File file) {
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            return BitmapFactory.decodeStream(new FileInputStream(file), null, options);
        }
        catch (final Exception e) {
            LogHelper.wtf(e);
            return null;
        }
    }

    public static boolean writeString(@NonNull final OutputStream outputStream, @NonNull final String content) {
        try {
            outputStream.write(content.getBytes());
            outputStream.close();
            return true;
        }
        catch (final Exception e) {
            LogHelper.wtf(e);
            return false;
        }
    }

    public static boolean writeString(@NonNull final File file, @NonNull final String content) {
        try {
            return writeString(new FileOutputStream(file), content);
        }
        catch (final Exception e) {
            LogHelper.wtf(e);
            return false;
        }
    }

    public static boolean writeBitmap(@NonNull final File file, @NonNull final Bitmap bitmap) {
        try {
            final FileOutputStream fileOutputStream = new FileOutputStream(file);
            return bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
        }
        catch (final Exception e) {
            LogHelper.wtf(e);
            return false;
        }
    }

    public static List<String> list(@NonNull final File file) {
        if (! file.isDirectory()) {
            LogHelper.debug("File was not a directory");
            return null;
        }

        final List<String> list = new ArrayList<String>();

        final File[] files = file.listFiles();
        if (files == null) {
            LogHelper.debug("Files was NULL");
            return null;
        }

        for (final File f : files) {
            list.add(f.getAbsolutePath());
        }
        return list;
    }

    public static boolean remove(@NonNull final File file) {
        return file.delete();
    }

}
