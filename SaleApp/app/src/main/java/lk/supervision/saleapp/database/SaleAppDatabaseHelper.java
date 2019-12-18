package lk.supervision.saleapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.supervision.saleapp.R;
import lk.supervision.saleapp.constant.AppEnvironmentValues;
import lk.supervision.saleapp.model.MCardDetails;
import lk.supervision.saleapp.model.MSettings;
import lk.supervision.saleapp.model.MSperson;
import lk.supervision.saleapp.model.MTransactionData;

/**
 * Created by kavish manjitha on 10/7/2017.
 */
public class SaleAppDatabaseHelper extends SQLiteOpenHelper {

    //database
    public static final String DATABASE_NAME = "saleapp.db";
    public static final int DATABASE_VERSION = 2;

    private static final String DB_TRANSACTION_DATA = "m_transaction_data";
    private static final String DB_CARD_DETAILS = "m_card_details";
    private static final String DB_SPERSON = "m_sperson";
    private static final String DB_SETTINGS = "m_settings";

    private Context context;

    public SaleAppDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        //onUpgrade(getReadableDatabase(), 1, 1);
    }

    @Override
    public synchronized SQLiteDatabase getReadableDatabase() {

        return super.getReadableDatabase();
    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {

        return super.getWritableDatabase();
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

    }

    @Override
    public synchronized void close() {
        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        InputStream inputStream = context.getResources().openRawResource(R.raw.database);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        StringBuilder builder = new StringBuilder();
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            String rawSql = builder.toString();

            String[] sqls = rawSql.split(";");
            for (String sql : sqls) {
                db.execSQL(sql);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DB_SPERSON);
        db.execSQL("DROP TABLE IF EXISTS " + DB_TRANSACTION_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + DB_CARD_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + DB_SETTINGS);

        //recreate database
        onCreate(db);
    }

    public int insertMSperson(MSperson mSperson) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues transactionValues = new ContentValues();
        transactionValues.put("sp_id", mSperson.getSpId());
        transactionValues.put("sp_name", mSperson.getSpName());
        transactionValues.put("password", mSperson.getPassword());
        transactionValues.put("last_serial", mSperson.getLastSerial());
        return (int) db.insert(DB_SPERSON, null, transactionValues);
    }

    public int updateMSettings(MSettings mSettings) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues transactionValues = new ContentValues();
        transactionValues.put("bluetooth_printer", mSettings.getBluetoothPrinter());
        transactionValues.put("bluetooth_printer_mac", mSettings.getBluetoothPrinterMac());
        transactionValues.put("clear_card_last_date_time", mSettings.getClearCardLastDateTime());
        transactionValues.put("befor_sync_last_date_time", mSettings.getBeforSyncLastDateTime());
        transactionValues.put("after_sync_last_date_time", mSettings.getAfterSyncLastDateTime());
        return (int) db.update(DB_SETTINGS, transactionValues, "index_no = ?", new String[]{mSettings.getIndexNo().toString()});
    }

    public MSettings viewMSettings() {
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + DB_SETTINGS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        MSettings mSettings = new MSettings();
        while (!cursor.isAfterLast()) {
            mSettings.setIndexNo(cursor.getInt(cursor.getColumnIndex("index_no")));
            mSettings.setBluetoothPrinter(cursor.getString(cursor.getColumnIndex("bluetooth_printer")));
            mSettings.setBluetoothPrinterMac(cursor.getString(cursor.getColumnIndex("bluetooth_printer_mac")));
            mSettings.setClearCardLastDateTime(cursor.getString(cursor.getColumnIndex("clear_card_last_date_time")));
            mSettings.setBeforSyncLastDateTime(cursor.getString(cursor.getColumnIndex("befor_sync_last_date_time")));
            mSettings.setAfterSyncLastDateTime(cursor.getString(cursor.getColumnIndex("after_sync_last_date_time")));
            cursor.moveToNext();
        }
        cursor.close();
        return mSettings;
    }

    public String getDeafaulBluetoothPrinter() {
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + DB_SETTINGS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String deafaulBluetoothPrinter = "";
        while (!cursor.isAfterLast()) {
            deafaulBluetoothPrinter = cursor.getString(cursor.getColumnIndex("bluetooth_printer_mac"));
            cursor.moveToNext();
        }
        cursor.close();
        return deafaulBluetoothPrinter;
    }

    public long mSettingsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long cnt = DatabaseUtils.queryNumEntries(db, DB_SETTINGS);
        db.close();
        return cnt;
    }

    public List<MSperson> viewMSperson() {
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + DB_SPERSON;
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        List<MSperson> mSpersonsList = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            MSperson mSperson = new MSperson();
            mSperson.setIndexNo(cursor.getInt(cursor.getColumnIndex("index_no")));
            mSperson.setSpId(cursor.getString(cursor.getColumnIndex("sp_id")));
            mSperson.setSpName(cursor.getString(cursor.getColumnIndex("sp_name")));
            mSperson.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            mSperson.setLastSerial(cursor.getInt(cursor.getColumnIndex("last_serial")));
            mSpersonsList.add(mSperson);
            cursor.moveToNext();
        }
        cursor.close();
        return mSpersonsList;
    }

    public MSperson findByNameAndPasssword(String name, String password) {
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + DB_SPERSON + " WHERE sp_name = '" + name + "' AND password = '" + password + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        List<MSperson> mSpersonsList = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            MSperson mSperson = new MSperson();
            mSperson.setIndexNo(cursor.getInt(cursor.getColumnIndex("index_no")));
            mSperson.setSpId(cursor.getString(cursor.getColumnIndex("sp_id")));
            mSperson.setSpName(cursor.getString(cursor.getColumnIndex("sp_name")));
            mSperson.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            mSperson.setLastSerial(cursor.getInt(cursor.getColumnIndex("last_serial")));
            mSpersonsList.add(mSperson);
            cursor.moveToNext();
        }
        cursor.close();

        if (mSpersonsList.isEmpty()) {
            return new MSperson();
        } else {
            return mSpersonsList.get(0);
        }
    }

    public Integer findByMSpersonLastSerialNo(String sId) {
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + DB_SPERSON + " WHERE sp_id = '" + sId + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        List<MSperson> mSpersonsList = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            MSperson mSperson = new MSperson();
            mSperson.setIndexNo(cursor.getInt(cursor.getColumnIndex("index_no")));
            mSperson.setLastSerial(cursor.getInt(cursor.getColumnIndex("last_serial")));
            mSpersonsList.add(mSperson);
            cursor.moveToNext();
        }
        cursor.close();

        if (mSpersonsList.isEmpty()) {
            return 1;
        } else {
            return mSpersonsList.get(0).getLastSerial();
        }
    }

    public int updateMSpersonLastSerialNo(int lastNo, String spId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues transactionValues = new ContentValues();
        transactionValues.put("last_serial", lastNo);
        return (int) db.update(DB_SPERSON, transactionValues, "sp_id = ?", new String[]{spId});
    }

    public int insertMCardDetails(MCardDetails mCardDetails) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues transactionValues = new ContentValues();
        transactionValues.put("con_no", mCardDetails.getConNo().toString());
        transactionValues.put("sp_id", mCardDetails.getSpId());
        transactionValues.put("sp_name", mCardDetails.getSpName());
        transactionValues.put("m_inv_no", mCardDetails.getmInvNo());
        transactionValues.put("reg_flag", mCardDetails.getRecFlag());
        transactionValues.put("con_date", mCardDetails.getConDate());
        transactionValues.put("cus_no", mCardDetails.getCusNo());
        transactionValues.put("id_no", mCardDetails.getIdNo());
        transactionValues.put("cus_name", mCardDetails.getCusName());
        transactionValues.put("add1", mCardDetails.getAdd1());
        transactionValues.put("add2", mCardDetails.getAdd2());
        transactionValues.put("city", mCardDetails.getCity());
        transactionValues.put("tp_no", mCardDetails.getTpNo());
        transactionValues.put("mobile_no", mCardDetails.getMobNo());
        transactionValues.put("total_value", String.valueOf(mCardDetails.getTotVal()));
        transactionValues.put("dpmt", String.valueOf(mCardDetails.getDpmt()));
        transactionValues.put("bal_value", String.valueOf(mCardDetails.getBalVal()));
        transactionValues.put("total_due_amount", String.valueOf(mCardDetails.getTotalDueAmt()));
        transactionValues.put("due_amount", String.valueOf(mCardDetails.getDueAmt()));
        return (int) db.insert(DB_CARD_DETAILS, null, transactionValues);
    }

    public List<MCardDetails> viewMCardDetails() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DB_CARD_DETAILS, null);
        cursor.moveToFirst();
        List<MCardDetails> mCardDetailsList = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            MCardDetails mCardDetails = new MCardDetails();
            mCardDetails.setIndexNo(cursor.getInt(cursor.getColumnIndex("index_no")));
            mCardDetails.setConNo(cursor.getString(cursor.getColumnIndex("con_no")));
            mCardDetails.setSpId(cursor.getString(cursor.getColumnIndex("sp_id")));
            mCardDetails.setSpName(cursor.getString(cursor.getColumnIndex("sp_name")));
            mCardDetails.setmInvNo(cursor.getString(cursor.getColumnIndex("m_inv_no")));
            mCardDetails.setRecFlag(cursor.getString(cursor.getColumnIndex("reg_flag")));
            mCardDetails.setConDate(cursor.getString(cursor.getColumnIndex("con_date")));
            mCardDetails.setCusNo(cursor.getString(cursor.getColumnIndex("cus_no")));
            mCardDetails.setIdNo(cursor.getString(cursor.getColumnIndex("id_no")));
            mCardDetails.setCusName(cursor.getString(cursor.getColumnIndex("cus_name")));
            mCardDetails.setAdd1(cursor.getString(cursor.getColumnIndex("add1")));
            mCardDetails.setAdd2(cursor.getString(cursor.getColumnIndex("add2")));
            mCardDetails.setCity(cursor.getString(cursor.getColumnIndex("city")));
            mCardDetails.setTpNo(cursor.getString(cursor.getColumnIndex("tp_no")));
            mCardDetails.setMobNo(cursor.getString(cursor.getColumnIndex("mobile_no")));
            mCardDetails.setTotVal(new BigDecimal(cursor.getString(cursor.getColumnIndex("total_value"))));
            mCardDetails.setDpmt(new BigDecimal(cursor.getString(cursor.getColumnIndex("dpmt"))));
            mCardDetails.setBalVal(new BigDecimal(cursor.getString(cursor.getColumnIndex("bal_value"))));
            mCardDetails.setTotalDueAmt(new BigDecimal(cursor.getString(cursor.getColumnIndex("total_due_amount"))));
            mCardDetails.setDueAmt(new BigDecimal(cursor.getString(cursor.getColumnIndex("due_amount"))));
            mCardDetailsList.add(mCardDetails);
            cursor.moveToNext();
        }
        cursor.close();
        return mCardDetailsList;
    }

    public List<MCardDetails> findMCardDetails(String paramiter) {
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + DB_CARD_DETAILS + " WHERE m_inv_no LIKE '%" + paramiter + "%' or id_no LIKE '%" + paramiter + "%' or mobile_no LIKE '%" + paramiter + "%' or add1 LIKE '%" + paramiter + "%'";
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        List<MCardDetails> mCardDetailsList = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            MCardDetails mCardDetails = new MCardDetails();
            mCardDetails.setIndexNo(cursor.getInt(cursor.getColumnIndex("index_no")));
            mCardDetails.setConNo(cursor.getString(cursor.getColumnIndex("con_no")));
            mCardDetails.setSpId(cursor.getString(cursor.getColumnIndex("sp_id")));
            mCardDetails.setSpName(cursor.getString(cursor.getColumnIndex("sp_name")));
            mCardDetails.setmInvNo(cursor.getString(cursor.getColumnIndex("m_inv_no")));
            mCardDetails.setRecFlag(cursor.getString(cursor.getColumnIndex("reg_flag")));
            mCardDetails.setConDate(cursor.getString(cursor.getColumnIndex("con_date")));
            mCardDetails.setCusNo(cursor.getString(cursor.getColumnIndex("cus_no")));
            mCardDetails.setIdNo(cursor.getString(cursor.getColumnIndex("id_no")));
            mCardDetails.setCusName(cursor.getString(cursor.getColumnIndex("cus_name")));
            mCardDetails.setAdd1(cursor.getString(cursor.getColumnIndex("add1")));
            mCardDetails.setAdd2(cursor.getString(cursor.getColumnIndex("add2")));
            mCardDetails.setCity(cursor.getString(cursor.getColumnIndex("city")));
            mCardDetails.setTpNo(cursor.getString(cursor.getColumnIndex("tp_no")));
            mCardDetails.setMobNo(cursor.getString(cursor.getColumnIndex("mobile_no")));
            mCardDetails.setTotVal(new BigDecimal(cursor.getString(cursor.getColumnIndex("total_value"))));
            mCardDetails.setDpmt(new BigDecimal(cursor.getString(cursor.getColumnIndex("dpmt"))));
            mCardDetails.setBalVal(new BigDecimal(cursor.getString(cursor.getColumnIndex("bal_value"))));
            mCardDetails.setTotalDueAmt(new BigDecimal(cursor.getString(cursor.getColumnIndex("total_due_amount"))));
            mCardDetails.setDueAmt(new BigDecimal(cursor.getString(cursor.getColumnIndex("due_amount"))));
            mCardDetailsList.add(mCardDetails);
            cursor.moveToNext();
        }
        cursor.close();
        return mCardDetailsList;
    }

    public int findMTransactionDetailsCount() {
        String countQuery = "SELECT  * FROM " + DB_TRANSACTION_DATA;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        db.close();
        return cnt;
    }

    public int insertMTransactionData(MTransactionData mTransactionData) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues transactionValues = new ContentValues();
        transactionValues.put("sp_id", mTransactionData.getSpId());
        transactionValues.put("m_inv_no", mTransactionData.getmInvNo());
        transactionValues.put("pmt_no", mTransactionData.getPmtNo());
        transactionValues.put("con_no", mTransactionData.getConNo());
        transactionValues.put("pmt_date", AppEnvironmentValues.getSystemDateTimeFormat());
        transactionValues.put("pmt_time", AppEnvironmentValues.getSystemDateTimeFormat());
        transactionValues.put("pmt_type", mTransactionData.getPmtType());
        transactionValues.put("pmt_amount", String.valueOf(mTransactionData.getPmtAmt()));

        if (mTransactionData.getPmtRemark() != null) {
            transactionValues.put("pmt_remark", mTransactionData.getPmtRemark());
        }

        if (mTransactionData.getNpmtDate() != null) {
            transactionValues.put("npm_date", mTransactionData.getNpmtDate());
        }

        transactionValues.put("phone_id", mTransactionData.getPhoneId());
        transactionValues.put("sys_update", mTransactionData.getSysUpdate());

        if (mTransactionData.getSysUpdateDate() != null) {
            transactionValues.put("sys_update_date", AppEnvironmentValues.getSystemDateTimeFormat());
        }
        return (int) db.insert(DB_TRANSACTION_DATA, null, transactionValues);
    }

    public int updateMCardDetails(String invNo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues transactionValues = new ContentValues();
        transactionValues.put("reg_flag", "1");
        return (int) db.update(DB_CARD_DETAILS, transactionValues, "m_inv_no = ?", new String[]{invNo});
    }

    public int updateMTransactionData(MTransactionData mTransactionData) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues transactionValues = new ContentValues();
        transactionValues.put("sys_update", mTransactionData.getSysUpdate());

        if (mTransactionData.getSysUpdateDate() != null) {
            transactionValues.put("sys_update_date", AppEnvironmentValues.getSystemDateTimeFormat());
        }
        return (int) db.update(DB_TRANSACTION_DATA, transactionValues, "index_no = ?", new String[]{mTransactionData.getIndexNo().toString()});
    }

    public List<MTransactionData> viewMTransactionData() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DB_TRANSACTION_DATA + " WHERE sys_update = 0", null);
        cursor.moveToFirst();
        List<MTransactionData> mTransactionDataList = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            MTransactionData mTransactionData = new MTransactionData();
            mTransactionData.setIndexNo(cursor.getInt(cursor.getColumnIndex("index_no")));
            mTransactionData.setSpId(cursor.getString(cursor.getColumnIndex("sp_id")));
            mTransactionData.setPmtNo(cursor.getString(cursor.getColumnIndex("pmt_no")));
            mTransactionData.setConNo(cursor.getString(cursor.getColumnIndex("con_no")));
            mTransactionData.setmInvNo(cursor.getString(cursor.getColumnIndex("m_inv_no")));
            mTransactionData.setPmtDate(AppEnvironmentValues.getSystemDateTimeParse(cursor.getString(cursor.getColumnIndex("pmt_date"))));
            mTransactionData.setPmtTime(AppEnvironmentValues.getSystemDateTimeParse(cursor.getString(cursor.getColumnIndex("pmt_time"))));
            mTransactionData.setPmtType(cursor.getString(cursor.getColumnIndex("pmt_type")));
            mTransactionData.setPmtAmt(new BigDecimal(cursor.getString(cursor.getColumnIndex("pmt_amount"))));
            mTransactionData.setPmtRemark(cursor.getString(cursor.getColumnIndex("pmt_remark")));
            mTransactionData.setNpmtDate(cursor.getString(cursor.getColumnIndex("npm_date")));
            mTransactionData.setPhoneId(cursor.getInt(cursor.getColumnIndex("phone_id")));
            mTransactionData.setSysUpdate(cursor.getInt(cursor.getColumnIndex("sys_update")));
            mTransactionData.setSysUpdateDate(AppEnvironmentValues.getSystemDateTimeParse(cursor.getString(cursor.getColumnIndex("sys_update_date"))));
            mTransactionDataList.add(mTransactionData);
            cursor.moveToNext();
        }
        cursor.close();
        return mTransactionDataList;
    }

    public List<MTransactionData> viewMTransactionDataByDate(String date) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TRANSACTION_DATA + " WHERE strftime('%Y-%m-%d', pmt_date) = strftime('%Y-%m-%d', '" + date + "')";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        List<MTransactionData> mTransactionDataList = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            MTransactionData mTransactionData = new MTransactionData();
            mTransactionData.setIndexNo(cursor.getInt(cursor.getColumnIndex("index_no")));
            mTransactionData.setSpId(cursor.getString(cursor.getColumnIndex("sp_id")));
            mTransactionData.setPmtNo(cursor.getString(cursor.getColumnIndex("pmt_no")));
            mTransactionData.setConNo(cursor.getString(cursor.getColumnIndex("con_no")));
            mTransactionData.setmInvNo(cursor.getString(cursor.getColumnIndex("m_inv_no")));
            mTransactionData.setPmtDate(AppEnvironmentValues.getSystemDateTimeParse(cursor.getString(cursor.getColumnIndex("pmt_date"))));
            mTransactionData.setPmtTime(AppEnvironmentValues.getSystemDateTimeParse(cursor.getString(cursor.getColumnIndex("pmt_time"))));
            mTransactionData.setPmtType(cursor.getString(cursor.getColumnIndex("pmt_type")));
            mTransactionData.setPmtAmt(new BigDecimal(cursor.getString(cursor.getColumnIndex("pmt_amount"))));
            mTransactionData.setPmtRemark(cursor.getString(cursor.getColumnIndex("pmt_remark")));
            mTransactionData.setNpmtDate(cursor.getString(cursor.getColumnIndex("npm_date")));
            mTransactionData.setPhoneId(cursor.getInt(cursor.getColumnIndex("phone_id")));
            mTransactionData.setSysUpdate(cursor.getInt(cursor.getColumnIndex("sys_update")));
            mTransactionData.setSysUpdateDate(AppEnvironmentValues.getSystemDateTimeParse(cursor.getString(cursor.getColumnIndex("sys_update_date"))));
            mTransactionDataList.add(mTransactionData);
            cursor.moveToNext();
        }
        cursor.close();
        return mTransactionDataList;
    }

    public boolean clearMCardDetails() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + DB_CARD_DETAILS);
        db.close();
        return true;
    }

    public boolean clearSperson() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + DB_SPERSON);
        db.close();
        return true;
    }
}
