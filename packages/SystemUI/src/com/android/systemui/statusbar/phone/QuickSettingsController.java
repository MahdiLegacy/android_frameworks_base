/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.systemui.statusbar.phone;

import static com.android.internal.util.mahdi.QSConstants.TILES_DEFAULT;
import static com.android.internal.util.mahdi.QSConstants.TILE_AIRPLANE;
import static com.android.internal.util.mahdi.QSConstants.TILE_AUTOROTATE;
import static com.android.internal.util.mahdi.QSConstants.TILE_BATTERY;
import static com.android.internal.util.mahdi.QSConstants.TILE_BATTERYSAVER;
import static com.android.internal.util.mahdi.QSConstants.TILE_BLUETOOTH;
import static com.android.internal.util.mahdi.QSConstants.TILE_BRIGHTNESS;
import static com.android.internal.util.mahdi.QSConstants.TILE_CAMERA;
import static com.android.internal.util.mahdi.QSConstants.TILE_COMPASS;
import static com.android.internal.util.mahdi.QSConstants.TILE_CONTACT;
import static com.android.internal.util.mahdi.QSConstants.TILE_CUSTOM;
import static com.android.internal.util.mahdi.QSConstants.TILE_CUSTOM_KEY;
import static com.android.internal.util.mahdi.QSConstants.TILE_DELIMITER;
import static com.android.internal.util.mahdi.QSConstants.TILE_HEADS_UP;
import static com.android.internal.util.mahdi.QSConstants.TILE_IMMERSIVEMODE;
import static com.android.internal.util.mahdi.QSConstants.TILE_LOCATION;
import static com.android.internal.util.mahdi.QSConstants.TILE_LOCKSCREEN;
import static com.android.internal.util.mahdi.QSConstants.TILE_LTE;
import static com.android.internal.util.mahdi.QSConstants.TILE_MOBILEDATA;
import static com.android.internal.util.mahdi.QSConstants.TILE_MUSIC;
import static com.android.internal.util.mahdi.QSConstants.TILE_NAVBAR;
import static com.android.internal.util.mahdi.QSConstants.TILE_NETWORKADB;
import static com.android.internal.util.mahdi.QSConstants.TILE_NETWORKMODE;
import static com.android.internal.util.mahdi.QSConstants.TILE_NETWORKTRAFFIC;
import static com.android.internal.util.mahdi.QSConstants.TILE_NFC;
import static com.android.internal.util.mahdi.QSConstants.TILE_ONTHEGO;
import static com.android.internal.util.mahdi.QSConstants.TILE_PERFORMANCE_PROFILE;
import static com.android.internal.util.mahdi.QSConstants.TILE_PROFILE;
import static com.android.internal.util.mahdi.QSConstants.TILE_QUICKRECORD;
import static com.android.internal.util.mahdi.QSConstants.TILE_QUIETHOURS;
import static com.android.internal.util.mahdi.QSConstants.TILE_RINGER;
import static com.android.internal.util.mahdi.QSConstants.TILE_SCREENTIMEOUT;
import static com.android.internal.util.mahdi.QSConstants.TILE_SETTINGS;
import static com.android.internal.util.mahdi.QSConstants.TILE_SHAKE;
import static com.android.internal.util.mahdi.QSConstants.TILE_SLEEP;
import static com.android.internal.util.mahdi.QSConstants.TILE_SYNC;
import static com.android.internal.util.mahdi.QSConstants.TILE_THEME;
import static com.android.internal.util.mahdi.QSConstants.TILE_TORCH;
import static com.android.internal.util.mahdi.QSConstants.TILE_USER;
import static com.android.internal.util.mahdi.QSConstants.TILE_VOLUME;
import static com.android.internal.util.mahdi.QSConstants.TILE_WIFI;
import static com.android.internal.util.mahdi.QSConstants.TILE_WIFIAP;
import static com.android.internal.util.mahdi.QSConstants.TILE_WIMAX;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;

