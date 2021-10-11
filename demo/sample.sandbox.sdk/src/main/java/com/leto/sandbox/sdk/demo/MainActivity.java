package com.leto.sandbox.sdk.demo;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.leto.sandbox.sdk.LSBSdk;
import com.leto.sandbox.sdk.lsb.ILSBPayListener;
import com.leto.sandbox.sdk.lsb.ILSBSignInListener;
import com.leto.sandbox.sdk.lsb.LSBOrderInfo;
import com.leto.sandbox.sdk.lsb.LSBRoleInfo;
import com.leto.sandbox.sdk.lsb.LSBUserInfo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, ILSBSignInListener, ILSBPayListener {
    private TextView _statusLabel;
    private TextView _sandboxLabel;

    static Context _appCtx;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // request fullscreen
        super.onCreate(savedInstanceState);

        // save
        _appCtx = getApplicationContext();

        // set content view
        setContentView(R.layout.activity_main);

        // views
        _statusLabel = findViewById(R.id.status);
        _sandboxLabel = findViewById(R.id.sandbox);

        // set sandbox label
        if(LSBSdk.init(this)) {
            _sandboxLabel.setText("当前正处于沙盒环境, 渠道id: " + LSBSdk.getChannelId());
        } else {
            _sandboxLabel.setText("当前为独立运行环境");
        }

        // click
        findViewById(R.id.huo_sign_in).setOnClickListener(v -> {
            LSBSdk.startLSBSignIn();
        });
        findViewById(R.id.huo_pay).setOnClickListener(v -> {
            LSBOrderInfo info = new LSBOrderInfo();
            info.setCpOrderId("20161028111");
            info.setProductPrice(0.01f);
            info.setProductCount(1);
            info.setProductId("1");
            info.setProductName("元宝");
            info.setProductDesc("很好");
            info.setExchangeRate(1);
            info.setCurrencyName("金币");
            info.setExt("穿透");
            LSBSdk.startLSBPay(info);
        });

        // add wx sign in listener
        LSBSdk.addLSBSignInListener(this);
        LSBSdk.addLSBPayListener(this);

        initPermission();
    }

    private void initPermission() {
        String[] permissions = CheckPermissionUtils.checkPermission(this, new String[] {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        });
        if (permissions.length > 0) {
            ActivityCompat.requestPermissions(this, permissions, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        _appCtx = null;
    }

    @Override
    public void onLSBSignInSuccess(LSBUserInfo userInfo) {
        _statusLabel.setText("LSB登录成功, memId: " + userInfo.getMemId() + ", token: " + userInfo.getUserToken());

        // set huo role info
        LSBRoleInfo roleInfo = new LSBRoleInfo();
        roleInfo.setRoleLevelCreateTime("" + System.currentTimeMillis() / 1000);
        roleInfo.setRoleLevelModifyTime("" + System.currentTimeMillis() / 1000);
        roleInfo.setPartyName("");
        roleInfo.setRoleBalance(1.00f);
        roleInfo.setRoleId("Role_id");
        roleInfo.setRoleLevel(0);
        roleInfo.setRoleName("roleName");
        roleInfo.setRoleVip(0);
        roleInfo.setServerId("Server_id");
        roleInfo.setServerName("serverName");
        LSBSdk.setLSBRoleInfo(roleInfo);
    }

    @Override
    public void onLSBSignInCancelled() {
        _statusLabel.setText("LSB登录取消");
    }

    @Override
    public void onLSBSignInFailed(String errMsg) {
        _statusLabel.setText("LSB登录失败: " + errMsg);
    }

    @Override
    public void onLSBPaySuccess(String cpOrderID, String extrasParams) {
        _statusLabel.setText("LSB支付成功, cp order id: " + cpOrderID + ", extra: " + extrasParams);
    }

    @Override
    public void onLSBPayCancelled(String cpOrderID) {
        _statusLabel.setText("LSB支付取消, cp order id: " + cpOrderID);
    }

    @Override
    public void onLSBPayFailed(String cpOrderID, String errMsg) {
        _statusLabel.setText("LSB支付失败, cp order id: " + cpOrderID + ", errmsg: " + errMsg);
    }
}
