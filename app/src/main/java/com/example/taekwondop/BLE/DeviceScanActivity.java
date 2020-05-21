/*
 * Copyright (C) 2013 The Android Open Source Project
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

package com.example.taekwondop.BLE;

import android.Manifest;
import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.taekwondop.R;

import java.util.ArrayList;

/**
 * Activity for scanning and displaying available Bluetooth LE devices.
 * 这是用来扫描和显示可用低功耗蓝牙的设备
 */

/**
 * 两个重要的方法没有用到 getView onCreateOptionsMenu
 *
 * getView
 * 设置设备的名称和地址
 *
 * onCreateOptionsMenu
 * 如果没有在扫描的话，把stop设置为不可见，扫描设置为可见，refresh Todo设置为空
 * 如果在扫描的话，把stop设置为可见，scan设置为不可见，再在 refresh Todo的地方设置上转圈圈的小进度条
 */

// ListActivity可以理解为是ListView和Activity的结合。主要用来显示列表数据。显示数据需要设置适配器。
public class DeviceScanActivity extends ListActivity {

    // LeDeviceListAdapter继承自BaseAdapter，扩展BaseAdapter可以对各列表项进行最大限度的定制
    private LeDeviceListAdapter mLeDeviceListAdapter;
    //  BluetoothAdapter类简单点来说就是代表了本设备(手机、电脑等)的蓝牙适配器对象，通过它我们可以蓝牙设备进行基本开发了，主要有如下功能：
    // 1、开关蓝牙设备   2、扫描蓝牙设备       3、设置/获取蓝牙状态信息，例如：蓝牙状态值、蓝牙Name、蓝牙Mac地址等；
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    //handler是Android给我们提供用来更新UI的一套机制，也是一套消息处理机制，我们可以发消息，也可以通过它处理消息。
    //因为android在设计的时候就封装了一套消息创建、传递、处理。如果不遵循就不能更新UI信息，就会报出异常。
    //Android为什么要设计只能用handler机制更新UI呢？最根本的目的就是为了解决多线程并发的问题！
    private Handler mHandler;

    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds. 名称是个人定义
    private static final long SCAN_PERIOD = 10000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //传统
        super.onCreate(savedInstanceState);
        getActionBar().setTitle(R.string.title_devices);
        mHandler = new Handler();

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        //使用此检查来确定设备上是否支持BLE。然后可以有选择地禁用与BLE相关的特性
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        //初始化蓝牙适配器。对于API级别18或更高的API，可以通过蓝牙管理器获得对蓝牙适配器的引用。
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        //检查设备是否支持蓝牙
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        //andoird 6.0需要开启定位请求
        mayRequestLocation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //LayoutInflater是用来实例化整个布局文件，而 MenuInflater是用来实例化Menu目录下的Menu布局文件的。
        //传统意义上的菜单定义需要Override Activity的onCreateOptionsMenu，然后在里面调用Menu.add把Menu的一个个item加进来，比较复杂。
        // 而通过使用MenuInflater可以把Menu的构造直接放在Menu布局文件中，真正实现模型（Model）与视图（View）的分离，程序也看着清爽多了。
        getMenuInflater().inflate(R.menu.main, menu);
        //如果没有在扫描的话，把stop设置为不可见，扫描设置为可见，refresh Todo设置为空
        //如果在扫描的话，把stop设置为可见，scan设置为不可见，再在 refresh Todo的地方设置上转圈圈的小进度条
        if (!mScanning) {
            menu.findItem(R.id.menu_stop).setVisible(false);
            menu.findItem(R.id.menu_scan).setVisible(true);
//            menu.findItem(R.id.menu_refresh).setVisible(false);
            menu.findItem(R.id.menu_refresh).setActionView(null);
        } else {
            menu.findItem(R.id.menu_stop).setVisible(true);
            menu.findItem(R.id.menu_scan).setVisible(false);
            menu.findItem(R.id.menu_refresh).setActionView(
                    R.layout.actionbar_indeterminate_progress);
        }
        return true;
    }

    // 是否扫描设备 扫描函数是自己写的
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // scanLeDevice是自己写的一个函数
        switch (item.getItemId()) {
            case R.id.menu_scan:
                mLeDeviceListAdapter.clear();
                scanLeDevice(true);
                break;
            case R.id.menu_stop:
                scanLeDevice(false);
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        //奇奇怪怪 这里两个同样的if就是为了确定蓝牙权限是一定打开的吧
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        // Initializes list view adapter.
        // 初始化列表视图适配器  设置适配器  并让scanLeDevice打开
        mLeDeviceListAdapter = new LeDeviceListAdapter();
        setListAdapter(mLeDeviceListAdapter);
        scanLeDevice(true);
    }

    //用户不同意开启蓝牙的情况
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //如何因为某种原因中断了，扫描蓝牙的过程就要停止
    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
        mLeDeviceListAdapter.clear();
    }

    //当选择了扫描到的设备的时候，把设备的名字，地址都放在Intent里面，传递到BleSpp里面
    //如果蓝牙仍旧在扫描的话，就让扫描工作停止
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        final BluetoothDevice device = mLeDeviceListAdapter.getDevice(position);
        if (device == null) return;
        final Intent intent = new Intent(this, BleSppActivity.class);
        intent.putExtra(BleSppActivity.EXTRAS_DEVICE_NAME, device.getName());
        intent.putExtra(BleSppActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());
        if (mScanning) {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            mScanning = false;
        }
        startActivity(intent);
    }

    // 调用scanLeDevice 要给这个函数传递一个boolean
    private void scanLeDevice(final boolean enable) {
        //
        if (enable) {
            // Stops scanning after a pre-defined scan period.  在预先定义的扫描时间过去停止扫描
            //Causes the Runnable r to be added to the message queue, to be run after the specified amount of time elapses.
            //停止扫描的时间是 SCAN_PERIOD ，是在上面预先定义过了，10000ms,10S
            mHandler.postDelayed(new Runnable() {
                //整个大括号里都是new的Runnable对象
                @Override
                public void run() {
                    mScanning = false;
                    //停止扫描
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    //Declare that the options menu has changed, so should be recreated.
                    invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        invalidateOptionsMenu();
    }



    // Adapter for holding devices found through scanning.
    // 用于保存通过扫描找到的设备的适配器
    private class LeDeviceListAdapter extends BaseAdapter {
        // ArrayList new一个列表呀
        // LayoutInflater Instantiates a layout XML file into its corresponding  objects. It is never used directly.
        private ArrayList<BluetoothDevice> mLeDevices;
        private LayoutInflater mInflator;

        // 构造函数
        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<BluetoothDevice>();
            mInflator = DeviceScanActivity.this.getLayoutInflater();
        }

        // 如果有mLeDevices不包含的device,那就添加进去
        public void addDevice(BluetoothDevice device) {
            if(!mLeDevices.contains(device)) {
                mLeDevices.add(device);
            }
        }

        // 通过为止得到设备的信息
        public BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        // 清空设备列表信息
        public void clear() {
            mLeDevices.clear();
        }

        // 得到设备列表
        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        // 得到设备信息
        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        // 得到设备的ID
        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            // General ListView optimization code.
            // view是空的，那么久填充进度东西
            if (view == null) {
                view = mInflator.inflate(R.layout.listitem_device, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            BluetoothDevice device = mLeDevices.get(i);
            final String deviceName = device.getName();
            if (deviceName != null && deviceName.length() > 0)
                viewHolder.deviceName.setText(deviceName);
            else
                viewHolder.deviceName.setText(R.string.unknown_device);
            viewHolder.deviceAddress.setText(device.getAddress());

            return view;
        }
    }

    // Device scan callback.
    // 用于传递LE扫描结果的回调接口
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mLeDeviceListAdapter.addDevice(device);
                            mLeDeviceListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            };

    // ViewHolder包含两个TextView deviceName deviceAddress
    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
    }

    //需要开启位置权限
    private void mayRequestLocation() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                //判断是否需要 向用户解释，为什么要申请该权限
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION))
                    Toast.makeText(this,R.string.ble_need_location, Toast.LENGTH_LONG).show();

                ActivityCompat.requestPermissions(this ,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
                return;
            }else{

            }
        } else {

        }
    }
}