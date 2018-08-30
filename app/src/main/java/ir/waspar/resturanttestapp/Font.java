package ir.waspar.resturanttestapp;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class Font extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