import com.android.internal.util.mahdi.QSUtils;
import com.android.systemui.quicksettings.AirplaneModeTile;
import com.android.systemui.quicksettings.AlarmTile;
import com.android.systemui.quicksettings.AutoRotateTile;
import com.android.systemui.quicksettings.BatteryTile;
import com.android.systemui.quicksettings.BatterySaverTile;
import com.android.systemui.quicksettings.BluetoothTile;
import com.android.systemui.quicksettings.BrightnessTile;
import com.android.systemui.quicksettings.BugReportTile;
import com.android.systemui.quicksettings.CameraTile;
import com.android.systemui.quicksettings.CompassTile;
import com.android.systemui.quicksettings.ContactTile;
import com.android.systemui.quicksettings.CustomTile;
import com.android.systemui.quicksettings.EqualizerTile;
import com.android.systemui.quicksettings.HeadsUpTile;
import com.android.systemui.quicksettings.ImmersiveModeTile;
import com.android.systemui.quicksettings.InputMethodTile;
import com.android.systemui.quicksettings.LocationTile;
import com.android.systemui.quicksettings.LteTile;
import com.android.systemui.quicksettings.MobileNetworkTile;
import com.android.systemui.quicksettings.MobileNetworkTypeTile;
import com.android.systemui.quicksettings.MusicTile;
import com.android.systemui.quicksettings.NavBarTile;
import com.android.systemui.quicksettings.NetworkTrafficTile;
import com.android.systemui.quicksettings.NetworkAdbTile;
import com.android.systemui.quicksettings.NfcTile;
import com.android.systemui.quicksettings.OnTheGoTile;
import com.android.systemui.quicksettings.PerformanceProfileTile;
import com.android.systemui.quicksettings.PreferencesTile;
import com.android.systemui.quicksettings.ProfileTile;
import com.android.systemui.quicksettings.QuickRecordTile;
import com.android.systemui.quicksettings.QuickSettingsTile;
import com.android.systemui.quicksettings.QuietHoursTile;
import com.android.systemui.quicksettings.RingerModeTile;
import com.android.systemui.quicksettings.ScreenTimeoutTile;
import com.android.systemui.quicksettings.SleepScreenTile;
import com.android.systemui.quicksettings.ShakeEventTile;
import com.android.systemui.quicksettings.SyncTile;
import com.android.systemui.quicksettings.ThemeTile;
import com.android.systemui.quicksettings.ToggleLockscreenTile;
import com.android.systemui.quicksettings.TorchTile;
import com.android.systemui.quicksettings.UsbTetherTile;
import com.android.systemui.quicksettings.UserTile;
import com.android.systemui.quicksettings.VolumeTile;
import com.android.systemui.quicksettings.RemoteDisplayTile;
import com.android.systemui.quicksettings.WiFiTile;
import com.android.systemui.quicksettings.WifiAPTile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class QuickSettingsController {
    private static final String TAG = "QuickSettingsController";

    // Stores the broadcast receivers and content observers
    // quick tiles register for.
    public HashMap<String, ArrayList<QuickSettingsTile>> mReceiverMap
        = new HashMap<String, ArrayList<QuickSettingsTile>>();
    public HashMap<Uri, ArrayList<QuickSettingsTile>> mObserverMap
        = new HashMap<Uri, ArrayList<QuickSettingsTile>>();

    // Uris that need to be monitored for updating tile status
    private HashSet<Uri> mTileStatusUris = new HashSet<Uri>();

    private final Context mContext;
    private ArrayList<QuickSettingsTile> mQuickSettingsTiles;
    public PanelBar mBar;
    private final QuickSettingsContainerView mContainerView;
    private final Handler mHandler;
    private BroadcastReceiver mReceiver;
    private ContentObserver mObserver;
    public PhoneStatusBar mStatusBarService;
    private final String mSettingsKey;
    private final boolean mRibbonMode;

    private InputMethodTile mIMETile;

    private static final int MSG_UPDATE_TILES = 1000;

    public QuickSettingsController(Context context, QuickSettingsContainerView container,
            PhoneStatusBar statusBarService, String settingsKey, boolean ribbonMode) {
        mContext = context;
        mContainerView = container;
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case MSG_UPDATE_TILES:
                        setupQuickSettings();
                        break;
                }
            }
        };
        mStatusBarService = statusBarService;
        mQuickSettingsTiles = new ArrayList<QuickSettingsTile>();
        mSettingsKey = settingsKey;
        mRibbonMode = ribbonMode;
    }

    public boolean isRibbonMode() {
        return mRibbonMode;
    }

    void loadTiles() {
        // Reset reference tiles
        mIMETile = null;

        // Filter items not compatible with device
        boolean cameraSupported = QSUtils.deviceSupportsCamera();
        boolean bluetoothSupported = QSUtils.deviceSupportsBluetooth();
        boolean lteSupported = QSUtils.deviceSupportsLte(mContext);
        boolean mobileDataSupported = QSUtils.deviceSupportsMobileData(mContext);        
        boolean gpsSupported = QSUtils.deviceSupportsGps(mContext);
        boolean torchSupported = QSUtils.deviceSupportsTorch(mContext);

        if (!bluetoothSupported) {
            TILES_DEFAULT.remove(TILE_BLUETOOTH);
        }

        if (!mobileDataSupported) {
            TILES_DEFAULT.remove(TILE_WIFIAP);
            TILES_DEFAULT.remove(TILE_MOBILEDATA);
            TILES_DEFAULT.remove(TILE_NETWORKMODE);
        }

        if (!lteSupported) {
            TILES_DEFAULT.remove(TILE_LTE);
        }

        if (!gpsSupported) {
            TILES_DEFAULT.remove(TILE_LOCATION);
        }

        if (!torchSupported) {
            TILES_DEFAULT.remove(TILE_TORCH);
        }

        // Read the stored list of tiles
        ContentResolver resolver = mContext.getContentResolver();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        String tiles = Settings.System.getStringForUser(resolver,
                mSettingsKey, UserHandle.USER_CURRENT);
        if (tiles == null) {
            Log.i(TAG, "Default tiles being loaded");
            tiles = TextUtils.join(TILE_DELIMITER, TILES_DEFAULT);
        }

        Log.i(TAG, "Tiles list: " + tiles);

        // Split out the tile names and add to the list
        for (String tile : tiles.split("\\|")) {
            QuickSettingsTile qs = null;
            if (tile.equals(TILE_USER)) {
                qs = new UserTile(mContext, this, mHandler);
            } else if (tile.equals(TILE_BATTERY)) {
                qs = new BatteryTile(mContext, this, mStatusBarService.mBatteryController);
            } else if (tile.equals(TILE_SETTINGS)) {
                qs = new PreferencesTile(mContext, this);
            } else if (tile.equals(TILE_WIFI)) {
                qs = new WiFiTile(mContext, this, mStatusBarService.mNetworkController);
            } else if (tile.equals(TILE_LOCATION)) {
                qs = new LocationTile(mContext, this);
            } else if (tile.equals(TILE_BLUETOOTH) && bluetoothSupported) {
                qs = new BluetoothTile(mContext, this, mStatusBarService.mBluetoothController);
            } else if (tile.equals(TILE_BRIGHTNESS)) {
                qs = new BrightnessTile(mContext, this);
            } else if (tile.equals(TILE_CAMERA) && cameraSupported) {
                qs = new CameraTile(mContext, this, mHandler);
            } else if (tile.contains(TILE_CONTACT)) {
                qs = new ContactTile(mContext, this, findCustomKey(tile));
            } else if (tile.contains(TILE_CUSTOM)) {
                qs = new CustomTile(mContext, this, findCustomKey(tile));
            } else if (tile.equals(TILE_RINGER)) {
                qs = new RingerModeTile(mContext, this);
            } else if (tile.equals(TILE_SYNC)) {
                qs = new SyncTile(mContext, this);
            } else if (tile.equals(TILE_WIFIAP) && mobileDataSupported) {
                qs = new WifiAPTile(mContext, this);
            } else if (tile.equals(TILE_SCREENTIMEOUT)) {
                qs = new ScreenTimeoutTile(mContext, this);
            } else if (tile.equals(TILE_MOBILEDATA) && mobileDataSupported) {
                qs = new MobileNetworkTile(mContext, this, mStatusBarService.mNetworkController);
            } else if (tile.equals(TILE_MUSIC)) {
                qs = new MusicTile(mContext, this);
            } else if (tile.equals(TILE_LOCKSCREEN)) {
                qs = new ToggleLockscreenTile(mContext, this);
            } else if (tile.equals(TILE_LTE)) {
                qs = new LteTile(mContext, this);
            } else if (tile.equals(TILE_NETWORKMODE) && mobileDataSupported) {
                qs = new MobileNetworkTypeTile(mContext, this, mStatusBarService.mNetworkController);
            } else if (tile.equals(TILE_AUTOROTATE)) {
                qs = new AutoRotateTile(mContext, this, mHandler);
            } else if (tile.equals(TILE_AIRPLANE)) {
                qs = new AirplaneModeTile(mContext, this, mStatusBarService.mNetworkController);
            } else if (tile.equals(TILE_TORCH)) {
                qs = new TorchTile(mContext, this, mHandler);
            } else if (tile.equals(TILE_SLEEP)) {
                qs = new SleepScreenTile(mContext, this);
            } else if (tile.equals(TILE_PROFILE)) {
                mTileStatusUris.add(Settings.System.getUriFor(Settings.System.SYSTEM_PROFILES_ENABLED));
                if (QSUtils.systemProfilesEnabled(resolver)) {
                    qs = new ProfileTile(mContext, this);
                }
            } else if (tile.equals(TILE_PERFORMANCE_PROFILE)) {
                if (QSUtils.deviceSupportsPerformanceProfiles(mContext)) {
                    qs = new PerformanceProfileTile(mContext, this);
                }
            } else if (tile.equals(TILE_NFC)) {
                // User cannot add the NFC tile if the device does not support it
                // No need to check again here
                qs = new NfcTile(mContext, this);
            } else if (tile.equals(TILE_QUICKRECORD)) {
                qs = new QuickRecordTile(mContext, this);           
            } else if (tile.equals(TILE_QUIETHOURS)) {
                qs = new QuietHoursTile(mContext, this);
            } else if (tile.equals(TILE_VOLUME)) {
                qs = new VolumeTile(mContext, this, mHandler);
            } else if (tile.equals(TILE_NAVBAR)) {
                    qs = new NavBarTile(mContext, this, mHandler);
            } else if (tile.equals(TILE_NETWORKTRAFFIC)) {
                qs = new NetworkTrafficTile(mContext, this, mHandler);
            } else if (tile.equals(TILE_THEME)) {
                qs = new ThemeTile(mContext, this);
            } else if (tile.equals(TILE_NETWORKADB)) {
                mTileStatusUris.add(Settings.Global.getUriFor(Settings.Global.ADB_ENABLED));
                if (QSUtils.adbEnabled(resolver)) {
                    qs = new NetworkAdbTile(mContext, this);
                }
            } else if (tile.equals(TILE_IMMERSIVEMODE)) {
                mTileStatusUris.add(Settings.System.getUriFor(Settings.System.GLOBAL_IMMERSIVE_MODE_STYLE));
                if (QSUtils.globalImmersiveModeEnabled(resolver)) {
                    qs = new ImmersiveModeTile(mContext, this, mHandler);
                }
            } else if (tile.equals(TILE_ONTHEGO)) {
                qs = new OnTheGoTile(mContext, this);
            } else if (tile.equals(TILE_BATTERYSAVER)) {
                qs = new BatterySaverTile(mContext, this);
            } else if (tile.equals(TILE_SHAKE)) {
                qs = new ShakeEventTile(mContext, this);
            } else if (tile.equals(TILE_COMPASS)) {
                qs = new CompassTile(mContext, this);
            } else if (tile.equals(TILE_HEADS_UP)) {
                qs = new HeadsUpTile(mContext, this);
            }

            if (qs != null) {
                qs.setupQuickSettingsTile(inflater, mContainerView);
                mQuickSettingsTiles.add(qs);
            }
        }

        if (mRibbonMode) {
            return;
        }

        // Load the dynamic tiles
        // These toggles must be the last ones added to the view, as they will show
        // only when they are needed
        if (Settings.System.getIntForUser(resolver,
                    Settings.System.QS_DYNAMIC_ALARM, 1, UserHandle.USER_CURRENT) == 1) {
            QuickSettingsTile qs = new AlarmTile(mContext, this, mHandler);
            qs.setupQuickSettingsTile(inflater, mContainerView);
            mQuickSettingsTiles.add(qs);
        }
        if (Settings.System.getIntForUser(resolver,
                    Settings.System.QS_DYNAMIC_BUGREPORT, 1, UserHandle.USER_CURRENT) == 1) {
            QuickSettingsTile qs = new BugReportTile(mContext, this, mHandler);
            qs.setupQuickSettingsTile(inflater, mContainerView);
            mQuickSettingsTiles.add(qs);
        }
        if (Settings.System.getIntForUser(resolver,
                    Settings.System.QS_DYNAMIC_WIFI, 1, UserHandle.USER_CURRENT) == 1) {
            QuickSettingsTile qs = new RemoteDisplayTile(mContext, this);
            qs.setupQuickSettingsTile(inflater, mContainerView);
            mQuickSettingsTiles.add(qs);
        }
        if (QSUtils.deviceSupportsImeSwitcher(mContext) && Settings.System.getIntForUser(resolver,
                    Settings.System.QS_DYNAMIC_IME, 1, UserHandle.USER_CURRENT) == 1) {
            mIMETile = new InputMethodTile(mContext, this);
            mIMETile.setupQuickSettingsTile(inflater, mContainerView);
            mQuickSettingsTiles.add(mIMETile);
        }
        if (QSUtils.deviceSupportsUsbTether(mContext) && Settings.System.getIntForUser(resolver,
                    Settings.System.QS_DYNAMIC_USBTETHER, 1, UserHandle.USER_CURRENT) == 1) {
            QuickSettingsTile qs = new UsbTetherTile(mContext, this);
            qs.setupQuickSettingsTile(inflater, mContainerView);
            mQuickSettingsTiles.add(qs);
        }
        if (Settings.System.getIntForUser(resolver,
                Settings.System.QS_DYNAMIC_EQUALIZER, 1, UserHandle.USER_CURRENT) == 1) {
            QuickSettingsTile qs = new EqualizerTile(mContext, this);
            qs.setupQuickSettingsTile(inflater, mContainerView);
            mQuickSettingsTiles.add(qs);
        }
    }

    private String findCustomKey (String tile) {
        String[] split = tile.split(TILE_CUSTOM_KEY);
        return split[1];
    }

    public void shutdown() {
        if (mObserver != null) {
            mContext.getContentResolver().unregisterContentObserver(mObserver);
        }
        if (mReceiver != null) {
            mContext.unregisterReceiver(mReceiver);
        }
        for (QuickSettingsTile qs : mQuickSettingsTiles) {
            qs.onDestroy();
        }
        mQuickSettingsTiles.clear();
        mContainerView.removeAllViews();
    }

    protected void setupQuickSettings() {
        shutdown();
        mReceiver = new QSBroadcastReceiver();
        mReceiverMap.clear();
        mObserver = new QuickSettingsObserver(mHandler);
        mObserverMap.clear();
        mTileStatusUris.clear();
        loadTiles();
        setupBroadcastReceiver();
        setupContentObserver();
        ContentResolver resolver = mContext.getContentResolver();
        boolean smallIcons = Settings.System.getIntForUser(resolver,
                Settings.System.QUICK_SETTINGS_SMALL_ICONS, 0, UserHandle.USER_CURRENT) == 1;
        if (mRibbonMode || smallIcons) {
            for (QuickSettingsTile t : mQuickSettingsTiles) {
                if (mRibbonMode) {
                    t.switchToRibbonMode();
                } else {
                    t.switchToSmallIcons();
                }
            }
        } if (Settings.System.getInt(resolver, //ribbon custom colors aren't supported ATM
                Settings.System.QUICK_TILES_CUSTOM_COLOR, 0) == 1) {
            int bgColor = Settings.System.getInt(resolver,
                    Settings.System.QUICK_TILES_BG_COLOR, -2);
            int presColor = Settings.System.getInt(resolver,
                    Settings.System.QUICK_TILES_BG_PRESSED_COLOR, -2);
            float bgAlpha = Settings.System.getFloat(resolver,
                Settings.System.QUICK_TILES_BG_ALPHA, 0.0f);
            for (QuickSettingsTile t : mQuickSettingsTiles) {
                t.setColors(bgColor, presColor, bgAlpha);
            }
        }
    }

    void setupContentObserver() {
        ContentResolver resolver = mContext.getContentResolver();
        for (Uri uri : mObserverMap.keySet()) {
            resolver.registerContentObserver(uri, false, mObserver);
        }
        for (Uri uri : mTileStatusUris) {
            resolver.registerContentObserver(uri, false, mObserver);
        }
    }

    private class QuickSettingsObserver extends ContentObserver {
        public QuickSettingsObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            if (mTileStatusUris.contains(uri)) {
                mHandler.removeMessages(MSG_UPDATE_TILES);
                mHandler.sendEmptyMessage(MSG_UPDATE_TILES);
            } else {
                ContentResolver resolver = mContext.getContentResolver();
                if (mObserverMap != null && mObserverMap.get(uri) != null) {
                    for (QuickSettingsTile tile : mObserverMap.get(uri)) {
                        tile.onChangeUri(resolver, uri);
                    }
                }
            }
        }
    }

    void setupBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        for (String action : mReceiverMap.keySet()) {
            filter.addAction(action);
        }
        mContext.registerReceiver(mReceiver, filter);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void registerInMap(Object item, QuickSettingsTile tile, HashMap map) {
        if (map.keySet().contains(item)) {
            ArrayList list = (ArrayList) map.get(item);
            if (!list.contains(tile)) {
                list.add(tile);
            }
        } else {
            ArrayList<QuickSettingsTile> list = new ArrayList<QuickSettingsTile>();
            list.add(tile);
            map.put(item, list);
        }
    }

    public void registerAction(String action, QuickSettingsTile tile) {
        registerInMap(action, tile, mReceiverMap);
    }

    public void registerObservedContent(Uri uri, QuickSettingsTile tile) {
        registerInMap(uri, tile, mObserverMap);
    }

    // Add to map and don't requre a race to post update methods
    // to do so.  Can register at any point in a tile's lifetime.
    public void addtoInstantObserverMap(Uri uri, QuickSettingsTile tile) {
        ContentResolver resolver = mContext.getContentResolver();
        resolver.registerContentObserver(uri, false, mObserver);
        registerInMap(uri, tile, mObserverMap);
    }

    private class QSBroadcastReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                for (QuickSettingsTile t : mReceiverMap.get(action)) {
                    t.onReceive(context, intent);
                }
            }
        }
    }

    void setBar(PanelBar bar) {
        mBar = bar;
    }

    public void setService(PhoneStatusBar phoneStatusBar) {
        mStatusBarService = phoneStatusBar;
    }

    public void setImeWindowStatus(boolean visible) {
        if (mIMETile != null) {
            mIMETile.toggleVisibility(visible);
        }
    }

    public void updateResources() {
        mContainerView.updateResources();
        for (QuickSettingsTile t : mQuickSettingsTiles) {
            t.updateResources();
        }
    }
}
