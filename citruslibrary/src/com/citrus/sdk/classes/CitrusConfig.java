/*
 *
 *    Copyright 2014 Citrus Payment Solutions Pvt. Ltd.
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * /
 */

package com.citrus.sdk.classes;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.citrus.sdk.Constants;

/**
 * Created by salil on 18/5/15.
 */
public class CitrusConfig implements Parcelable {
    private boolean usePGHealth = false;
    private boolean autoReadOTP = false;

    /**
     * Primary color for the app, in the form of #123456.
     * This is the color of the status bar when the is opened.
     * This will be used only on android versions lollipop and above.
     */
    private String colorPrimaryDark = Constants.colorPrimaryDark;
    /**
     * Main color code for the app in the form #123456
     */
    private String colorPrimary = Constants.colorPrimary;
    /**
     * Primary text color. #123456
     */
    private String textColorPrimary = Constants.textColor;
    /**
     * Accent color for the app, will be used to display common actions.
     */
    private String accentColor = Constants.accentColor;

//    public void setUsePGHealth(boolean usePGHealth) {
//        this.usePGHealth = usePGHealth;
//    }
//
//    public void setAutoReadOTP(boolean autoReadOTP) {
//        this.autoReadOTP = autoReadOTP;
//    }

    public void setColorPrimaryDark(String colorPrimaryDark) {
        this.colorPrimaryDark = colorPrimaryDark;
    }

    public void setColorPrimary(String colorPrimary) {
        this.colorPrimary = colorPrimary;
    }

    public void setTextColorPrimary(String textColorPrimary) {
        this.textColorPrimary = textColorPrimary;
    }

    public void setAccentColor(String accentColor) {
        this.accentColor = accentColor;
    }

    public int getColorPrimaryDark() {
        return Color.parseColor(colorPrimaryDark);
    }

    public int getColorPrimary() {
        return Color.parseColor(colorPrimary);
    }

    public int getTextColorPrimary() {
        return Color.parseColor(textColorPrimary);
    }

    public int getAccentColor() {
        return Color.parseColor(accentColor);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(usePGHealth ? (byte) 1 : (byte) 0);
        dest.writeByte(autoReadOTP ? (byte) 1 : (byte) 0);
        dest.writeString(this.colorPrimaryDark);
        dest.writeString(this.colorPrimary);
        dest.writeString(this.textColorPrimary);
        dest.writeString(this.accentColor);
    }

    public CitrusConfig() {
    }

    private CitrusConfig(Parcel in) {
        this.usePGHealth = in.readByte() != 0;
        this.autoReadOTP = in.readByte() != 0;
        this.colorPrimaryDark = in.readString();
        this.colorPrimary = in.readString();
        this.textColorPrimary = in.readString();
        this.accentColor = in.readString();
    }

    public static final Parcelable.Creator<CitrusConfig> CREATOR = new Parcelable.Creator<CitrusConfig>() {
        public CitrusConfig createFromParcel(Parcel source) {
            return new CitrusConfig(source);
        }

        public CitrusConfig[] newArray(int size) {
            return new CitrusConfig[size];
        }
    };
}
