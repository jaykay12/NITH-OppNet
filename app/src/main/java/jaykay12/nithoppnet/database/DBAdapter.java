package jaykay12.nithoppnet.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import jaykay12.nithoppnet.model.DeviceDTO;

/**
 * Created by jaykay12 on 31/3/18.
 */

public class DBAdapter {

    private static DBAdapter singleInstance;
    private static Object lockObject = new Object();
    private Context context;

    private SQLiteDatabase db = null;

    private DBAdapter(Context context) {
        this.context = context;
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public static DBAdapter getInstance(Context context) {
        if (singleInstance == null) {
            synchronized (DBAdapter.class) {
                if (singleInstance == null) {
                    singleInstance = new DBAdapter(context);
                }
            }
        }
        return singleInstance;
    }

    public long addDevice(DeviceDTO device) {
        if (device == null || device.getIp() == null || device.getPort() == 0) {
            return -1;
        }
        ContentValues values = new ContentValues();
        values.put(DBHelper.COL_DEV_MODEL, device.getDeviceName());
        values.put(DBHelper.COL_DEV_IP, device.getIp());
        values.put(DBHelper.COL_DEV_PLAYER, device.getPlayerName());
        values.put(DBHelper.COL_DEV_PORT, device.getPort());
        values.put(DBHelper.COL_DEV_VERSION, device.getOsVersion());

        if (!deviceExists(device.getIp())) {
            long rowId = db.insert(DBHelper.TABLE_DEVICES, null, values);
            return rowId;
        }

        return -1;

    }

    public ArrayList<DeviceDTO> getDeviceList() {
        ArrayList<DeviceDTO> devices = null;

        Cursor cursor = db.query(DBHelper.TABLE_DEVICES, null, null, null, null, null,
                DBHelper.COL_DEV_PLAYER);

        if (cursor != null) {
            devices = new ArrayList<>();
        } else {
            return devices;
        }

        int modelIndex = cursor.getColumnIndex(DBHelper.COL_DEV_MODEL);
        int ipIndex = cursor.getColumnIndex(DBHelper.COL_DEV_IP);
        int playerNameIndex = cursor.getColumnIndex(DBHelper.COL_DEV_PLAYER);
        int portIndex = cursor.getColumnIndex(DBHelper.COL_DEV_PORT);
        int versionIndex = cursor.getColumnIndex(DBHelper.COL_DEV_VERSION);

        while (cursor.moveToNext()) {
            DeviceDTO device = new DeviceDTO();
            device.setDeviceName(cursor.getString(modelIndex));
            device.setIp(cursor.getString(ipIndex));
            device.setPlayerName(cursor.getString(playerNameIndex));
            device.setPort(cursor.getInt(portIndex));
            device.setOsVersion(cursor.getString(versionIndex));

            devices.add(device);
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return devices;
    }

    public DeviceDTO getDevice(String ip) {

        DeviceDTO device = null;

        Cursor cursor = db.query(DBHelper.TABLE_DEVICES, null, DBHelper.COL_DEV_IP + "=?",
                new String[]{ip}, null, null, DBHelper.COL_DEV_PLAYER);

        if (cursor != null) {
            device = new DeviceDTO();
        } else {
            return device;
        }

        int modelIndex = cursor.getColumnIndex(DBHelper.COL_DEV_MODEL);
        int ipIndex = cursor.getColumnIndex(DBHelper.COL_DEV_IP);
        int playerNameIndex = cursor.getColumnIndex(DBHelper.COL_DEV_PLAYER);
        int portIndex = cursor.getColumnIndex(DBHelper.COL_DEV_PORT);
        int versionIndex = cursor.getColumnIndex(DBHelper.COL_DEV_VERSION);

        if (cursor.moveToNext()) {
            device.setDeviceName(cursor.getString(modelIndex));
            device.setIp(cursor.getString(ipIndex));
            device.setPlayerName(cursor.getString(playerNameIndex));
            device.setPort(cursor.getInt(portIndex));
            device.setOsVersion(cursor.getString(versionIndex));
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return device;
    }

    public boolean removeDevice(String ip) {
        int rowsAffected = db.delete(DBHelper.TABLE_DEVICES, DBHelper.COL_DEV_IP + "=?"
                , new String[]{ip});
        return (rowsAffected > 0);
    }

    public boolean deviceExists(String ip) {
        Cursor cursor = db.query(DBHelper.TABLE_DEVICES, null, DBHelper.COL_DEV_IP + "=?", new
                String[]{ip}, null, null, null);

        return (cursor.getCount() > 0);
    }

    public int clearDatabase() {
        int rowsAffected = db.delete(DBHelper.TABLE_DEVICES, null, null);
        return rowsAffected;
    }
}
