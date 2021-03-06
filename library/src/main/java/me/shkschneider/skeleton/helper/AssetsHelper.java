package me.shkschneider.skeleton.helper;

import android.content.res.AssetManager;
import android.support.annotation.NonNull;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.shkschneider.skeleton.data.InternalDataHelper;

public class AssetsHelper {

    public static AssetManager assetManager() {
        return ApplicationHelper.context().getAssets();
    }

    public static List<String> list() {
        final List<String> list = new ArrayList<String>();
        try {
            Collections.addAll(list, assetManager().list(""));
            return list;
        }
        catch (final Exception e) {
            LogHelper.wtf(e);
            return null;
        }
    }

    public static InputStream open(@NonNull final String name) {
        final AssetManager assetManager = assetManager();
        try {
            return assetManager.open(name);
        }
        catch (final Exception e) {
            LogHelper.wtf(e);
            return null;
        }
    }

    public static boolean dump() {
        int errors = 0;
        for (final String asset : list()) {
            try {
                final InputStream inputStream = open(asset);
                final OutputStream outputStream = InternalDataHelper.openOutput(asset);
                byte[] buffer = new byte[1024];
                int read;
                while ((read = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, read);
                }
                inputStream.close();
                outputStream.flush();
                outputStream.close();
            }
            catch (Exception e) {
                e.printStackTrace();
                errors++;
            }
        }
        return (errors == 0);
    }

}
