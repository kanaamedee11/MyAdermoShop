package com.example.myadermoshop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;
import com.google.gson.GsonBuilder;
import com.itextpdf.styledxmlparser.css.CommonCssConstants;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DatabaseHelper extends SQLiteOpenHelper {

    // -------------------------------------------------------------------------
    // Column constants
    // -------------------------------------------------------------------------
    private static final String COLUMN_ACTION_DATE = "actionDate";
    public static final String COLUMN_ACTION_TAKEN = "actionTaken";
    public static final String COLUMN_ACTUAL_QUANTITY = "actualQuantity";
    public static final String COLUMN_ADMIN_FIRST_NAME = "adminFirstName";
    public static final String COLUMN_ADMIN_ID = "adminID";
    public static final String COLUMN_ADMIN_ID_FK = "adminID";
    public static final String COLUMN_ADMIN_LAST_NAME = "adminLastName";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_AMOUNT_IN_EXPENSES = "amountInExpenses";
    public static final String COLUMN_AMOUNT_IN_STOCK = "amountInStock";
    public static final String COLUMN_API_KEY = "apiKey";
    public static final String COLUMN_CART_DATE = "cartDate";
    public static final String COLUMN_CART_ID = "cartID";
    public static final String COLUMN_CART_ITEM_ID = "cartItemID";
    public static final String COLUMN_CART_ITEM_WITHOUT_INSTANCE_ID = "cartItemwithoutinstanceID";
    public static final String COLUMN_CLOSURE_DATE = "closureDate";
    public static final String COLUMN_CLOSURE_ID = "closureID";
    public static final String COLUMN_CLOSURE_STATUS = "closureStatus";
    public static final String COLUMN_CONTROLE_CASE_ID = "controleCaseID";
    public static final String COLUMN_CONTROLE_DATETIME = "controleDateTime";
    public static final String COLUMN_CONTROLE_ID = "controleID";
    public static final String COLUMN_DETECTED_BY_EMPLOYEE_ID = "detectedByEmployeeID";
    public static final String COLUMN_DETERIORATED_PRODUCT_WITHOUT_INSTANCE_ID = "deterioratedProductWithoutInstanceID";
    public static final String COLUMN_DETERIORATED_PRODUCT_WITH_INSTANCE_ID = "deterioratedProductWithInstanceID";
    public static final String COLUMN_DETERIORATION_DATE = "deteriorationDate";
    public static final String COLUMN_DISPENSE_DATE = "dispenseDate";
    public static final String COLUMN_DISPENSE_ID = "dispenseID";
    public static final String COLUMN_EMPLOYEE_ACCOUNT_ACTIVATION = "employeeAccountActivation";
    public static final String COLUMN_EMPLOYEE_BIRTHDAY = "employeeBirthday";
    public static final String COLUMN_EMPLOYEE_CNI = "employeeCNI";
    public static final String COLUMN_EMPLOYEE_EMAIL = "employeeEmail";
    public static final String COLUMN_EMPLOYEE_FIRST_NAME = "employeeFirstName";
    public static final String COLUMN_EMPLOYEE_ID = "employeeID";
    public static final String COLUMN_EMPLOYEE_ID_FK = "employeeID";
    public static final String COLUMN_EMPLOYEE_LAST_NAME = "employeeLastName";
    public static final String COLUMN_EMPLOYEE_PASSWORD = "employeePassword";
    public static final String COLUMN_EMPLOYEE_TEL = "employeeTel";
    private static final String COLUMN_EXPECTED_AMOUNT = "expectedAmount";
    public static final String COLUMN_EXPECTED_QUANTITY = "expectedQuantity";
    public static final String COLUMN_FACTURE_IMAGE_NAME = "factureImage_name";
    public static final String COLUMN_FACTURE_NUMBER = "factureNumber";
    public static final String COLUMN_FATHER_FULL_NAME = "fatherFullName";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ID_PDF_FILE = "id_file_case";
    public static final String COLUMN_INSTANCE_ID = "instanceID";
    public static final String COLUMN_INSTANCE_STATE = "instanceState";
    public static final String COLUMN_IS_ACTIVE = "isActive";
    public static final String COLUMN_IS_ACTIVE_TO_DECIMAL_QUANTITY = "isActiveToDecimalQuantity";
    public static final String COLUMN_IS_ACTIVE_TO_INSTANCES = "isActiveToInstances";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_MANUFACTURE_ADDRESS = "manufactureAddress";
    public static final String COLUMN_MOTHER_FULL_NAME = "motherFullName";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PAYMENT_ID = "paymentID";
    public static final String COLUMN_PAYMENT_METHOD = "paymentMethod";
    public static final String COLUMN_PAYMENT_TYPE_ID = "paymentTypeID";
    public static final String COLUMN_PICTURE_NAME = "picture_name";
    public static final String COLUMN_PICTURE_URL = "picture_url";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_PRICE_CASE_ID = "pricecaseID";
    public static final String COLUMN_PRICE_CASE_STATE = "pricecaseState";
    public static final String COLUMN_PRICE_STATE_DATE = "priceStateDate";
    public static final String COLUMN_PRODUCT_ADD_DATE = "product_add_date";
    public static final String COLUMN_PRODUCT_ID = "productID";
    public static final String COLUMN_PRODUCT_ID_FK = "productID";
    public static final String COLUMN_PRODUCT_MANUFACTURE = "productManufacture";
    public static final String COLUMN_PRODUCT_NAME = "productName";
    public static final String COLUMN_PRODUCT_PHOTO_NAME = "product_photo_name";
    public static final String COLUMN_PRODUCT_SEUIL_STOCK = "product_seuil_stock";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_QUANTITY_CART = "quantityCart";
    public static final String COLUMN_REASON = "reason";
    public static final String COLUMN_STATUS_DESCRIPTION = "statusDescription";
    public static final String COLUMN_STATUS_ID = "statusID";
    public static final String COLUMN_STATUS_LABEL = "statusLabel";
    public static final String COLUMN_STOCK_DATE_TIME = "stockDateTime";
    public static final String COLUMN_STOCK_EXP_DATE = "stockExpDate";
    public static final String COLUMN_STOCK_ID = "stockID";
    public static final String COLUMN_STOCK_MAN_DATE = "stockManDate";
    public static final String COLUMN_STOCK_QUANTITY = "stockQuantity";
    public static final String COLUMN_SUBMISSION_DATE = "submissionDate";
    public static final String COLUMN_SUB_ACCOUNT_ID = "subAccountID";
    public static final String COLUMN_SUB_SUB_ACCOUNT_ID = "subSubAccountID";
    public static final String COLUMN_SUPPLIER_CONTACT = "supplierContact";
    public static final String COLUMN_SUPPLIER_NAME = "supplierName";
    public static final String COLUMN_TOTAL_AMOUNT_USED = "totalAmountUsed";
    public static final String COLUMN_TOTAL_SALES = "totalSales";
    public static final String COLUMN_TOTAL_STOCKS_MADE = "totalStocksMade";
    public static final String COLUMN_TYPE_DISPENSE_ID = "typeDispenseID";
    public static final String COLUMN_TYPE_DISPENSE_ID_FK = "typeDispenseID";
    public static final String COLUMN_TYPE_DISPENSE_NAME = "typeDispenseName";
    public static final String COLUMN_TYPE_PRODUCT_ID = "typeProductID";
    public static final String COLUMN_TYPE_PRODUCT_NAME = "nameTypeProduct";
    public static final String COLUMN_UNITE_DESCRIPTION = "uniteDescription";
    public static final String COLUMN_UNITE_ID = "uniteID";
    public static final String COLUMN_UNITE_NAME = "uniteName";
    public static final String COLUMN_UNITE_SIGN = "uniteSign";
    public static final String COLUMN_UPLOAD_STATUS = "uploadStatus";
    private static final String COLUMN_VERSED_AMOUNT = "versedAmount";
    private static final String COLUMN_VERSEMENT_DATE_TIME = "versementDateTime";
    public static final String COLUMN_VERSEMENT_DEPOSIT = "versementDeposit";
    private static final String COLUMN_VERSEMENT_ID = "versementID";
    private static final String COLUMN_VERSEMENT_PICTURE_NAME = "versementPictureName";

    private static final String DATABASE_NAME = "myadermoshop.db";
    private static final int DATABASE_VERSION = 1;
    private static final SecureRandom random = new SecureRandom();
    private final Context context;

    public static final String TABLE_ADMIN = "tbl_admin";
    public static final String TABLE_CART = "tbl_cart";
    public static final String TABLE_CART_ITEMS_WITHOUT_INSTANCE = "tbl_cart_itemswithoutinstance";
    public static final String TABLE_CART_ITEMS_WITH_INSTANCE = "tbl_cart_itemswithinstance";
    public static final String TABLE_CLOSURE = "tbl_closure";
    public static final String TABLE_CONTROLE_CASE = "tbl_controle_case";
    public static final String TABLE_DETERIORATED_PRODUCT_WITHOUT_INSTANCE = "tbl_deteriorated_product_without_instance";
    public static final String TABLE_DETERIORATED_PRODUCT_WITH_INSTANCE = "tbl_deteriorated_product_with_instance";
    public static final String TABLE_DISPENSES = "tbl_dispenses";
    public static final String TABLE_EMPLOYEE = "tbl_employee";
    public static final String TABLE_MEASUREMENT_UNIT = "tbl_mesurement_unit";
    public static final String TABLE_OPERATION_STATUS = "tbl_operationstatus";
    public static final String TABLE_PAYMENT = "tbl_payment";
    public static final String TABLE_PDF_FILES = "tbl_pdf_files";
    public static final String TABLE_PHYSICAL_CONTROLE = "tbl_physical_controle";
    public static final String TABLE_PRODUCT = "tbl_product";
    public static final String TABLE_PRODUCT_INSTANCE = "tbl_product_instance";
    public static final String TABLE_PRODUCT_PRICE = "tbl_product_price";
    public static final String TABLE_PRODUCT_TYPE = "tbl_product_type";
    public static final String TABLE_STOCK = "tbl_stock";
    public static final String TABLE_TYPE_DISPENSES = "tbl_type_dispenses";
    public static final String TABLE_TYPE_PAYMENT = "tbl_typepayment";
    private static final String TABLE_VERSEMENT = "tbl_versement";

    // -------------------------------------------------------------------------
    // CREATE TABLE statements
    // -------------------------------------------------------------------------
    private static final String TABLE_CREATE_ADMIN =
            "CREATE TABLE tbl_admin (adminID INTEGER PRIMARY KEY, adminFirstName TEXT, adminLastName TEXT);";
    private static final String TABLE_CREATE_CART =
            "CREATE TABLE tbl_cart (cartID TEXT PRIMARY KEY, cartDate TEXT, employeeID TEXT);";
    private static final String TABLE_CREATE_CART_ITEMS_WITHOUT_INSTANCE =
            "CREATE TABLE tbl_cart_itemswithoutinstance (cartItemwithoutinstanceID TEXT PRIMARY KEY,"
                    + " quantityCart REAL, cartID TEXT, productID TEXT, pricecaseID INTEGER,"
                    + " FOREIGN KEY (cartID) REFERENCES tbl_cart(cartID),"
                    + " FOREIGN KEY (productID) REFERENCES tbl_product(productID),"
                    + " FOREIGN KEY (pricecaseID) REFERENCES tbl_product_price(pricecaseID));";
    private static final String TABLE_CREATE_CART_ITEMS_WITH_INSTANCE =
            "CREATE TABLE tbl_cart_itemswithinstance (cartItemID TEXT PRIMARY KEY, cartID TEXT,"
                    + " instanceID TEXT, pricecaseID INTEGER,"
                    + " FOREIGN KEY (cartID) REFERENCES tbl_cart(cartID),"
                    + " FOREIGN KEY (pricecaseID) REFERENCES tbl_product_price(pricecaseID));";
    private static final String TABLE_CREATE_CLOSURE =
            "CREATE TABLE tbl_closure (closureID TEXT PRIMARY KEY,"
                    + " closureDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                    + " totalSales DOUBLE DEFAULT NULL, amountInStock DOUBLE DEFAULT NULL,"
                    + " closureStatus INTEGER DEFAULT NULL, employeeID TEXT NOT NULL,"
                    + " totalStocksMade INTEGER DEFAULT NULL, amountInExpenses DOUBLE DEFAULT NULL,"
                    + " versementDeposit DOUBLE DEFAULT NULL,"
                    + " FOREIGN KEY (employeeID) REFERENCES tbl_employee(employeeID));";
    private static final String TABLE_CREATE_CONTROLE_CASE =
            "CREATE TABLE tbl_controle_case (controleCaseID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + " controleID INTEGER NOT NULL, productID VARCHAR(11) NOT NULL,"
                    + " expectedQuantity INTEGER NOT NULL, actualQuantity INTEGER NOT NULL,"
                    + " FOREIGN KEY (controleID) REFERENCES tbl_physical_controle(controleID),"
                    + " FOREIGN KEY (productID) REFERENCES tbl_product(productID));";
    private static final String TABLE_CREATE_DETERIORATED_PRODUCT_WITHOUT_INSTANCE =
            "CREATE TABLE IF NOT EXISTS tbl_deteriorated_product_without_instance"
                    + " (deterioratedProductWithoutInstanceID TEXT PRIMARY KEY,"
                    + " productID TEXT NOT NULL, deteriorationDate DATETIME NOT NULL,"
                    + " reason TEXT DEFAULT NULL, quantity INTEGER NOT NULL,"
                    + " detectedByEmployeeID TEXT NOT NULL, picture_name TEXT DEFAULT NULL,"
                    + " actionTaken BOOLEAN DEFAULT 0, actionDate DATETIME DEFAULT NULL,"
                    + " submissionDate DATETIME NOT NULL, uploadStatus INTEGER NOT NULL DEFAULT 0,"
                    + " FOREIGN KEY (productID) REFERENCES tbl_product(productID),"
                    + " FOREIGN KEY (detectedByEmployeeID) REFERENCES tbl_employee(employeeID));";
    private static final String TABLE_CREATE_DETERIORATED_PRODUCT_WITH_INSTANCE =
            "CREATE TABLE IF NOT EXISTS tbl_deteriorated_product_with_instance"
                    + " (deterioratedProductWithInstanceID TEXT PRIMARY KEY,"
                    + " instanceID TEXT NOT NULL, deteriorationDate DATETIME NOT NULL,"
                    + " reason TEXT DEFAULT NULL, quantity INTEGER NOT NULL,"
                    + " detectedByEmployeeID TEXT NOT NULL, picture_name TEXT DEFAULT NULL,"
                    + " actionTaken BOOLEAN DEFAULT 0, actionDate DATETIME DEFAULT NULL,"
                    + " submissionDate DATETIME NOT NULL, uploadStatus INTEGER NOT NULL DEFAULT 0,"
                    + " FOREIGN KEY (instanceID) REFERENCES tbl_product_instance(instanceID),"
                    + " FOREIGN KEY (detectedByEmployeeID) REFERENCES tbl_employee(employeeID));";
    private static final String TABLE_CREATE_DISPENSES =
            "CREATE TABLE IF NOT EXISTS tbl_dispenses (dispenseID TEXT PRIMARY KEY,"
                    + " dispenseDate DATETIME NOT NULL, typeDispenseID INTEGER NOT NULL,"
                    + " employeeID TEXT NOT NULL, statusID INTEGER NOT NULL,"
                    + " picture_name TEXT, amount REAL, paymentTypeID INTEGER NOT NULL,"
                    + " uploadStatus INTEGER DEFAULT 0,"
                    + " FOREIGN KEY (typeDispenseID) REFERENCES tbl_type_dispenses(typeDispenseID),"
                    + " FOREIGN KEY (employeeID) REFERENCES tbl_employee(employeeID),"
                    + " FOREIGN KEY (statusID) REFERENCES tbl_operationstatus(statusID),"
                    + " FOREIGN KEY (paymentTypeID) REFERENCES tbl_typepayment(paymentTypeID));";
    private static final String TABLE_CREATE_EMPLOYEE =
            "CREATE TABLE tbl_employee (employeeID TEXT PRIMARY KEY,"
                    + " employeeFirstName TEXT, employeeLastName TEXT, employeeTel TEXT,"
                    + " employeeEmail TEXT, employeePassword TEXT, fatherFullName TEXT,"
                    + " motherFullName TEXT, employeeBirthday TEXT, employeeAccountActivation TEXT,"
                    + " employeeCNI TEXT, apiKey TEXT, picture_name TEXT, picture_url TEXT);";
    private static final String TABLE_CREATE_MEASUREMENT_UNIT =
            "CREATE TABLE tbl_mesurement_unit (uniteID TEXT PRIMARY KEY,"
                    + " uniteName TEXT, uniteDescription TEXT, uniteSign TEXT);";
    private static final String TABLE_CREATE_OPERATION_STATUS =
            "CREATE TABLE tbl_operationstatus (statusID INTEGER PRIMARY KEY,"
                    + " statusLabel TEXT, statusDescription TEXT);";
    private static final String TABLE_CREATE_PAYMENT =
            "CREATE TABLE tbl_payment (paymentID TEXT PRIMARY KEY, cartID TEXT,"
                    + " paymentTypeID INTEGER, employeeID TEXT,"
                    + " FOREIGN KEY (paymentTypeID) REFERENCES tbl_typepayment(paymentTypeID),"
                    + " FOREIGN KEY (employeeID) REFERENCES tbl_employee(employeeID),"
                    + " FOREIGN KEY (cartID) REFERENCES tbl_cart(cartID));";
    private static final String TABLE_CREATE_PDF_FILES =
            "CREATE TABLE IF NOT EXISTS tbl_pdf_files"
                    + " (id_file_case INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, location TEXT);";
    private static final String TABLE_CREATE_PHYSICAL_CONTROLE =
            "CREATE TABLE tbl_physical_controle (controleID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + " controleDateTime DATETIME DEFAULT NULL, adminID VARCHAR(11) NOT NULL,"
                    + " employeeID VARCHAR(11) DEFAULT NULL,"
                    + " FOREIGN KEY (adminID) REFERENCES tbl_admin(adminID),"
                    + " FOREIGN KEY (employeeID) REFERENCES tbl_employee(employeeID));";
    private static final String TABLE_CREATE_PRODUCT =
            "CREATE TABLE tbl_product (productID TEXT PRIMARY KEY, productName TEXT,"
                    + " productManufacture TEXT, manufactureAddress TEXT, product_photo_name TEXT,"
                    + " product_add_date TEXT, product_seuil_stock INTEGER, adminID TEXT,"
                    + " typeProductID INTEGER, subSubAccountID TEXT, uniteID TEXT,"
                    + " isActiveToInstances INTEGER, isActiveToDecimalQuantity INTEGER,"
                    + " FOREIGN KEY (adminID) REFERENCES tbl_admin(adminID),"
                    + " FOREIGN KEY (typeProductID) REFERENCES tbl_product_type(typeProductID),"
                    + " FOREIGN KEY (uniteID) REFERENCES tbl_mesurement_unit(uniteID));";
    private static final String TABLE_CREATE_PRODUCT_INSTANCE =
            "CREATE TABLE IF NOT EXISTS tbl_product_instance"
                    + " (instanceID TEXT PRIMARY KEY, instanceState TEXT, stockID TEXT NOT NULL,"
                    + " FOREIGN KEY (stockID) REFERENCES tbl_stock(stockID))";
    private static final String TABLE_CREATE_PRODUCT_PRICE =
            "CREATE TABLE tbl_product_price (pricecaseID INTEGER PRIMARY KEY,"
                    + " price REAL, pricecaseState TEXT, priceStateDate TEXT, productID TEXT,"
                    + " FOREIGN KEY (productID) REFERENCES tbl_product(productID));";
    private static final String TABLE_CREATE_PRODUCT_TYPE =
            "CREATE TABLE tbl_product_type (typeProductID INTEGER PRIMARY KEY, nameTypeProduct TEXT);";
    private static final String TABLE_CREATE_STOCK =
            "CREATE TABLE tbl_stock (stockID TEXT PRIMARY KEY, stockDateTime TEXT,"
                    + " stockQuantity INTEGER, totalAmountUsed REAL, productID TEXT,"
                    + " stockManDate TEXT, stockExpDate TEXT, supplierName TEXT,"
                    + " supplierContact TEXT, factureNumber TEXT, factureImage_name TEXT,"
                    + " paymentTypeID INTEGER, employeeID TEXT, statusID INTEGER,"
                    + " uploadStatus INTEGER DEFAULT 0,"
                    + " FOREIGN KEY (productID) REFERENCES tbl_product(productID))";
    private static final String TABLE_CREATE_TYPE_DISPENSES =
            "CREATE TABLE IF NOT EXISTS tbl_type_dispenses"
                    + " (typeDispenseID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + " typeDispenseName TEXT NOT NULL, subAccountID TEXT NOT NULL,"
                    + " isActive BOOLEAN DEFAULT 1);";
    private static final String TABLE_CREATE_TYPE_PAYMENT =
            "CREATE TABLE tbl_typepayment (paymentTypeID INTEGER PRIMARY KEY,"
                    + " paymentMethod TEXT, subSubAccountID TEXT);";
    private static final String TABLE_CREATE_VERSEMENT =
            "CREATE TABLE IF NOT EXISTS tbl_versement (versementID TEXT PRIMARY KEY,"
                    + " employeeID TEXT NOT NULL, adminID TEXT DEFAULT NULL,"
                    + " statusID INTEGER NOT NULL, expectedAmount REAL NOT NULL,"
                    + " versedAmount REAL NOT NULL, versementPictureName TEXT NOT NULL,"
                    + " versementDateTime DATETIME NOT NULL, actionDate DATETIME NOT NULL,"
                    + " paymentTypeID INTEGER NOT NULL, uploadStatus INTEGER NOT NULL DEFAULT 0,"
                    + " FOREIGN KEY (employeeID) REFERENCES tbl_employee(employeeID),"
                    + " FOREIGN KEY (statusID) REFERENCES tbl_operationstatus(statusID),"
                    + " FOREIGN KEY (paymentTypeID) REFERENCES tbl_typepayment(paymentTypeID));";

    // -------------------------------------------------------------------------
    // Callback interfaces
    // -------------------------------------------------------------------------
    public interface DataUpdateCallback {
        void onComplete();
        void onFailure(String msg);
    }

    public interface PhysicalControlCallback {
        void onComplete(List<PhysicalControle> list);
        void onFailure(String msg);
    }

    public interface ServerStatusCallback {
        void onSuccess();
        void onFailure(String msg);
    }

    public interface UploadCallback {
        void onSuccess(String msg);
        void onFailure(String msg);
    }

    public interface LoginCallback {
        void onSuccess(Employee employee);
        void onFailure(String msg);
    }

    public interface PasswordCallback {
        void onSuccess(String msg);
        void onFailure(String msg);
    }

    // -------------------------------------------------------------------------
    // Constructor & lifecycle
    // -------------------------------------------------------------------------
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) throws SQLException {
        db.execSQL(TABLE_CREATE_PRODUCT);
        db.execSQL(TABLE_CREATE_PRODUCT_TYPE);
        db.execSQL(TABLE_CREATE_ADMIN);
        db.execSQL(TABLE_CREATE_EMPLOYEE);
        db.execSQL(TABLE_CREATE_STOCK);
        db.execSQL(TABLE_CREATE_CART);
        db.execSQL(TABLE_CREATE_CART_ITEMS_WITHOUT_INSTANCE);
        db.execSQL(TABLE_CREATE_CART_ITEMS_WITH_INSTANCE);
        db.execSQL(TABLE_CREATE_PAYMENT);
        db.execSQL(TABLE_CREATE_TYPE_PAYMENT);
        db.execSQL(TABLE_CREATE_PRODUCT_PRICE);
        db.execSQL(TABLE_CREATE_OPERATION_STATUS);
        db.execSQL(TABLE_CREATE_MEASUREMENT_UNIT);
        db.execSQL(TABLE_CREATE_PRODUCT_INSTANCE);
        db.execSQL(TABLE_CREATE_PHYSICAL_CONTROLE);
        db.execSQL(TABLE_CREATE_CONTROLE_CASE);
        db.execSQL(TABLE_CREATE_CLOSURE);
        db.execSQL(TABLE_CREATE_TYPE_DISPENSES);
        db.execSQL(TABLE_CREATE_DISPENSES);
        db.execSQL(TABLE_CREATE_VERSEMENT);
        db.execSQL(TABLE_CREATE_DETERIORATED_PRODUCT_WITH_INSTANCE);
        db.execSQL(TABLE_CREATE_DETERIORATED_PRODUCT_WITHOUT_INSTANCE);
        db.execSQL(TABLE_CREATE_PDF_FILES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            String[] tables = {
                    "tbl_product", "tbl_product_type", "tbl_admin", "tbl_employee",
                    "tbl_stock", "tbl_cart", "tbl_cart_itemswithoutinstance",
                    "tbl_cart_itemswithinstance", "tbl_payment", "tbl_typepayment",
                    "tbl_product_price", "tbl_operationstatus", "tbl_mesurement_unit",
                    "tbl_product_instance", "tbl_physical_controle", "tbl_controle_case",
                    "tbl_closure", "tbl_type_dispenses", "tbl_dispenses", "tbl_versement",
                    "tbl_deteriorated_product_with_instance",
                    "tbl_deteriorated_product_without_instance", "tbl_pdf_files"
            };
            for (String t : tables) db.execSQL("DROP TABLE IF EXISTS " + t);
            onCreate(db);
        }
    }

    // =========================================================================
    // SHARED PREFERENCES
    // =========================================================================
    public String getApiKey() {
        return context.getSharedPreferences("MyApp", 0).getString(COLUMN_API_KEY, "");
    }

    private String getLoggedInEmployeeID() {
        return context.getSharedPreferences("MyApp", 0).getString(COLUMN_EMPLOYEE_ID, "");
    }

    public String getEmployeeID(Context context) {
        return context.getSharedPreferences("MySharedPref", 0).getString(COLUMN_EMPLOYEE_ID, null);
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm != null ? cm.getActiveNetworkInfo() : null;
        return ni != null && ni.isConnectedOrConnecting();
    }

    public Context getContext() { return context; }

    // =========================================================================
    // PDF FILES
    // =========================================================================
    public void insertPdfFile(String name, String location) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put(COLUMN_LOCATION, location);
        db.insert(TABLE_PDF_FILES, null, cv);
        db.close();
    }

    public Cursor getAllPdfFiles() {
        return getReadableDatabase().query(TABLE_PDF_FILES, null, null, null, null, null, null);
    }

    // =========================================================================
    // TABLE MAINTENANCE
    // =========================================================================
    public void clearTable(String tableName) throws SQLException {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + tableName);
        db.close();
    }

    public void clearAllTables() throws SQLException {
        String[] tables = {
                TABLE_PRODUCT, TABLE_PRODUCT_TYPE, TABLE_ADMIN, TABLE_EMPLOYEE,
                TABLE_STOCK, TABLE_CART, TABLE_CART_ITEMS_WITHOUT_INSTANCE,
                TABLE_CART_ITEMS_WITH_INSTANCE, TABLE_PAYMENT, TABLE_TYPE_PAYMENT,
                TABLE_PRODUCT_PRICE, TABLE_OPERATION_STATUS, TABLE_MEASUREMENT_UNIT,
                TABLE_PRODUCT_INSTANCE, TABLE_PHYSICAL_CONTROLE, TABLE_CONTROLE_CASE,
                TABLE_CLOSURE, TABLE_TYPE_DISPENSES, TABLE_DISPENSES, TABLE_VERSEMENT,
                TABLE_DETERIORATED_PRODUCT_WITH_INSTANCE,
                TABLE_DETERIORATED_PRODUCT_WITHOUT_INSTANCE, TABLE_PDF_FILES
        };
        for (String t : tables) clearTable(t);
    }

    // =========================================================================
    // AUTHENTICATION
    // employee_login.php, change_password.php, check_password_validity.php
    // =========================================================================

    /** POST employee_login.php — body: { email, password } */
    public void loginEmployee(String email, String password, final LoginCallback callback) {
        Employee req = new Employee();
        req.setEmployeeEmail(email);
        req.setEmployeePassword(password);
        RetrofitInstance.getApiService().loginEmployee(req)
                .enqueue(new Callback<ServerResponse<Employee>>() {
                    @Override
                    public void onResponse(Call<ServerResponse<Employee>> call,
                                           Response<ServerResponse<Employee>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ServerResponse<Employee> body = response.body();
                            if (body.isSuccess() && body.getData() != null) {
                                addEmployee(body.getData());
                                callback.onSuccess(body.getData());
                            } else {
                                callback.onFailure(body.getMessage() != null ? body.getMessage() : "Login failed.");
                            }
                        } else {
                            callback.onFailure("HTTP " + response.code());
                        }
                    }
                    @Override public void onFailure(Call<ServerResponse<Employee>> call, Throwable t) {
                        callback.onFailure("Network error: " + t.getMessage());
                    }
                });
    }

    /** POST change_password.php — body: { apiKey, employeeID, existingPassword, newPassword } */
    public void changePassword(String employeeID, String existingPassword,
                               String newPassword, final PasswordCallback callback) {
        Map<String, String> body = new HashMap<>();
        body.put("apiKey", getApiKey());
        body.put("employeeID", employeeID);
        body.put("existingPassword", existingPassword);
        body.put("newPassword", newPassword);
        RetrofitInstance.getApiService().changePassword(body)
                .enqueue(new Callback<ServerResponse<Void>>() {
                    @Override
                    public void onResponse(Call<ServerResponse<Void>> call,
                                           Response<ServerResponse<Void>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            callback.onSuccess(response.body().getMessage());
                        } else {
                            String msg = (response.body() != null && response.body().getMessage() != null)
                                    ? response.body().getMessage() : "Password change failed.";
                            callback.onFailure(msg);
                        }
                    }
                    @Override public void onFailure(Call<ServerResponse<Void>> call, Throwable t) {
                        callback.onFailure("Network error: " + t.getMessage());
                    }
                });
    }

    /** POST check_password_validity.php — body: { apiKey, employeeID, password } */
    public void checkPasswordValidity(String employeeID, String password,
                                      final LoginCallback callback) {
        Map<String, String> body = new HashMap<>();
        body.put("apiKey", getApiKey());
        body.put("employeeID", employeeID);
        body.put("password", password);
        RetrofitInstance.getApiService().checkPasswordValidity(body)
                .enqueue(new Callback<ServerResponse<Employee>>() {
                    @Override
                    public void onResponse(Call<ServerResponse<Employee>> call,
                                           Response<ServerResponse<Employee>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ServerResponse<Employee> resp = response.body();
                            if (resp.isSuccess() && resp.getData() != null) {
                                callback.onSuccess(resp.getData());
                            } else {
                                callback.onFailure(resp.getMessage() != null
                                        ? resp.getMessage() : "Verification failed.");
                            }
                        } else {
                            callback.onFailure("HTTP " + response.code());
                        }
                    }
                    @Override public void onFailure(Call<ServerResponse<Employee>> call, Throwable t) {
                        callback.onFailure("Network error: " + t.getMessage());
                    }
                });
    }

    // =========================================================================
    // SYNC ALL — calls sync_all.php, replaces all individual GET calls on startup
    // =========================================================================
    public void syncAll(final DataUpdateCallback callback) {
        String apiKey = getApiKey();
        if (apiKey == null || apiKey.isEmpty()) { callback.onFailure("Missing API key"); return; }
        RetrofitInstance.getApiService().syncAll(apiKey)
                .enqueue(new Callback<SyncResponse>() {
                    @Override
                    public void onResponse(Call<SyncResponse> call, Response<SyncResponse> response) {
                        if (!response.isSuccessful() || response.body() == null) {
                            callback.onFailure("Sync failed, HTTP " + response.code()); return;
                        }
                        SyncResponse body = response.body();
                        if (!body.isSuccess()) {
                            callback.onFailure(body.getMessage() != null ? body.getMessage() : "Unknown error");
                            return;
                        }
                        SyncResponse.SyncData data = body.getData();
                        if (data == null) { callback.onFailure("Empty sync payload"); return; }
                        try {
                            processSyncData(data);
                            callback.onComplete();
                        } catch (Exception e) {
                            Log.e("DatabaseHelper", "processSyncData error", e);
                            callback.onFailure("Error processing sync: " + e.getMessage());
                        }
                    }
                    @Override public void onFailure(Call<SyncResponse> call, Throwable t) {
                        callback.onFailure("Network error: " + t.getMessage());
                    }
                });
    }

    private void processSyncData(SyncResponse.SyncData data) {
        // Static/reference tables first (parents before children)
        if (data.getProductTypes() != null)
            for (ProductType pt : data.getProductTypes()) if (pt != null) addProductType(pt);
        if (data.getPaymentTypes() != null)
            for (TypePayment tp : data.getPaymentTypes()) if (tp != null) savePaymentTypeToDatabase(tp);
        if (data.getOperationStatuses() != null)
            for (OperationStatus os : data.getOperationStatuses()) if (os != null) saveOperationStatusToDatabase(os);
        if (data.getMeasurementUnits() != null)
            for (MeasurementUnit mu : data.getMeasurementUnits()) if (mu != null) addMeasurementUnit(mu);
        if (data.getTypeDispenses() != null)
            for (TypeDispense td : data.getTypeDispenses()) if (td != null) addTypeDispense(td);
        // Products and prices
        if (data.getProducts() != null) {
            for (Product p : data.getProducts()) {
                if (p == null) continue;
                addProduct(p);
                if (p.getProductPhotoUrl() != null && p.getProductPhotoName() != null)
                    ImageDownloadUtil.downloadImageWithCustomPath(context, p.getProductPhotoUrl(), "products");
            }
        }
        if (data.getProductPrices() != null)
            for (ProductPrice pp : data.getProductPrices()) if (pp != null) addProductPrice(pp);
        // Stocks
        if (data.getStocks() != null) {
            for (Stock s : data.getStocks()) {
                if (s == null) continue;
                s.setUploadStatus(1);
                addStock(s);
                if (s.getFactureImageUrl() != null && !s.getFactureImageUrl().isEmpty())
                    ImageDownloadUtil.downloadImageWithCustomPath(context, s.getFactureImageUrl(), "factures");
            }
        }
        // Instances — flat list from server, each has stockID
        if (data.getInstances() != null) {
            SQLiteDatabase db = getWritableDatabase();
            for (ProductInstance pi : data.getInstances()) {
                if (pi == null) continue;
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_INSTANCE_ID, pi.getInstanceID());
                cv.put(COLUMN_STOCK_ID, pi.getStockID());
                cv.put(COLUMN_INSTANCE_STATE, pi.getInstanceState());
                db.insertWithOnConflict(TABLE_PRODUCT_INSTANCE, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
            }
            db.close();
        }
        // Transaction data
        if (data.getCarts() != null)
            for (Cart c : data.getCarts()) if (c != null) addCart(c);
        if (data.getCartItemsWithInstance() != null)
            for (CartItemWithInstance ci : data.getCartItemsWithInstance()) if (ci != null) addCartItemWithInstance(ci);
        if (data.getCartItemsWithoutInstance() != null)
            for (CartItemWithoutInstance ci : data.getCartItemsWithoutInstance()) if (ci != null) addCartItemWithoutInstance(ci);
        if (data.getPayments() != null)
            for (Payment p : data.getPayments()) if (p != null) addPayment(p);
        if (data.getClosures() != null)
            for (ClosureData cd : data.getClosures()) if (cd != null) addClosureData(cd);
        // Versements
        if (data.getVersements() != null) {
            for (Versement v : data.getVersements()) {
                if (v == null) continue;
                v.setUploadStatus(1);
                addVersement(v);
                if (v.getVersementPictureName() != null && v.getVersementPictureUrl() != null)
                    ImageDownloadUtil.downloadImageWithCustomPath(context, v.getVersementPictureUrl(), "versements");
            }
        }
        // Dispenses
        if (data.getDispenses() != null) {
            for (Dispense d : data.getDispenses()) {
                if (d == null) continue;
                d.setUploadStatus(1);
                addDispense(d);
                if (d.getPictureName() != null && d.getPictureUrl() != null)
                    ImageDownloadUtil.downloadImageWithCustomPath(context, d.getPictureUrl(), "dispenses");
            }
        }
        // Deteriorated products
        if (data.getDeterioratedWithInstance() != null) {
            for (DeterioratedProductWithInstance d : data.getDeterioratedWithInstance()) {
                if (d == null) continue;
                d.setUploadStatus(1);
                addDeterioratedProductWithInstance(d);
                if (d.getPictureName() != null && d.getPictureUrl() != null)
                    ImageDownloadUtil.downloadImageWithCustomPath(context, d.getPictureUrl(), "deteriorated");
            }
        }
        if (data.getDeterioratedWithoutInstance() != null) {
            for (DeterioratedProductWithoutInstance d : data.getDeterioratedWithoutInstance()) {
                if (d == null) continue;
                d.setUploadStatus(1);
                addDeterioratedProductWithoutInstance(d);
                if (d.getPictureName() != null && d.getPictureUrl() != null)
                    ImageDownloadUtil.downloadImageWithCustomPath(context, d.getPictureUrl(), "deteriorated");
            }
        }
        // Physical controls last (reference products)
        if (data.getPhysicalControls() != null)
            for (PhysicalControle pc : data.getPhysicalControls()) if (pc != null) addPhysicalControle(pc);
    }

    // =========================================================================
    // INDIVIDUAL GET ENDPOINTS (kept for selective post-upload refreshes)
    // =========================================================================

    /** GET get_products.php */
    public void getFromServerProducts(final DataUpdateCallback callback) {
        RetrofitInstance.getApiService().getProducts(getApiKey())
                .enqueue(new Callback<ServerResponse<List<Product>>>() {
                    @Override
                    public void onResponse(Call<ServerResponse<List<Product>>> call,
                                           Response<ServerResponse<List<Product>>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()
                                && response.body().getData() != null) {
                            for (Product p : response.body().getData()) {
                                if (p == null) continue;
                                addProduct(p);
                                if (p.getProductPhotoUrl() != null && p.getProductPhotoName() != null)
                                    ImageDownloadUtil.downloadImageWithCustomPath(context, p.getProductPhotoUrl(), "products");
                            }
                            callback.onComplete();
                        } else {
                            callback.onFailure("HTTP " + response.code());
                        }
                    }
                    @Override public void onFailure(Call<ServerResponse<List<Product>>> call, Throwable t) {
                        callback.onFailure("Network error: " + t.getMessage());
                    }
                });
    }

    /** GET get_product_prices.php */
    public void getFromServerProductPrices(final DataUpdateCallback callback) {
        RetrofitInstance.getApiService().getProductPrices(getApiKey())
                .enqueue(new Callback<ServerResponse<List<ProductPrice>>>() {
                    @Override
                    public void onResponse(Call<ServerResponse<List<ProductPrice>>> call,
                                           Response<ServerResponse<List<ProductPrice>>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()
                                && response.body().getData() != null) {
                            for (ProductPrice pp : response.body().getData()) if (pp != null) addProductPrice(pp);
                            callback.onComplete();
                        } else { callback.onFailure("HTTP " + response.code()); }
                    }
                    @Override public void onFailure(Call<ServerResponse<List<ProductPrice>>> call, Throwable t) {
                        callback.onFailure("Network error: " + t.getMessage());
                    }
                });
    }

    /** GET get_payment_types.php */
    public void getFromServerPaymentTypes(final DataUpdateCallback callback) {
        RetrofitInstance.getApiService().getPaymentTypes()
                .enqueue(new Callback<ServerResponse<List<TypePayment>>>() {
                    @Override
                    public void onResponse(Call<ServerResponse<List<TypePayment>>> call,
                                           Response<ServerResponse<List<TypePayment>>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()
                                && response.body().getData() != null) {
                            for (TypePayment tp : response.body().getData()) if (tp != null) savePaymentTypeToDatabase(tp);
                            callback.onComplete();
                        } else { callback.onFailure("HTTP " + response.code()); }
                    }
                    @Override public void onFailure(Call<ServerResponse<List<TypePayment>>> call, Throwable t) {
                        callback.onFailure("Network error: " + t.getMessage());
                    }
                });
    }

    /** GET get_operation_statuses.php */
    public void getFromServerOperationStatuses(final DataUpdateCallback callback) {
        RetrofitInstance.getApiService().getOperationStatuses()
                .enqueue(new Callback<ServerResponse<List<OperationStatus>>>() {
                    @Override
                    public void onResponse(Call<ServerResponse<List<OperationStatus>>> call,
                                           Response<ServerResponse<List<OperationStatus>>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()
                                && response.body().getData() != null) {
                            for (OperationStatus os : response.body().getData()) if (os != null) saveOperationStatusToDatabase(os);
                            callback.onComplete();
                        } else { callback.onFailure("HTTP " + response.code()); }
                    }
                    @Override public void onFailure(Call<ServerResponse<List<OperationStatus>>> call, Throwable t) {
                        callback.onFailure("Network error: " + t.getMessage());
                    }
                });
    }

    /** GET get_measurement_units.php */
    public void getFromServerMeasurementUnits(final DataUpdateCallback callback) {
        RetrofitInstance.getApiService().getMeasurementUnits()
                .enqueue(new Callback<ServerResponse<List<MeasurementUnit>>>() {
                    @Override
                    public void onResponse(Call<ServerResponse<List<MeasurementUnit>>> call,
                                           Response<ServerResponse<List<MeasurementUnit>>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()
                                && response.body().getData() != null) {
                            for (MeasurementUnit mu : response.body().getData()) if (mu != null) addMeasurementUnit(mu);
                            callback.onComplete();
                        } else { callback.onFailure("HTTP " + response.code()); }
                    }
                    @Override public void onFailure(Call<ServerResponse<List<MeasurementUnit>>> call, Throwable t) {
                        callback.onFailure("Network error: " + t.getMessage());
                    }
                });
    }

    /** GET get_type_dispenses.php */
    public void getFromServerTypeDispenses(final DataUpdateCallback callback) {
        RetrofitInstance.getApiService().getTypeDispenses(getApiKey())
                .enqueue(new Callback<ServerResponse<List<TypeDispense>>>() {
                    @Override
                    public void onResponse(Call<ServerResponse<List<TypeDispense>>> call,
                                           Response<ServerResponse<List<TypeDispense>>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()
                                && response.body().getData() != null) {
                            for (TypeDispense td : response.body().getData()) if (td != null) addTypeDispense(td);
                            callback.onComplete();
                        } else { callback.onFailure("HTTP " + response.code()); }
                    }
                    @Override public void onFailure(Call<ServerResponse<List<TypeDispense>>> call, Throwable t) {
                        callback.onFailure("Network error: " + t.getMessage());
                    }
                });
    }

    /** GET get_stocks.php */
    public void getFromServerStocks(final DataUpdateCallback callback) {
        RetrofitInstance.getApiService().getStocks(getApiKey())
                .enqueue(new Callback<ServerResponse<List<Stock>>>() {
                    @Override
                    public void onResponse(Call<ServerResponse<List<Stock>>> call,
                                           Response<ServerResponse<List<Stock>>> response) {
                        if (response.isSuccessful() && response.body() != null
                                && response.body().isSuccess() && response.body().getData() != null
                                && !response.body().getData().isEmpty()) {
                            HashSet<String> stockIDs = new HashSet<>();
                            SQLiteDatabase db = getWritableDatabase();
                            db.beginTransaction();
                            boolean ok = false;
                            try {
                                for (Stock s : response.body().getData()) {
                                    if (s == null) continue;
                                    stockIDs.add("'" + s.getStockID() + "'");
                                    ContentValues cv = stockToContentValues(s, 1);
                                    db.insertWithOnConflict(TABLE_STOCK, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
                                    if (s.getFactureImageUrl() != null && !s.getFactureImageUrl().isEmpty())
                                        ImageDownloadUtil.downloadImageWithCustomPath(context, s.getFactureImageUrl(), "factures");
                                }
                                if (!stockIDs.isEmpty())
                                    db.execSQL("DELETE FROM tbl_stock WHERE stockID NOT IN ("
                                            + TextUtils.join(",", stockIDs) + ") AND uploadStatus = 1");
                                db.setTransactionSuccessful();
                                ok = true;
                            } catch (Exception e) {
                                Log.e("DatabaseHelper", "Stock transaction error: " + e.getMessage());
                                callback.onFailure("Transaction error: " + e.getMessage());
                            } finally {
                                db.endTransaction();
                                db.close();
                            }
                            if (ok) callback.onComplete();
                        } else {
                            callback.onComplete();
                        }
                    }
                    @Override public void onFailure(Call<ServerResponse<List<Stock>>> call, Throwable t) {
                        callback.onFailure("Network error: " + t.getMessage());
                    }
                });
    }

    /** GET get_dispenses.php */
    public void getFromServerDispenses(final DataUpdateCallback callback) {
        RetrofitInstance.getApiService().getDispenses(getApiKey())
                .enqueue(new Callback<ServerResponse<List<Dispense>>>() {
                    @Override
                    public void onResponse(Call<ServerResponse<List<Dispense>>> call,
                                           Response<ServerResponse<List<Dispense>>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()
                                && response.body().getData() != null) {
                            for (Dispense d : response.body().getData()) {
                                if (d == null) continue;
                                d.setUploadStatus(1);
                                addDispense(d);
                                if (d.getPictureName() != null && d.getPictureUrl() != null)
                                    ImageDownloadUtil.downloadImageWithCustomPath(context, d.getPictureUrl(), "dispenses");
                            }
                            callback.onComplete();
                        } else { callback.onFailure("HTTP " + response.code()); }
                    }
                    @Override public void onFailure(Call<ServerResponse<List<Dispense>>> call, Throwable t) {
                        callback.onFailure("Network error: " + t.getMessage());
                    }
                });
    }

    /** GET get_versements.php */
    public void getFromServerVersements(final DataUpdateCallback callback) {
        RetrofitInstance.getApiService().getVersements(getApiKey())
                .enqueue(new Callback<ServerResponse<List<Versement>>>() {
                    @Override
                    public void onResponse(Call<ServerResponse<List<Versement>>> call,
                                           Response<ServerResponse<List<Versement>>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()
                                && response.body().getData() != null) {
                            for (Versement v : response.body().getData()) {
                                if (v == null) continue;
                                v.setUploadStatus(1);
                                addVersement(v);
                                if (v.getVersementPictureName() != null && v.getVersementPictureUrl() != null)
                                    ImageDownloadUtil.downloadImageWithCustomPath(context, v.getVersementPictureUrl(), "versements");
                            }
                            callback.onComplete();
                        } else { callback.onFailure("HTTP " + response.code()); }
                    }
                    @Override public void onFailure(Call<ServerResponse<List<Versement>>> call, Throwable t) {
                        callback.onFailure("Network error: " + t.getMessage());
                    }
                });
    }

    /** GET get_carts.php */
    public void getFromServerCarts(final DataUpdateCallback callback) {
        RetrofitInstance.getApiService().getCarts(getApiKey())
                .enqueue(new Callback<ServerResponse<List<Cart>>>() {
                    @Override
                    public void onResponse(Call<ServerResponse<List<Cart>>> call,
                                           Response<ServerResponse<List<Cart>>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()
                                && response.body().getData() != null) {
                            for (Cart c : response.body().getData()) if (c != null) addCart(c);
                            callback.onComplete();
                        } else { callback.onFailure("HTTP " + response.code()); }
                    }
                    @Override public void onFailure(Call<ServerResponse<List<Cart>>> call, Throwable t) {
                        callback.onFailure("Network error: " + t.getMessage());
                    }
                });
    }

    /** GET get_payments.php */
    public void getFromServerPayments(final DataUpdateCallback callback) {
        RetrofitInstance.getApiService().getPayments(getApiKey())
                .enqueue(new Callback<ServerResponse<List<Payment>>>() {
                    @Override
                    public void onResponse(Call<ServerResponse<List<Payment>>> call,
                                           Response<ServerResponse<List<Payment>>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()
                                && response.body().getData() != null) {
                            for (Payment p : response.body().getData()) if (p != null) addPayment(p);
                            callback.onComplete();
                        } else { callback.onFailure("HTTP " + response.code()); }
                    }
                    @Override public void onFailure(Call<ServerResponse<List<Payment>>> call, Throwable t) {
                        callback.onFailure("Network error: " + t.getMessage());
                    }
                });
    }

    /** GET get_cart_items_with_instance.php */
    public void getFromServerCartItemsWithInstance(final DataUpdateCallback callback) {
        RetrofitInstance.getApiService().getCartItemsWithInstance(getApiKey())
                .enqueue(new Callback<ServerResponse<List<CartItemWithInstance>>>() {
                    @Override
                    public void onResponse(Call<ServerResponse<List<CartItemWithInstance>>> call,
                                           Response<ServerResponse<List<CartItemWithInstance>>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()
                                && response.body().getData() != null) {
                            for (CartItemWithInstance ci : response.body().getData()) if (ci != null) addCartItemWithInstance(ci);
                            callback.onComplete();
                        } else { callback.onFailure("HTTP " + response.code()); }
                    }
                    @Override public void onFailure(Call<ServerResponse<List<CartItemWithInstance>>> call, Throwable t) {
                        callback.onFailure("Network error: " + t.getMessage());
                    }
                });
    }

    /** GET get_cart_items_without_instance.php */
    public void getFromServerCartItemsWithoutInstance(final DataUpdateCallback callback) {
        RetrofitInstance.getApiService().getCartItemsWithoutInstance(getApiKey())
                .enqueue(new Callback<ServerResponse<List<CartItemWithoutInstance>>>() {
                    @Override
                    public void onResponse(Call<ServerResponse<List<CartItemWithoutInstance>>> call,
                                           Response<ServerResponse<List<CartItemWithoutInstance>>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()
                                && response.body().getData() != null) {
                            for (CartItemWithoutInstance ci : response.body().getData()) if (ci != null) addCartItemWithoutInstance(ci);
                            callback.onComplete();
                        } else { callback.onFailure("HTTP " + response.code()); }
                    }
                    @Override public void onFailure(Call<ServerResponse<List<CartItemWithoutInstance>>> call, Throwable t) {
                        callback.onFailure("Network error: " + t.getMessage());
                    }
                });
    }

    /** GET get_closuredata.php */
    public void getFromServerClosures(final DataUpdateCallback callback) {
        RetrofitInstance.getApiService().getClosureData(getApiKey())
                .enqueue(new Callback<ServerResponse<List<ClosureData>>>() {
                    @Override
                    public void onResponse(Call<ServerResponse<List<ClosureData>>> call,
                                           Response<ServerResponse<List<ClosureData>>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()
                                && response.body().getData() != null) {
                            for (ClosureData cd : response.body().getData()) if (cd != null) addClosureData(cd);
                            callback.onComplete();
                        } else { callback.onFailure("HTTP " + response.code()); }
                    }
                    @Override public void onFailure(Call<ServerResponse<List<ClosureData>>> call, Throwable t) {
                        callback.onFailure("Network error: " + t.getMessage());
                    }
                });
    }

    /** GET get_physical_controls.php */
    public void fetchAndStorePhysicalControls(final PhysicalControlCallback callback) {
        RetrofitInstance.getApiService().getPhysicalControls(getApiKey())
                .enqueue(new Callback<ServerResponse<List<PhysicalControle>>>() {
                    @Override
                    public void onResponse(Call<ServerResponse<List<PhysicalControle>>> call,
                                           Response<ServerResponse<List<PhysicalControle>>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()
                                && response.body().getData() != null) {
                            List<PhysicalControle> data = response.body().getData();
                            for (PhysicalControle pc : data) if (pc != null) addPhysicalControle(pc);
                            callback.onComplete(data);
                        } else { callback.onFailure("HTTP " + response.code()); }
                    }
                    @Override public void onFailure(Call<ServerResponse<List<PhysicalControle>>> call, Throwable t) {
                        callback.onFailure("Network error: " + t.getMessage());
                    }
                });
    }

    /** GET get_deteriorated_products_with_instance.php */
    public void getFromServerDeterioratedProductsWithInstance(final DataUpdateCallback callback) {
        RetrofitInstance.getApiService().getDeterioratedProductsWithInstance(getApiKey())
                .enqueue(new Callback<ServerResponse<List<DeterioratedProductWithInstance>>>() {
                    @Override
                    public void onResponse(Call<ServerResponse<List<DeterioratedProductWithInstance>>> call,
                                           Response<ServerResponse<List<DeterioratedProductWithInstance>>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()
                                && response.body().getData() != null) {
                            for (DeterioratedProductWithInstance d : response.body().getData()) {
                                if (d == null) continue;
                                d.setUploadStatus(1);
                                addDeterioratedProductWithInstance(d);
                                if (d.getPictureName() != null && d.getPictureUrl() != null)
                                    ImageDownloadUtil.downloadImageWithCustomPath(context, d.getPictureUrl(), "deteriorated");
                            }
                            callback.onComplete();
                        } else { callback.onFailure("HTTP " + response.code()); }
                    }
                    @Override public void onFailure(Call<ServerResponse<List<DeterioratedProductWithInstance>>> call, Throwable t) {
                        callback.onFailure("Network error: " + t.getMessage());
                    }
                });
    }

    /** GET get_deteriorated_products_without_instance.php */
    public void getFromServerDeterioratedProductsWithoutInstance(final DataUpdateCallback callback) {
        RetrofitInstance.getApiService().getDeterioratedProductsWithoutInstance(getApiKey())
                .enqueue(new Callback<ServerResponse<List<DeterioratedProductWithoutInstance>>>() {
                    @Override
                    public void onResponse(Call<ServerResponse<List<DeterioratedProductWithoutInstance>>> call,
                                           Response<ServerResponse<List<DeterioratedProductWithoutInstance>>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()
                                && response.body().getData() != null) {
                            for (DeterioratedProductWithoutInstance d : response.body().getData()) {
                                if (d == null) continue;
                                d.setUploadStatus(1);
                                addDeterioratedProductWithoutInstance(d);
                                if (d.getPictureName() != null && d.getPictureUrl() != null)
                                    ImageDownloadUtil.downloadImageWithCustomPath(context, d.getPictureUrl(), "deteriorated");
                            }
                            callback.onComplete();
                        } else { callback.onFailure("HTTP " + response.code()); }
                    }
                    @Override public void onFailure(Call<ServerResponse<List<DeterioratedProductWithoutInstance>>> call, Throwable t) {
                        callback.onFailure("Network error: " + t.getMessage());
                    }
                });
    }

    /** GET get_server_status.php */
    public void getFromServerStatus(final ServerStatusCallback callback) {
        RetrofitInstance.getApiService().getServerStatus()
                .enqueue(new Callback<ServerResponse<String>>() {
                    @Override
                    public void onResponse(Call<ServerResponse<String>> call,
                                           Response<ServerResponse<String>> response) {
                        if (response.isSuccessful()) callback.onSuccess();
                        else callback.onFailure("HTTP " + response.code());
                    }
                    @Override public void onFailure(Call<ServerResponse<String>> call, Throwable t) {
                        callback.onFailure(t.getMessage());
                    }
                });
    }

    // =========================================================================
    // INSTANCES — get_instances.php & generate_instances.php
    // =========================================================================

    /** GET get_instances.php?stockID=... */
    public void fetchInstancesFromServer(final String stockID, final DataUpdateCallback callback) {
        RetrofitInstance.getApiService().getInstances(getApiKey(), stockID)
                .enqueue(new Callback<ServerResponse<List<ProductInstance>>>() {
                    @Override
                    public void onResponse(Call<ServerResponse<List<ProductInstance>>> call,
                                           Response<ServerResponse<List<ProductInstance>>> response) {
                        if (response.isSuccessful() && response.body() != null
                                && response.body().isSuccess() && response.body().getData() != null) {
                            saveInstancesLocallyByObjects(response.body().getData(), stockID, callback);
                        } else { callback.onFailure("HTTP " + response.code()); }
                    }
                    @Override public void onFailure(Call<ServerResponse<List<ProductInstance>>> call, Throwable t) {
                        callback.onFailure("Network error: " + t.getMessage());
                    }
                });
    }

    /** Fetches instances for multiple stockIDs; fires onComplete after all finish. */
    public void fetchAllInstancesForStocks(List<String> stockIDs, final DataUpdateCallback callback) {
        if (stockIDs == null || stockIDs.isEmpty()) { callback.onComplete(); return; }
        final int[] remaining = {stockIDs.size()};
        final boolean[] failed = {false};
        for (final String stockID : stockIDs) {
            fetchInstancesFromServer(stockID, new DataUpdateCallback() {
                @Override public void onComplete() {
                    synchronized (remaining) {
                        remaining[0]--;
                        if (remaining[0] == 0 && !failed[0]) callback.onComplete();
                    }
                }
                @Override public void onFailure(String msg) {
                    synchronized (remaining) {
                        if (!failed[0]) { failed[0] = true; callback.onFailure(msg); }
                    }
                }
            });
        }
    }

    /** POST generate_instances.php */
    public void generateInstancesOnServer(final String stockID, final DataUpdateCallback callback) {
        RetrofitInstance.getApiService().generateInstances(getApiKey(), stockID)
                .enqueue(new Callback<ServerResponse<List<String>>>() {
                    @Override
                    public void onResponse(Call<ServerResponse<List<String>>> call,
                                           Response<ServerResponse<List<String>>> response) {
                        if (response.isSuccessful() && response.body() != null
                                && response.body().isSuccess() && response.body().getData() != null) {
                            final List<String> ids = response.body().getData();
                            deleteLocalActiveInstances(stockID, new DataUpdateCallback() {
                                @Override public void onComplete() {
                                    saveInstancesLocallyByIDs(ids, stockID, callback);
                                }
                                @Override public void onFailure(String msg) {
                                    callback.onFailure("Delete local instances failed: " + msg);
                                }
                            });
                        } else { callback.onFailure("HTTP " + response.code()); }
                    }
                    @Override public void onFailure(Call<ServerResponse<List<String>>> call, Throwable t) {
                        callback.onFailure("Network error: " + t.getMessage());
                    }
                });
    }

    // =========================================================================
    // PHYSICAL CONTROL — add_physical_control.php
    // =========================================================================
    public void sendPhysicalControlData(String operationKey, List<Product> products,
                                        List<Double> actualQtys, final DataUpdateCallback callback) {
        String apiKey = context.getSharedPreferences("MyApp", 0).getString(COLUMN_API_KEY, null);
        if (apiKey == null) { callback.onFailure("Missing API key"); return; }
        String now = getCurrentDateTime();
        HashMap<String, Object> controle = new HashMap<>();
        controle.put(COLUMN_CONTROLE_DATETIME, now);
        controle.put("operationKey", operationKey);
        List<HashMap<String, Object>> cases = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            double expected = p.isUsingInstances()
                    ? getRemainingInstancesForProduct(p.getProductID())
                    : getRemainingQuantityWithoutInstances(p.getProductID());
            HashMap<String, Object> c = new HashMap<>();
            c.put(COLUMN_PRODUCT_ID, p.getProductID());
            c.put(COLUMN_EXPECTED_QUANTITY, expected);
            c.put(COLUMN_ACTUAL_QUANTITY, actualQtys.get(i));
            cases.add(c);
        }
        HashMap<String, Object> body = new HashMap<>();
        body.put("physical_controle", controle);
        body.put("controle_cases", cases);
        RetrofitInstance.getHttpService().addPhysicalControl(apiKey,
                        RequestBody.create(MediaType.parse("application/json"),
                                new GsonBuilder().setLenient().create().toJson(body)))
                .enqueue(new Callback<ServerResponse<Void>>() {
                    @Override
                    public void onResponse(Call<ServerResponse<Void>> call, Response<ServerResponse<Void>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            Toast.makeText(context, "Data sent successfully", Toast.LENGTH_SHORT).show();
                            callback.onComplete();
                        } else {
                            String msg = (response.body() != null && response.body().getMessage() != null)
                                    ? response.body().getMessage() : "HTTP " + response.code();
                            Toast.makeText(context, "Failed: " + msg, Toast.LENGTH_SHORT).show();
                            callback.onFailure(msg);
                        }
                    }
                    @Override public void onFailure(Call<ServerResponse<Void>> call, Throwable t) {
                        callback.onFailure("Network error: " + t.getMessage());
                    }
                });
    }

    // =========================================================================
    // UPLOAD METHODS (POST with multipart / JSON)
    // =========================================================================

    /** POST add_stock.php */
    public void uploadStockDataToServer(HttpService httpService, final Stock stock,
                                        final UploadCallback uploadCallback) {
        File file = new File(context.getFilesDir(), "factures/" + stock.getFactureImageName());
        if (!file.exists()) {
            uploadCallback.onFailure("Image file not found: " + file.getAbsolutePath()); return;
        }
        httpService.callUploadApi(
                MultipartBody.Part.createFormData("factureImage", file.getName(),
                        RequestBody.create(MediaType.parse("multipart/form-data"), file)),
                rp(stock.getStockID()), rp(String.valueOf(stock.getStockQuantity())),
                rp(String.valueOf(stock.getTotalAmountUsed())), rp(stock.getProductID()),
                rp(stock.getStockManDate()), rp(stock.getStockExpDate()),
                rp(stock.getSupplierName()), rp(stock.getSupplierContact()),
                rp(stock.getFactureNumber()), rp(String.valueOf(stock.getPaymentTypeID())),
                rp(getApiKey()), rp(String.valueOf(stock.getStatusID()))
        ).enqueue(new Callback<FileModel>() {
            @Override public void onResponse(Call<FileModel> call, Response<FileModel> response) {
                if (response.isSuccessful() && response.body() != null
                        && Boolean.TRUE.equals(response.body().getStatus())) {
                    uploadCallback.onSuccess(response.body().getMessage());
                    markStockAsUploaded(stock.getStockID());
                } else {
                    uploadCallback.onFailure(response.body() != null
                            ? response.body().getMessage() : "Unknown error");
                }
            }
            @Override public void onFailure(Call<FileModel> call, Throwable t) {
                uploadCallback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    /** POST add_versement.php */
    public void uploadVersementDataToServer(HttpService httpService, final Versement versement,
                                            final UploadCallback uploadCallback) {
        String pictureName = versement.getVersementPictureName();
        if (pictureName == null) { uploadCallback.onFailure("Picture name is null."); return; }
        File file = new File(context.getFilesDir(), "versements/" + pictureName);
        if (!file.exists()) { uploadCallback.onFailure("Image file not found: " + file.getAbsolutePath()); return; }

        RequestBody rbAdminID = (versement.getAdminID() == null || versement.getAdminID().isEmpty())
                ? null : rp(versement.getAdminID());

        Call<FileModel> call = (rbAdminID != null)
                ? httpService.callUploadVersementApi(
                imgPart("versementImage", file), rp(versement.getVersementID()),
                rp(versement.getVersementDateTime()), rp(versement.getEmployeeID()),
                rbAdminID, rp(String.valueOf(versement.getStatusID())),
                rp(String.valueOf(versement.getExpectedAmount())),
                rp(String.valueOf(versement.getVersedAmount())),
                rp(String.valueOf(versement.getPaymentTypeID())), rp(getApiKey()))
                : httpService.callUploadVersementApi(
                imgPart("versementImage", file), rp(versement.getVersementID()),
                rp(versement.getVersementDateTime()), rp(versement.getEmployeeID()),
                null, rp(String.valueOf(versement.getStatusID())),
                rp(String.valueOf(versement.getExpectedAmount())),
                rp(String.valueOf(versement.getVersedAmount())),
                rp(String.valueOf(versement.getPaymentTypeID())), rp(getApiKey()));

        call.enqueue(new Callback<FileModel>() {
            @Override public void onResponse(Call<FileModel> c, Response<FileModel> response) {
                if (response.isSuccessful()) {
                    FileModel body = response.body();
                    boolean ok = body != null && (Boolean.TRUE.equals(body.getStatus())
                            || "Versement added successfully.".equals(body.getMessage()));
                    if (ok) {
                        uploadCallback.onSuccess(body.getMessage());
                        markVersementAsUploaded(versement.getVersementID());
                    } else {
                        uploadCallback.onFailure(body != null ? body.getMessage() : "Unknown error");
                    }
                } else { uploadCallback.onFailure("HTTP " + response.code()); }
            }
            @Override public void onFailure(Call<FileModel> c, Throwable t) {
                uploadCallback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    public void uploadVersementsForDate(String date, final UploadCallback cb) {
        List<Versement> list = getVersementsForDate(date);
        if (list.isEmpty()) { cb.onSuccess("No versements to upload."); return; }
        for (Versement v : list)
            uploadVersementDataToServer(RetrofitInstance.getHttpService(), v,
                    new UploadCallback() {
                        @Override public void onSuccess(String m) { cb.onSuccess(m); }
                        @Override public void onFailure(String m) { cb.onFailure(m); }
                    });
    }

    /** POST add_dispense.php */
    public void uploadDispenseDataToServer(HttpService httpService, final Dispense dispense,
                                           final UploadCallback uploadCallback) {
        String pictureName = dispense.getPictureName();
        if (pictureName == null) { uploadCallback.onFailure("Picture name is null."); return; }
        File file = new File(context.getFilesDir(), "dispenses/" + pictureName);
        if (!file.exists()) { uploadCallback.onFailure("Image file not found: " + file.getAbsolutePath()); return; }

        httpService.callUploadDispenseApi(
                imgPart("dispenseImage", file),
                rp(dispense.getDispenseID()), rp(dispense.getDispenseDate()),
                rp(String.valueOf(dispense.getTypeDispenseID())),
                rp(String.valueOf(dispense.getAmount())), rp(dispense.getEmployeeID()),
                rp(getApiKey()), rp(String.valueOf(dispense.getStatusID())),
                rp(String.valueOf(dispense.getPaymentTypeID()))
        ).enqueue(new Callback<FileModel>() {
            @Override public void onResponse(Call<FileModel> call, Response<FileModel> response) {
                if (response.isSuccessful() && response.body() != null
                        && "New dispense added successfully.".equals(response.body().getMessage())) {
                    uploadCallback.onSuccess(response.body().getMessage());
                    markDispenseAsUploaded(dispense.getDispenseID());
                } else {
                    uploadCallback.onFailure(response.body() != null
                            ? response.body().getMessage() : "Unknown error");
                }
            }
            @Override public void onFailure(Call<FileModel> call, Throwable t) {
                uploadCallback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    public void uploadDispensesForDate(String date, final UploadCallback cb) {
        List<Dispense> list = getDispensesForDate(date);
        if (list.isEmpty()) { cb.onSuccess("No dispenses to upload."); return; }
        for (Dispense d : list)
            uploadDispenseDataToServer(RetrofitInstance.getHttpService(), d,
                    new UploadCallback() {
                        @Override public void onSuccess(String m) { cb.onSuccess(m); }
                        @Override public void onFailure(String m) { cb.onFailure(m); }
                    });
    }

    /** POST addDeterioratedProductWithInstance.php */
    public void uploadDeterioratedProductWithInstanceDataToServer(
            HttpService httpService, final DeterioratedProductWithInstance d,
            final UploadCallback uploadCallback) {
        String pictureName = d.getPictureName();
        if (pictureName == null) { uploadCallback.onFailure("Picture name is null."); return; }
        File file = new File(context.getFilesDir(), "deteriorated/" + pictureName);
        if (!file.exists()) { uploadCallback.onFailure("Image file not found: " + file.getAbsolutePath()); return; }

        httpService.callUploadDeterioratedProductWithInstanceApi(
                imgPart("deterioratedImage", file),
                rp(d.getDeterioratedProductWithInstanceID()), rp(d.getDeteriorationDate()),
                rp(d.getInstanceID()), rp(String.valueOf(d.getQuantity())),
                rp(d.getReason()), rp(d.getDetectedByEmployeeID()),
                rp(d.getSubmissionDate()), rp(getApiKey())
        ).enqueue(new Callback<FileModel>() {
            @Override public void onResponse(Call<FileModel> call, Response<FileModel> response) {
                if (response.isSuccessful()) {
                    FileModel body = response.body();
                    if (body != null && (Boolean.TRUE.equals(body.getStatus())
                            || "New deteriorated product with instance added successfully.".equals(body.getMessage()))) {
                        uploadCallback.onSuccess(body.getMessage());
                        markDeterioratedProductWithInstanceAsUploaded(d.getDeterioratedProductWithInstanceID());
                    } else { uploadCallback.onFailure(body != null ? body.getMessage() : "Unknown error"); }
                } else { uploadCallback.onFailure("HTTP " + response.code()); }
            }
            @Override public void onFailure(Call<FileModel> call, Throwable t) {
                uploadCallback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    /** POST addDeterioratedProductWithoutInstance.php */
    public void uploadDeterioratedProductWithoutInstanceDataToServer(
            HttpService httpService, final DeterioratedProductWithoutInstance d,
            final UploadCallback uploadCallback) {
        String pictureName = d.getPictureName();
        if (pictureName == null) { uploadCallback.onFailure("Picture name is null."); return; }
        File file = new File(context.getFilesDir(), "deteriorated/" + pictureName);
        if (!file.exists()) { uploadCallback.onFailure("Image file not found: " + file.getAbsolutePath()); return; }

        httpService.callUploadDeterioratedProductWithoutInstanceApi(
                imgPart("deterioratedImage", file),
                rp(d.getDeterioratedProductWithoutInstanceID()), rp(d.getDeteriorationDate()),
                rp(d.getProductID()), rp(String.valueOf(d.getQuantity())),
                rp(d.getReason()), rp(d.getDetectedByEmployeeID()),
                rp(d.getSubmissionDate()), rp(getApiKey())
        ).enqueue(new Callback<FileModel>() {
            @Override public void onResponse(Call<FileModel> call, Response<FileModel> response) {
                if (response.isSuccessful()) {
                    FileModel body = response.body();
                    if (body != null && (Boolean.TRUE.equals(body.getStatus())
                            || "New deteriorated product without instance added successfully.".equals(body.getMessage()))) {
                        uploadCallback.onSuccess(body.getMessage());
                        markDeterioratedProductWithoutInstanceAsUploaded(d.getDeterioratedProductWithoutInstanceID());
                    } else { uploadCallback.onFailure(body != null ? body.getMessage() : "Unknown error"); }
                } else { uploadCallback.onFailure("HTTP " + response.code()); }
            }
            @Override public void onFailure(Call<FileModel> call, Throwable t) {
                uploadCallback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    public void uploadDeterioratedProductsWithInstanceForDate(String date, final UploadCallback cb) {
        List<DeterioratedProductWithInstance> list = getDeterioratedProductsWithInstanceForDate(date);
        if (list.isEmpty()) { cb.onSuccess("No deteriorated-with-instance to upload."); return; }
        for (DeterioratedProductWithInstance d : list)
            uploadDeterioratedProductWithInstanceDataToServer(RetrofitInstance.getHttpService(), d,
                    new UploadCallback() {
                        @Override public void onSuccess(String m) { cb.onSuccess(m); }
                        @Override public void onFailure(String m) { cb.onFailure(m); }
                    });
    }

    public void uploadDeterioratedProductsWithoutInstanceForDate(String date, final UploadCallback cb) {
        List<DeterioratedProductWithoutInstance> list = getDeterioratedProductsWithoutInstanceForDate(date);
        if (list.isEmpty()) { cb.onSuccess("No deteriorated-without-instance to upload."); return; }
        for (DeterioratedProductWithoutInstance d : list)
            uploadDeterioratedProductWithoutInstanceDataToServer(RetrofitInstance.getHttpService(), d,
                    new UploadCallback() {
                        @Override public void onSuccess(String m) { cb.onSuccess(m); }
                        @Override public void onFailure(String m) { cb.onFailure(m); }
                    });
    }

    /** POST upload_closure_data.php */
    public void uploadClosureData(HttpService httpService, ClosureData closureData,
                                  final UploadCallback uploadCallback) {
        httpService.uploadClosureData(getApiKey(), closureData)
                .enqueue(new Callback<ServerResponse<Void>>() {
                    @Override public void onResponse(Call<ServerResponse<Void>> call,
                                                     Response<ServerResponse<Void>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess())
                            uploadCallback.onSuccess("Closure data uploaded successfully.");
                        else uploadCallback.onFailure("Failed to upload closure data.");
                    }
                    @Override public void onFailure(Call<ServerResponse<Void>> call, Throwable t) {
                        uploadCallback.onFailure("Network error: " + t.getMessage());
                    }
                });
    }

    /** POST upload_daily_carts.php */
    public void uploadCartsForDate(final String date, HttpService httpService,
                                   final UploadCallback uploadCallback) {
        List<Cart> carts = getCartsForDate(date);
        if (carts == null || carts.isEmpty()) {
            uploadCallback.onSuccess("No carts to upload for " + date); return;
        }
        httpService.uploadDailyCarts(getApiKey(), carts)
                .enqueue(new Callback<ServerResponse<Void>>() {
                    @Override public void onResponse(Call<ServerResponse<Void>> call,
                                                     Response<ServerResponse<Void>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess())
                            uploadCallback.onSuccess("Carts uploaded for " + date);
                        else uploadCallback.onFailure("Failed to upload carts for " + date);
                    }
                    @Override public void onFailure(Call<ServerResponse<Void>> call, Throwable t) {
                        uploadCallback.onFailure("Network error: " + t.getMessage());
                    }
                });
    }

    /** POST upload_daily_payments.php */
    public void uploadPaymentsForDate(final String date, HttpService httpService,
                                      final UploadCallback uploadCallback) {
        List<Payment> payments = getPaymentsForDate(date);
        if (payments == null || payments.isEmpty()) {
            uploadCallback.onSuccess("No payments to upload for " + date); return;
        }
        httpService.uploadDailyPayments(getApiKey(), payments)
                .enqueue(new Callback<ServerResponse<Void>>() {
                    @Override public void onResponse(Call<ServerResponse<Void>> call,
                                                     Response<ServerResponse<Void>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess())
                            uploadCallback.onSuccess("Payments uploaded for " + date);
                        else uploadCallback.onFailure("Failed to upload payments for " + date);
                    }
                    @Override public void onFailure(Call<ServerResponse<Void>> call, Throwable t) {
                        uploadCallback.onFailure("Network error: " + t.getMessage());
                    }
                });
    }

    // =========================================================================
    // LOCAL SQLite WRITE HELPERS
    // =========================================================================
    public void addProductType(ProductType pt) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TYPE_PRODUCT_ID, pt.getTypeProductID());
        cv.put(COLUMN_TYPE_PRODUCT_NAME, pt.getNameTypeProduct());
        db.insertWithOnConflict(TABLE_PRODUCT_TYPE, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void addAdmin(Admin admin) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ADMIN_ID, admin.getAdminID());
        cv.put(COLUMN_ADMIN_FIRST_NAME, admin.getAdminFirstName());
        cv.put(COLUMN_ADMIN_LAST_NAME, admin.getAdminLastName());
        db.insertWithOnConflict(TABLE_ADMIN, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void addEmployee(Employee employee) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_EMPLOYEE, null, null);
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_EMPLOYEE_ID, employee.getEmployeeID());
            cv.put(COLUMN_EMPLOYEE_FIRST_NAME, employee.getEmployeeFirstName());
            cv.put(COLUMN_EMPLOYEE_LAST_NAME, employee.getEmployeeLastName());
            cv.put(COLUMN_EMPLOYEE_TEL, employee.getEmployeeTel());
            cv.put(COLUMN_EMPLOYEE_EMAIL, employee.getEmployeeEmail());
            cv.put(COLUMN_FATHER_FULL_NAME, employee.getFatherFullName());
            cv.put(COLUMN_MOTHER_FULL_NAME, employee.getMotherFullName());
            cv.put(COLUMN_EMPLOYEE_BIRTHDAY, employee.getEmployeeBirthday());
            cv.put(COLUMN_EMPLOYEE_ACCOUNT_ACTIVATION, employee.getEmployeeAccountActivation());
            cv.put(COLUMN_EMPLOYEE_CNI, employee.getEmployeeCNI());
            cv.put(COLUMN_API_KEY, employee.getApiKey());
            cv.put(COLUMN_PICTURE_NAME, employee.getPictureName());
            cv.put(COLUMN_PICTURE_URL, employee.getPictureUrl());
            db.insert(TABLE_EMPLOYEE, null, cv);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public Employee getLoggedEmployee() {
        Cursor cursor = getReadableDatabase().query(TABLE_EMPLOYEE, null, null, null, null, null, null);
        if (cursor == null) return null;
        try {
            if (!cursor.moveToFirst()) return null;
            Employee e = new Employee();
            e.setEmployeeID(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_ID)));
            e.setEmployeeFirstName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_FIRST_NAME)));
            e.setEmployeeLastName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_LAST_NAME)));
            e.setEmployeeTel(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_TEL)));
            e.setEmployeeEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_EMAIL)));
            e.setFatherFullName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FATHER_FULL_NAME)));
            e.setMotherFullName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MOTHER_FULL_NAME)));
            e.setEmployeeBirthday(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_BIRTHDAY)));
            e.setEmployeeAccountActivation(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_ACCOUNT_ACTIVATION)));
            e.setEmployeeCNI(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_CNI)));
            e.setApiKey(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_API_KEY)));
            e.setPictureName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PICTURE_NAME)));
            e.setPictureUrl(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PICTURE_URL)));
            return e;
        } finally { cursor.close(); }
    }

    public void addCart(Cart cart) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CART_ID, cart.getCartID());
        cv.put(COLUMN_CART_DATE, cart.getTimestamp());
        cv.put(COLUMN_EMPLOYEE_ID, cart.getEmployeeID());
        db.insertWithOnConflict(TABLE_CART, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public long addCart(String cartID, String cartDate, String employeeID) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CART_ID, cartID);
        cv.put(COLUMN_CART_DATE, cartDate);
        cv.put(COLUMN_EMPLOYEE_ID, employeeID);
        long r = db.insertWithOnConflict(TABLE_CART, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        return r;
    }

    public void addCartItemWithInstance(CartItemWithInstance item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CART_ITEM_ID, item.getCartItemID());
        cv.put(COLUMN_CART_ID, item.getCartID());
        cv.put(COLUMN_INSTANCE_ID, item.getInstanceID());
        cv.put(COLUMN_PRICE_CASE_ID, item.getPricecaseID());
        db.insertWithOnConflict(TABLE_CART_ITEMS_WITH_INSTANCE, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void addCartItemWithoutInstance(CartItemWithoutInstance item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CART_ITEM_WITHOUT_INSTANCE_ID, item.getCartItemwithoutinstanceID());
        cv.put(COLUMN_QUANTITY_CART, item.getQuantityCart());
        cv.put(COLUMN_CART_ID, item.getCartID());
        cv.put(COLUMN_PRODUCT_ID, item.getProductID());
        cv.put(COLUMN_PRICE_CASE_ID, item.getPricecaseID());
        db.insertWithOnConflict(TABLE_CART_ITEMS_WITHOUT_INSTANCE, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void addProduct(Product product) {
        if (product == null) return;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PRODUCT_ID, product.getProductID());
        cv.put(COLUMN_PRODUCT_NAME, product.getProductName());
        cv.put(COLUMN_PRODUCT_MANUFACTURE, product.getProductManufacture());
        cv.put(COLUMN_MANUFACTURE_ADDRESS, product.getManufactureAddress());
        cv.put(COLUMN_PRODUCT_PHOTO_NAME, product.getProductPhotoName());
        cv.put(COLUMN_PRODUCT_ADD_DATE, product.getProductAddDate());
        cv.put(COLUMN_PRODUCT_SEUIL_STOCK, product.getProductSeuilStock());
        cv.put(COLUMN_ADMIN_ID, product.getAdminID());
        cv.put(COLUMN_TYPE_PRODUCT_ID, product.getTypeProductID());
        cv.put(COLUMN_SUB_SUB_ACCOUNT_ID, product.getSubSubAccountID());
        cv.put(COLUMN_UNITE_ID, product.getUniteID());
        cv.put(COLUMN_IS_ACTIVE_TO_INSTANCES, product.getIsActiveToInstances());
        cv.put(COLUMN_IS_ACTIVE_TO_DECIMAL_QUANTITY, product.getIsActiveToDecimalQuantity());
        db.insertWithOnConflict(TABLE_PRODUCT, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void savePaymentTypeToDatabase(TypePayment tp) {
        if (tp == null) return;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PAYMENT_TYPE_ID, tp.getPaymentTypeID());
        cv.put(COLUMN_PAYMENT_METHOD, tp.getPaymentMethod());
        cv.put(COLUMN_SUB_SUB_ACCOUNT_ID, tp.getSubSubAccountID());
        db.insertWithOnConflict(TABLE_TYPE_PAYMENT, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void addProductPrice(ProductPrice pp) {
        if (pp == null) return;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PRICE_CASE_ID, pp.getPricecaseID());
        cv.put(COLUMN_PRICE, pp.getPrice());
        cv.put(COLUMN_PRICE_CASE_STATE, pp.getPricecaseState());
        cv.put(COLUMN_PRICE_STATE_DATE, pp.getPriceStateDate());
        cv.put(COLUMN_PRODUCT_ID, pp.getProductID());
        db.insertWithOnConflict(TABLE_PRODUCT_PRICE, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void saveOperationStatusToDatabase(OperationStatus os) {
        if (os == null) return;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_STATUS_ID, os.getStatusID());
        cv.put(COLUMN_STATUS_LABEL, os.getStatusLabel());
        cv.put(COLUMN_STATUS_DESCRIPTION, os.getStatusDescription());
        db.insertWithOnConflict(TABLE_OPERATION_STATUS, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void addMeasurementUnit(MeasurementUnit mu) {
        if (mu == null) return;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_UNITE_ID, mu.getUniteID());
        cv.put(COLUMN_UNITE_NAME, mu.getUniteName());
        cv.put(COLUMN_UNITE_DESCRIPTION, mu.getUniteDescription());
        cv.put(COLUMN_UNITE_SIGN, mu.getUniteSign());
        db.insertWithOnConflict(TABLE_MEASUREMENT_UNIT, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void addTypeDispense(TypeDispense td) {
        if (td == null) return;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TYPE_DISPENSE_ID, td.getTypeDispenseID());
        cv.put(COLUMN_TYPE_DISPENSE_NAME, td.getTypeDispenseName());
        cv.put(COLUMN_SUB_ACCOUNT_ID, td.getSubAccountID());
        cv.put(COLUMN_IS_ACTIVE, td.isActive());
        db.insertWithOnConflict(TABLE_TYPE_DISPENSES, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public boolean addStock(Stock stock) {
        SQLiteDatabase db = getWritableDatabase();
        long r = db.insertWithOnConflict(TABLE_STOCK, null,
                stockToContentValues(stock, stock.getUploadStatus()), SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        return r != -1;
    }

    private ContentValues stockToContentValues(Stock s, int uploadStatus) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_STOCK_ID, s.getStockID());
        cv.put(COLUMN_STOCK_DATE_TIME, s.getStockDateTime());
        cv.put(COLUMN_STOCK_QUANTITY, s.getStockQuantity());
        cv.put(COLUMN_TOTAL_AMOUNT_USED, s.getTotalAmountUsed());
        cv.put(COLUMN_PRODUCT_ID, s.getProductID());
        cv.put(COLUMN_STOCK_MAN_DATE, s.getStockManDate());
        cv.put(COLUMN_STOCK_EXP_DATE, s.getStockExpDate());
        cv.put(COLUMN_SUPPLIER_NAME, s.getSupplierName());
        cv.put(COLUMN_SUPPLIER_CONTACT, s.getSupplierContact());
        cv.put(COLUMN_FACTURE_NUMBER, s.getFactureNumber());
        cv.put(COLUMN_FACTURE_IMAGE_NAME, s.getFactureImageName());
        cv.put(COLUMN_PAYMENT_TYPE_ID, s.getPaymentTypeID());
        cv.put(COLUMN_EMPLOYEE_ID, s.getEmployeeID());
        cv.put(COLUMN_STATUS_ID, s.getStatusID());
        cv.put(COLUMN_UPLOAD_STATUS, uploadStatus);
        return cv;
    }

    public void markStockAsUploaded(String stockID) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_UPLOAD_STATUS, 1);
        db.update(TABLE_STOCK, cv, "stockID = ?", new String[]{stockID});
        db.close();
    }

    public void addVersement(Versement v) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_VERSEMENT_ID, v.getVersementID());
        cv.put(COLUMN_EMPLOYEE_ID, v.getEmployeeID());
        cv.put(COLUMN_ADMIN_ID, v.getAdminID());
        cv.put(COLUMN_STATUS_ID, v.getStatusID());
        cv.put(COLUMN_EXPECTED_AMOUNT, v.getExpectedAmount());
        cv.put(COLUMN_VERSED_AMOUNT, v.getVersedAmount());
        cv.put(COLUMN_VERSEMENT_PICTURE_NAME, v.getVersementPictureName());
        cv.put(COLUMN_VERSEMENT_DATE_TIME, v.getVersementDateTime());
        cv.put(COLUMN_ACTION_DATE, v.getActionDate());
        cv.put(COLUMN_PAYMENT_TYPE_ID, v.getPaymentTypeID());
        cv.put(COLUMN_UPLOAD_STATUS, v.getUploadStatus());
        if (db.insertWithOnConflict(TABLE_VERSEMENT, null, cv, SQLiteDatabase.CONFLICT_REPLACE) == -1)
            Log.e("DatabaseHelper", "Error inserting versement");
        db.close();
    }

    public void markVersementAsUploaded(String versementID) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_UPLOAD_STATUS, 1);
        db.update(TABLE_VERSEMENT, cv, "versementID = ?", new String[]{versementID});
        db.close();
    }

    public void addDispense(Dispense dispense) {
        if (dispense == null) return;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DISPENSE_ID, dispense.getDispenseID());
        cv.put(COLUMN_DISPENSE_DATE, dispense.getDispenseDate());
        cv.put(COLUMN_TYPE_DISPENSE_ID, dispense.getTypeDispenseID());
        cv.put(COLUMN_EMPLOYEE_ID, dispense.getEmployeeID());
        cv.put(COLUMN_STATUS_ID, dispense.getStatusID());
        cv.put(COLUMN_PICTURE_NAME, dispense.getPictureName());
        cv.put(COLUMN_AMOUNT, dispense.getAmount());
        cv.put(COLUMN_PAYMENT_TYPE_ID, dispense.getPaymentTypeID());
        cv.put(COLUMN_UPLOAD_STATUS, dispense.getUploadStatus());
        db.insertWithOnConflict(TABLE_DISPENSES, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void markDispenseAsUploaded(String dispenseID) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_UPLOAD_STATUS, 1);
        db.update(TABLE_DISPENSES, cv, "dispenseID = ?", new String[]{dispenseID});
        db.close();
    }

    public void addDeterioratedProductWithoutInstance(DeterioratedProductWithoutInstance d) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DETERIORATED_PRODUCT_WITHOUT_INSTANCE_ID, d.getDeterioratedProductWithoutInstanceID());
        cv.put(COLUMN_PRODUCT_ID, d.getProductID());
        cv.put(COLUMN_QUANTITY, d.getQuantity());
        cv.put(COLUMN_REASON, d.getReason());
        cv.put(COLUMN_DETERIORATION_DATE, d.getDeteriorationDate());
        cv.put(COLUMN_PICTURE_NAME, d.getPictureName());
        cv.put(COLUMN_DETECTED_BY_EMPLOYEE_ID, d.getDetectedByEmployeeID());
        cv.put(COLUMN_ACTION_TAKEN, d.isActionTaken() ? 1 : 0);
        cv.put(COLUMN_ACTION_DATE, d.getActionDate());
        cv.put(COLUMN_SUBMISSION_DATE, d.getSubmissionDate());
        cv.put(COLUMN_UPLOAD_STATUS, d.getUploadStatus());
        db.insertWithOnConflict(TABLE_DETERIORATED_PRODUCT_WITHOUT_INSTANCE, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void addDeterioratedProductWithInstance(DeterioratedProductWithInstance d) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DETERIORATED_PRODUCT_WITH_INSTANCE_ID, d.getDeterioratedProductWithInstanceID());
        cv.put(COLUMN_INSTANCE_ID, d.getInstanceID());
        cv.put(COLUMN_QUANTITY, d.getQuantity());
        cv.put(COLUMN_REASON, d.getReason());
        cv.put(COLUMN_DETERIORATION_DATE, d.getDeteriorationDate());
        cv.put(COLUMN_PICTURE_NAME, d.getPictureName());
        cv.put(COLUMN_DETECTED_BY_EMPLOYEE_ID, d.getDetectedByEmployeeID());
        cv.put(COLUMN_ACTION_TAKEN, d.isActionTaken() ? 1 : 0);
        cv.put(COLUMN_ACTION_DATE, d.getActionDate());
        cv.put(COLUMN_SUBMISSION_DATE, d.getSubmissionDate());
        cv.put(COLUMN_UPLOAD_STATUS, d.getUploadStatus());
        db.insertWithOnConflict(TABLE_DETERIORATED_PRODUCT_WITH_INSTANCE, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void markDeterioratedProductWithInstanceAsUploaded(String id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_UPLOAD_STATUS, 1);
        db.update(TABLE_DETERIORATED_PRODUCT_WITH_INSTANCE, cv,
                "deterioratedProductWithInstanceID = ?", new String[]{id});
        db.close();
    }

    public void markDeterioratedProductWithoutInstanceAsUploaded(String id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_UPLOAD_STATUS, 1);
        db.update(TABLE_DETERIORATED_PRODUCT_WITHOUT_INSTANCE, cv,
                "deterioratedProductWithoutInstanceID = ?", new String[]{id});
        db.close();
    }

    public void addClosureData(ClosureData cd) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CLOSURE_ID, cd.getClosureID());
        cv.put(COLUMN_CLOSURE_DATE, cd.getClosureDate());
        cv.put(COLUMN_TOTAL_SALES, cd.getTotalSales());
        cv.put(COLUMN_AMOUNT_IN_STOCK, cd.getAmountInStock());
        cv.put(COLUMN_CLOSURE_STATUS, cd.getClosureStatus());
        cv.put(COLUMN_EMPLOYEE_ID, cd.getEmployeeID());
        cv.put(COLUMN_TOTAL_STOCKS_MADE, cd.getTotalStocksMade());
        cv.put(COLUMN_AMOUNT_IN_EXPENSES, cd.getAmountInExpenses());
        cv.put(COLUMN_VERSEMENT_DEPOSIT, cd.getVersementDeposit());
        if (db.insertWithOnConflict(TABLE_CLOSURE, null, cv, SQLiteDatabase.CONFLICT_REPLACE) == -1)
            Log.e("DatabaseHelper", "Failed to insert closure data.");
        db.close();
    }

    void addPhysicalControle(PhysicalControle pc) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CONTROLE_ID, pc.getControleID());
        cv.put(COLUMN_CONTROLE_DATETIME, pc.getControleDateTime());
        cv.put(COLUMN_ADMIN_ID, pc.getAdminID());
        cv.put(COLUMN_EMPLOYEE_ID, pc.getEmployeeID());
        db.insertWithOnConflict(TABLE_PHYSICAL_CONTROLE, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        db.delete(TABLE_CONTROLE_CASE, "controleID = ?", new String[]{String.valueOf(pc.getControleID())});
        for (ControleCase cc : pc.getControleCases()) {
            ContentValues cv2 = new ContentValues();
            cv2.put(COLUMN_CONTROLE_ID, cc.getControleID());
            cv2.put(COLUMN_PRODUCT_ID, cc.getProductID());
            cv2.put(COLUMN_EXPECTED_QUANTITY, cc.getExpectedQuantity());
            cv2.put(COLUMN_ACTUAL_QUANTITY, cc.getActualQuantity());
            db.insertWithOnConflict(TABLE_CONTROLE_CASE, null, cv2, SQLiteDatabase.CONFLICT_REPLACE);
        }
        db.close();
    }

    public long addPayment(Payment payment) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PAYMENT_ID, payment.getPaymentID());
        cv.put(COLUMN_CART_ID, payment.getCartID());
        cv.put(COLUMN_PAYMENT_TYPE_ID, payment.getPaymentTypeID());
        cv.put(COLUMN_EMPLOYEE_ID, payment.getEmployeeID());
        long r = db.insertWithOnConflict(TABLE_PAYMENT, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        return r;
    }

    // =========================================================================
    // INSTANCE LOCAL HELPERS
    // =========================================================================
    public String generateUniqueInstanceID() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 11; i++) sb.append(random.nextInt(10));
        return sb.toString();
    }

    public boolean addProductInstance(String stockID) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_INSTANCE_ID, generateUniqueInstanceID());
        cv.put(COLUMN_STOCK_ID, stockID);
        cv.put(COLUMN_INSTANCE_STATE, CommonCssConstants.ACTIVE);
        long r = db.insert(TABLE_PRODUCT_INSTANCE, null, cv);
        db.close();
        return r != -1;
    }

    public void saveInstancesLocallyByIDs(List<String> instanceIDs, String stockID,
                                          DataUpdateCallback callback) {
        SQLiteDatabase db = getWritableDatabase();
        boolean allOk = true;
        for (String id : instanceIDs) {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_INSTANCE_ID, id);
            cv.put(COLUMN_STOCK_ID, stockID);
            cv.put(COLUMN_INSTANCE_STATE, CommonCssConstants.ACTIVE);
            if (db.insertWithOnConflict(TABLE_PRODUCT_INSTANCE, null, cv,
                    SQLiteDatabase.CONFLICT_REPLACE) == -1) {
                Log.e("DatabaseHelper", "Error saving instance: " + id);
                allOk = false;
            }
        }
        db.close();
        if (allOk) callback.onComplete();
        else callback.onFailure("Error saving some instances locally.");
    }

    public void saveInstancesLocallyByObjects(List<ProductInstance> instances, String stockID,
                                              DataUpdateCallback callback) {
        SQLiteDatabase db = getWritableDatabase();
        boolean allOk = true;
        for (ProductInstance pi : instances) {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_INSTANCE_ID, pi.getInstanceID());
            cv.put(COLUMN_STOCK_ID, stockID);
            cv.put(COLUMN_INSTANCE_STATE, pi.getInstanceState());
            if (db.insertWithOnConflict(TABLE_PRODUCT_INSTANCE, null, cv,
                    SQLiteDatabase.CONFLICT_REPLACE) == -1) {
                Log.e("DatabaseHelper", "Error saving instance: " + pi.getInstanceID());
                allOk = false;
            }
        }
        db.close();
        if (allOk) callback.onComplete();
        else callback.onFailure("Error saving some instances locally.");
    }

    public void deleteLocalActiveInstances(String stockID, DataUpdateCallback callback) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(TABLE_PRODUCT_INSTANCE,
                "stockID = ? AND instanceState = 'active'", new String[]{stockID});
        db.close();
        if (rows >= 0) callback.onComplete();
        else callback.onFailure("Failed to delete local active instances.");
    }

    public int updateInstanceState(String instanceID, String state) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_INSTANCE_STATE, state);
        int rows = db.update(TABLE_PRODUCT_INSTANCE, cv, "instanceID = ?", new String[]{instanceID});
        db.close();
        return rows;
    }

    // =========================================================================
    // IMAGE FILE HELPERS
    // =========================================================================
    public String saveImageWithNewName(Uri uri, String fileName) throws IOException {
        InputStream is = context.getContentResolver().openInputStream(uri);
        File file = new File(context.getFilesDir(), "factures/" + fileName);
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        writeStream(is, file);
        return fileName;
    }

    public String saveDispenseImageWithNewName(Uri uri, String fileName) throws IOException {
        InputStream is = context.getContentResolver().openInputStream(uri);
        File file = new File(context.getFilesDir(), "dispenses/" + fileName);
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        writeStream(is, file);
        return fileName;
    }

    public String saveVersementImageWithNewName(Uri uri, String fileName) throws IOException {
        InputStream is = context.getContentResolver().openInputStream(uri);
        File file = new File(context.getFilesDir(), "versements/" + fileName);
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        writeStream(is, file);
        return fileName;
    }

    public String saveDeterioratedProductImageWithNewName(Uri uri, String fileName) throws IOException {
        InputStream is = context.getContentResolver().openInputStream(uri);
        File file = new File(context.getFilesDir(), "deteriorated/" + fileName);
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        writeStream(is, file);
        return fileName;
    }

    private void writeStream(InputStream is, File dest) throws IOException {
        FileOutputStream fos = new FileOutputStream(dest);
        byte[] buf = new byte[1024]; int len;
        while ((len = is.read(buf)) > 0) fos.write(buf, 0, len);
        is.close(); fos.close();
    }

    /** Short alias: create a text/plain RequestBody. Null value sends empty string. */
    private RequestBody rp(String value) {
        return RequestBody.create(value != null ? value : "", MediaType.parse("text/plain"));
    }

    /** Create a multipart image Part from a file. */
    private MultipartBody.Part imgPart(String fieldName, File file) {
        return MultipartBody.Part.createFormData(fieldName, file.getName(),
                RequestBody.create(MediaType.parse("multipart/form-data"), file));
    }

    private String getCurrentDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    private String getMimeType(String filePath) {
        String ext = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(filePath)).toString());
        return ext != null ? MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext.toLowerCase())
                : "application/octet-stream";
    }

    // =========================================================================
    // SINGLE-ROW LOOKUPS
    // =========================================================================
    public Product getProductByID(String productID) {
        Cursor cursor = getReadableDatabase().query(TABLE_PRODUCT, null,
                "productID = ?", new String[]{productID}, null, null, null);
        if (cursor == null) return null;
        try {
            if (!cursor.moveToFirst()) return null;
            return buildProductFromCursor(cursor);
        } finally { cursor.close(); }
    }

    public ProductType getProductTypeByID(int id) {
        Cursor cursor = getReadableDatabase().query(TABLE_PRODUCT_TYPE, null,
                "typeProductID = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor == null) return null;
        try {
            if (!cursor.moveToFirst()) return null;
            ProductType pt = new ProductType();
            pt.setTypeProductID(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TYPE_PRODUCT_ID)));
            pt.setNameTypeProduct(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE_PRODUCT_NAME)));
            return pt;
        } finally { cursor.close(); }
    }

    public Admin getAdminByID(int id) {
        Cursor cursor = getReadableDatabase().query(TABLE_ADMIN, null,
                "adminID = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor == null) return null;
        try {
            if (!cursor.moveToFirst()) return null;
            Admin a = new Admin();
            a.setAdminID(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ADMIN_ID)));
            a.setAdminFirstName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADMIN_FIRST_NAME)));
            a.setAdminLastName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADMIN_LAST_NAME)));
            return a;
        } finally { cursor.close(); }
    }

    public Employee getEmployeeByID(String id) {
        Cursor cursor = getReadableDatabase().query(TABLE_EMPLOYEE, null,
                "employeeID = ?", new String[]{id}, null, null, null);
        if (cursor == null) return null;
        try {
            if (!cursor.moveToFirst()) return null;
            Employee e = new Employee();
            e.setEmployeeID(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_ID)));
            e.setEmployeeFirstName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_FIRST_NAME)));
            e.setEmployeeLastName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_LAST_NAME)));
            e.setEmployeeTel(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_TEL)));
            e.setEmployeeEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_EMAIL)));
            e.setFatherFullName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FATHER_FULL_NAME)));
            e.setMotherFullName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MOTHER_FULL_NAME)));
            e.setEmployeeBirthday(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_BIRTHDAY)));
            e.setEmployeeAccountActivation(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_ACCOUNT_ACTIVATION)));
            e.setEmployeeCNI(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_CNI)));
            e.setApiKey(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_API_KEY)));
            e.setPictureName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PICTURE_NAME)));
            e.setPictureUrl(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PICTURE_URL)));
            return e;
        } finally { cursor.close(); }
    }

    public Cursor getStockById(String stockID) {
        return getReadableDatabase().rawQuery(
                "SELECT * FROM tbl_stock WHERE stockID = ?", new String[]{stockID});
    }

    public Cart getCartByID(String cartID) {
        Cursor cursor = getReadableDatabase().query(TABLE_CART, null,
                "cartID = ?", new String[]{cartID}, null, null, null);
        if (cursor == null) return null;
        try {
            if (!cursor.moveToFirst()) return null;
            Cart cart = new Cart();
            cart.setCartID(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CART_ID)));
            cart.setTimestamp(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CART_DATE)));
            cart.setEmployeeID(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_ID)));
            return cart;
        } finally { cursor.close(); }
    }

    public Payment getPaymentByID(String paymentID) {
        Cursor cursor = getReadableDatabase().query(TABLE_PAYMENT, null,
                "paymentID = ?", new String[]{paymentID}, null, null, null);
        if (cursor == null) return null;
        try {
            if (!cursor.moveToFirst()) return null;
            return new Payment(
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PAYMENT_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CART_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PAYMENT_TYPE_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_ID)));
        } finally { cursor.close(); }
    }

    public ProductPrice getProductPriceByID(int id) {
        Cursor cursor = getReadableDatabase().query(TABLE_PRODUCT_PRICE, null,
                "pricecaseID = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor == null) return null;
        try {
            if (!cursor.moveToFirst()) return null;
            ProductPrice pp = new ProductPrice();
            pp.setPricecaseID(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRICE_CASE_ID)));
            pp.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)));
            pp.setPricecaseState(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRICE_CASE_STATE)));
            pp.setPriceStateDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRICE_STATE_DATE)));
            pp.setProductID(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)));
            return pp;
        } finally { cursor.close(); }
    }

    public OperationStatus getOperationStatusByID(int id) {
        Cursor cursor = getReadableDatabase().query(TABLE_OPERATION_STATUS, null,
                "statusID = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor == null) return null;
        try {
            if (!cursor.moveToFirst()) return null;
            OperationStatus os = new OperationStatus();
            os.setStatusID(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STATUS_ID)));
            os.setStatusLabel(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS_LABEL)));
            os.setStatusDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS_DESCRIPTION)));
            return os;
        } finally { cursor.close(); }
    }

    public OperationStatus getOperationStatus(int statusID) { return getOperationStatusByID(statusID); }

    public ProductPrice getCurrentPriceForProduct(String productID) {
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT * FROM tbl_product_price WHERE productID = ? ORDER BY priceStateDate DESC LIMIT 1",
                new String[]{productID});
        if (cursor == null) return null;
        try {
            if (!cursor.moveToFirst()) return null;
            ProductPrice pp = new ProductPrice();
            pp.setPricecaseID(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRICE_CASE_ID)));
            pp.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)));
            pp.setPricecaseState(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRICE_CASE_STATE)));
            pp.setPriceStateDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRICE_STATE_DATE)));
            pp.setProductID(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)));
            return pp;
        } finally { cursor.close(); }
    }

    // =========================================================================
    // SCALAR LOOKUPS
    // =========================================================================
    public double getExpectedSellingPrice(String productID) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT price FROM tbl_product_price WHERE productID = ? AND pricecaseState = 'active'",
                new String[]{productID});
        try { return c.moveToFirst() ? c.getDouble(0) : 0.0; } finally { c.close(); }
    }

    public String getProductName(String productID) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT productName FROM tbl_product WHERE productID = ?", new String[]{productID});
        try { return c.moveToFirst() ? c.getString(0) : null; } finally { c.close(); }
    }

    public String getStatusLabel(int statusID) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT statusLabel FROM tbl_operationstatus WHERE statusID = ?",
                new String[]{String.valueOf(statusID)});
        try { return c.moveToFirst() ? c.getString(0) : null; } finally { c.close(); }
    }

    public String getProductPhotoName(String productID) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT product_photo_name FROM tbl_product WHERE productID = ?", new String[]{productID});
        try { return c.moveToFirst() ? c.getString(0) : null; } finally { c.close(); }
    }

    public String getFactureImageName(String stockID) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT factureImage_name FROM tbl_stock WHERE stockID = ?", new String[]{stockID});
        try { return c.moveToFirst() ? c.getString(0) : null; } finally { c.close(); }
    }

    public int getUploadStatus(String stockID) {
        Cursor c = getReadableDatabase().query(TABLE_STOCK, new String[]{COLUMN_UPLOAD_STATUS},
                "stockID = ?", new String[]{stockID}, null, null, null);
        if (c == null) return 0;
        try { return c.moveToFirst() ? c.getInt(0) : 0; } finally { c.close(); }
    }

    public int getPricecaseIDByInstance(String instanceID) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT PP.pricecaseID FROM tbl_product_instance PI"
                        + " JOIN tbl_stock S ON PI.stockID = S.stockID"
                        + " JOIN tbl_product P ON S.productID = P.productID"
                        + " JOIN tbl_product_price PP ON P.productID = PP.productID"
                        + " WHERE PI.instanceID = ? AND PP.pricecaseState = 'active'",
                new String[]{instanceID});
        if (c == null) return -1;
        try { return c.moveToFirst() ? c.getInt(0) : -1; } finally { c.close(); }
    }

    public int getPricecaseIDByProductID(String productID) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT pricecaseID FROM tbl_product_price WHERE productID = ? AND pricecaseState = 'active'",
                new String[]{productID});
        if (c == null) return -1;
        try { return c.moveToFirst() ? c.getInt(0) : -1; } finally { c.close(); }
    }

    public int getPaymentTypeID(String paymentMethod) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT paymentTypeID FROM tbl_typepayment WHERE paymentMethod = ?",
                new String[]{paymentMethod});
        if (c == null) return -1;
        try { return c.moveToFirst() ? c.getInt(0) : -1; } finally { c.close(); }
    }

    public boolean doesInstanceExist(String instanceID) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT 1 FROM tbl_product_instance WHERE instanceID = ?", new String[]{instanceID});
        if (c == null) return false;
        try { return c.moveToFirst(); } finally { c.close(); }
    }

    public boolean isInstanceSold(String instanceID) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT instanceState FROM tbl_product_instance WHERE instanceID = ?",
                new String[]{instanceID});
        if (c == null) return false;
        try { return c.moveToFirst() && "sold".equals(c.getString(0)); } finally { c.close(); }
    }

    public boolean isInstanceDeteriorated(String instanceID) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT instanceState FROM tbl_product_instance WHERE instanceID = ?",
                new String[]{instanceID});
        if (c == null) return false;
        try { return c.moveToFirst() && "deteriorated".equals(c.getString(0)); } finally { c.close(); }
    }

    public boolean isProductActiveToInstances(String productID) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT isActiveToInstances FROM tbl_product WHERE productID = ?",
                new String[]{productID});
        try { return c.moveToFirst() && c.getInt(0) == 1; }
        finally { c.close(); db.close(); }
    }

    // =========================================================================
    // LIST QUERIES
    // =========================================================================
    public Cursor getAllPaymentTypes() {
        return getReadableDatabase().query(TABLE_TYPE_PAYMENT, null, null, null, null, null, null);
    }
    public Cursor getAllTypeDispenses() {
        return getReadableDatabase().query(TABLE_TYPE_DISPENSES, null, null, null, null, null, null);
    }
    public Cursor getAllProductTypes() {
        return getReadableDatabase().query(TABLE_PRODUCT_TYPE, null, null, null, null, null, null);
    }
    public Cursor getAllProductsCursor() {
        return getReadableDatabase().query(TABLE_PRODUCT, null, null, null, null, null, "productName ASC");
    }
    public Cursor getAllStocksCursor() {
        return getReadableDatabase().query(TABLE_STOCK, null, null, null, null, null, "stockDateTime DESC");
    }

    public List<Product> getAllProductsList() {
        List<Product> list = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query(TABLE_PRODUCT, null, null, null, null, null, null);
        if (cursor != null) {
            try { if (cursor.moveToFirst()) do { list.add(buildProductFromCursor(cursor)); } while (cursor.moveToNext()); }
            finally { cursor.close(); }
        }
        return list;
    }

    public List<Stock> getAllStocksList() {
        List<Stock> list = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query(TABLE_STOCK, null, null, null, null, null, null);
        if (cursor != null) {
            try { if (cursor.moveToFirst()) do { list.add(buildStockFromCursor(cursor)); } while (cursor.moveToNext()); }
            finally { cursor.close(); }
        }
        return list;
    }

    public List<String> getAllStockIDs() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_STOCK, new String[]{COLUMN_STOCK_ID}, null, null, null, null, null);
        if (cursor != null) {
            try { while (cursor.moveToNext()) list.add(cursor.getString(0)); }
            finally { cursor.close(); }
        }
        db.close();
        return list;
    }

    public List<Product> getProductsWithoutInstances() {
        List<Product> list = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT * FROM tbl_product WHERE isActiveToInstances = 0 ORDER BY productName COLLATE NOCASE", null);
        if (cursor != null) {
            try { if (cursor.moveToFirst()) do { list.add(buildProductFromCursor(cursor)); } while (cursor.moveToNext()); }
            finally { cursor.close(); }
        }
        return list;
    }

    public List<Product> searchProductsWithoutInstances(String query) {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM tbl_product WHERE isActiveToInstances = 0 AND productName LIKE ?"
                        + " ORDER BY productName COLLATE NOCASE", new String[]{"%" + query + "%"});
        if (cursor != null) {
            try { if (cursor.moveToFirst()) do { list.add(buildProductFromCursor(cursor)); } while (cursor.moveToNext()); }
            finally { cursor.close(); }
        }
        db.close();
        return list;
    }

    public List<Product> getDistinctProductsFromStock() {
        List<Product> list = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT DISTINCT p.productID, p.isActiveToInstances FROM tbl_stock s"
                        + " JOIN tbl_product p ON s.productID = p.productID ORDER BY s.stockDateTime DESC", null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) do {
                    Product p = new Product();
                    p.setProductID(cursor.getString(0));
                    p.setIsActiveToInstances(cursor.getInt(1));
                    list.add(p);
                } while (cursor.moveToNext());
            } finally { cursor.close(); }
        }
        return list;
    }

    public List<Stock> getStocksForProduct(String productID) {
        List<Stock> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_STOCK, null, "productID = ?", new String[]{productID}, null, null, null);
        if (cursor != null) {
            try { if (cursor.moveToFirst()) do { list.add(buildStockFromCursor(cursor)); } while (cursor.moveToNext()); }
            finally { cursor.close(); }
        }
        db.close();
        return list;
    }

    public List<Stock> getStocksForProductFIFO(String productID) {
        List<Stock> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM tbl_stock WHERE productID = ? AND statusID = 3 ORDER BY stockDateTime ASC",
                new String[]{productID});
        if (cursor != null) {
            try { if (cursor.moveToFirst()) do { list.add(buildStockFromCursor(cursor)); } while (cursor.moveToNext()); }
            finally { cursor.close(); }
        }
        db.close();
        return list;
    }

    public List<Stock> getStocksForDate(String date) {
        List<Stock> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM tbl_stock WHERE DATE(stockDateTime) = ?", new String[]{date});
        if (cursor != null) {
            try { if (cursor.moveToFirst()) do { list.add(buildStockFromCursor(cursor)); } while (cursor.moveToNext()); }
            finally { cursor.close(); }
        }
        db.close();
        return list;
    }

    public List<String> getProductInstances(String stockID) {
        List<String> list = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT instanceID FROM tbl_product_instance WHERE stockID = ?", new String[]{stockID});
        try { if (cursor.moveToFirst()) do { list.add(cursor.getString(0)); } while (cursor.moveToNext()); }
        finally { cursor.close(); }
        return list;
    }

    public List<ProductInstance> getProductInstancesForStock(String stockID) {
        List<ProductInstance> list = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query(TABLE_PRODUCT_INSTANCE, null,
                "stockID = ?", new String[]{stockID}, null, null, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) do {
                    ProductInstance pi = new ProductInstance();
                    pi.setInstanceID(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INSTANCE_ID)));
                    pi.setInstanceState(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INSTANCE_STATE)));
                    pi.setStockID(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STOCK_ID)));
                    list.add(pi);
                } while (cursor.moveToNext());
            } finally { cursor.close(); }
        }
        return list;
    }

    public List<TypeDispense> fetchAllTypeDispensesAsList() {
        List<TypeDispense> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tbl_type_dispenses", null);
        try {
            if (cursor.moveToFirst()) do {
                list.add(new TypeDispense(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TYPE_DISPENSE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE_DISPENSE_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUB_ACCOUNT_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_ACTIVE))));
            } while (cursor.moveToNext());
        } finally { cursor.close(); db.close(); }
        return list;
    }

    public List<Dispense> fetchAllDispensesAsList() {
        List<Dispense> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tbl_dispenses", null);
        try { if (cursor.moveToFirst()) do { list.add(buildDispenseFromCursor(cursor)); } while (cursor.moveToNext()); }
        finally { cursor.close(); db.close(); }
        return list;
    }

    public List<Versement> fetchAllVersementsAsList() {
        List<Versement> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tbl_versement", null);
        try {
            if (cursor.moveToFirst()) do {
                list.add(new Versement(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VERSEMENT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADMIN_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STATUS_ID)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_EXPECTED_AMOUNT)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_VERSED_AMOUNT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VERSEMENT_PICTURE_NAME)),
                        "",
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VERSEMENT_DATE_TIME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACTION_DATE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PAYMENT_TYPE_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_UPLOAD_STATUS))));
            } while (cursor.moveToNext());
        } finally { cursor.close(); db.close(); }
        return list;
    }

    public List<DeterioratedProductWithInstance> fetchAllDeterioratedProductsWithInstances() {
        List<DeterioratedProductWithInstance> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_DETERIORATED_PRODUCT_WITH_INSTANCE, null,
                null, null, null, null, "submissionDate DESC");
        if (cursor != null) {
            try { if (cursor.moveToFirst()) do { list.add(buildDWI(cursor)); } while (cursor.moveToNext()); }
            finally { cursor.close(); }
        }
        db.close();
        return list;
    }

    public List<DeterioratedProductWithoutInstance> fetchAllDeterioratedProductsWithoutInstances() {
        List<DeterioratedProductWithoutInstance> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_DETERIORATED_PRODUCT_WITHOUT_INSTANCE, null,
                null, null, null, null, "submissionDate DESC");
        if (cursor != null) {
            try { if (cursor.moveToFirst()) do { list.add(buildDWOI(cursor)); } while (cursor.moveToNext()); }
            finally { cursor.close(); }
        }
        db.close();
        return list;
    }

    // =========================================================================
    // CART / PAYMENT LIST QUERIES
    // =========================================================================
    public List<Cart> getAllCarts() {
        List<Cart> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tbl_cart ORDER BY cartDate DESC", null);
        try {
            if (cursor.moveToFirst()) do {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CART_ID));
                List<CartItem> items = getCartItemsByCartID(id);
                list.add(new Cart(id, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CART_DATE)),
                        "BIF", calculateTotalAmount(items), items,
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_ID))));
            } while (cursor.moveToNext());
        } finally { cursor.close(); db.close(); }
        return list;
    }

    public List<Cart> getTodayCarts() {
        List<Cart> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Cursor cursor = db.rawQuery("SELECT * FROM tbl_cart WHERE DATE(cartDate) = ?", new String[]{today});
        try {
            if (cursor.moveToFirst()) do {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CART_ID));
                List<CartItem> items = getCartItemsByCartID(id);
                list.add(new Cart(id, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CART_DATE)),
                        "BIF", calculateTotalAmount(items), items,
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_ID))));
            } while (cursor.moveToNext());
        } finally { cursor.close(); db.close(); }
        return list;
    }

    public List<Cart> getCartsForDate(String date) {
        List<Cart> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM tbl_cart WHERE DATE(cartDate) = ?", new String[]{date});
        try {
            if (cursor.moveToFirst()) do {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CART_ID));
                List<CartItem> items = getCartItemsByCartID(id);
                list.add(new Cart(id, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CART_DATE)),
                        "BIF", calculateTotalAmount(items), items,
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_ID))));
            } while (cursor.moveToNext());
        } finally { cursor.close(); db.close(); }
        return list;
    }

    public List<Cart> getCartsBetweenDates(String from, String to) {
        List<Cart> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM tbl_cart WHERE date(cartDate) BETWEEN ? AND ? ORDER BY cartDate DESC",
                new String[]{from, to});
        try {
            if (cursor.moveToFirst()) do {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CART_ID));
                List<CartItem> items = getCartItemsByCartID(id);
                list.add(new Cart(id, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CART_DATE)),
                        "BIF", calculateTotalAmount(items), items,
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_ID))));
            } while (cursor.moveToNext());
        } finally { cursor.close(); db.close(); }
        return list;
    }

    public List<CartItem> getCartItemsByCartID(String cartID) {
        List<CartItem> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c1 = db.rawQuery(
                "SELECT ciwi.*, p.productName AS productName, pp.price AS unitPrice"
                        + " FROM tbl_cart_itemswithinstance ciwi"
                        + " JOIN tbl_product_instance pi ON ciwi.instanceID = pi.instanceID"
                        + " JOIN tbl_stock s ON pi.stockID = s.stockID"
                        + " JOIN tbl_product p ON s.productID = p.productID"
                        + " JOIN tbl_product_price pp ON ciwi.pricecaseID = pp.pricecaseID"
                        + " WHERE ciwi.cartID = ?", new String[]{cartID});
        try {
            if (c1.moveToFirst()) do {
                list.add(new CartItem(
                        c1.getString(c1.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME)), 1.0,
                        c1.getDouble(c1.getColumnIndexOrThrow("unitPrice")), null,
                        c1.getString(c1.getColumnIndexOrThrow(COLUMN_INSTANCE_ID)),
                        String.valueOf(c1.getInt(c1.getColumnIndexOrThrow(COLUMN_PRICE_CASE_ID))),
                        c1.getString(c1.getColumnIndexOrThrow(COLUMN_CART_ITEM_ID))));
            } while (c1.moveToNext());
        } finally { c1.close(); }
        Cursor c2 = db.rawQuery(
                "SELECT ciwoi.*, p.productName AS productName, pp.price AS unitPrice"
                        + " FROM tbl_cart_itemswithoutinstance ciwoi"
                        + " JOIN tbl_product p ON ciwoi.productID = p.productID"
                        + " JOIN tbl_product_price pp ON ciwoi.pricecaseID = pp.pricecaseID"
                        + " WHERE ciwoi.cartID = ?", new String[]{cartID});
        try {
            if (c2.moveToFirst()) do {
                list.add(new CartItem(
                        c2.getString(c2.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME)),
                        c2.getDouble(c2.getColumnIndexOrThrow(COLUMN_QUANTITY_CART)),
                        c2.getDouble(c2.getColumnIndexOrThrow("unitPrice")),
                        c2.getString(c2.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)), null,
                        String.valueOf(c2.getInt(c2.getColumnIndexOrThrow(COLUMN_PRICE_CASE_ID))),
                        c2.getString(c2.getColumnIndexOrThrow(COLUMN_CART_ITEM_WITHOUT_INSTANCE_ID))));
            } while (c2.moveToNext());
        } finally { c2.close(); db.close(); }
        return list;
    }

    public List<Payment> getPaymentsForDate(String date) {
        List<Payment> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT p.paymentID, p.cartID, p.paymentTypeID, p.employeeID"
                        + " FROM tbl_payment p JOIN tbl_cart c ON p.cartID = c.cartID WHERE DATE(c.cartDate) = ?",
                new String[]{date});
        try {
            if (cursor.moveToFirst()) do {
                list.add(new Payment(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PAYMENT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CART_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PAYMENT_TYPE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_ID))));
            } while (cursor.moveToNext());
        } finally { cursor.close(); db.close(); }
        return list;
    }

    public List<Dispense> getDispensesForDate(String date) {
        List<Dispense> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM tbl_dispenses WHERE DATE(dispenseDate) = ?", new String[]{date});
        if (cursor != null) {
            try { if (cursor.moveToFirst()) do { list.add(buildDispenseFromCursor(cursor)); } while (cursor.moveToNext()); }
            finally { cursor.close(); }
        }
        db.close();
        return list;
    }

    public List<Versement> getVersementsForDate(String date) {
        List<Versement> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM tbl_versement WHERE DATE(versementDateTime) = ?", new String[]{date});
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) do {
                    list.add(new Versement(
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VERSEMENT_ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADMIN_ID)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STATUS_ID)),
                            cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_EXPECTED_AMOUNT)),
                            cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_VERSED_AMOUNT)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PAYMENT_TYPE_ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VERSEMENT_PICTURE_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VERSEMENT_DATE_TIME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACTION_DATE))));
                } while (cursor.moveToNext());
            } finally { cursor.close(); }
        }
        db.close();
        return list;
    }

    public List<DeterioratedProductWithInstance> getDeterioratedProductsWithInstanceForDate(String date) {
        List<DeterioratedProductWithInstance> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM tbl_deteriorated_product_with_instance WHERE DATE(submissionDate) = ?",
                new String[]{date});
        if (cursor != null) {
            try { if (cursor.moveToFirst()) do { list.add(buildDWI(cursor)); } while (cursor.moveToNext()); }
            finally { cursor.close(); }
        }
        db.close();
        return list;
    }

    public List<DeterioratedProductWithoutInstance> getDeterioratedProductsWithoutInstanceForDate(String date) {
        List<DeterioratedProductWithoutInstance> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM tbl_deteriorated_product_without_instance WHERE DATE(submissionDate) = ?",
                new String[]{date});
        if (cursor != null) {
            try { if (cursor.moveToFirst()) do { list.add(buildDWOI(cursor)); } while (cursor.moveToNext()); }
            finally { cursor.close(); }
        }
        db.close();
        return list;
    }

    public List<PhysicalControle> getAllPhysicalControls() {
        List<PhysicalControle> list = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM tbl_physical_controle", null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CONTROLE_ID));
                    list.add(new PhysicalControle(id,
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTROLE_DATETIME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADMIN_ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_ID)),
                            getControleCasesForControl(id)));
                } while (cursor.moveToNext());
            } finally { cursor.close(); }
        }
        return list;
    }

    public List<ControleCase> getControleCasesForControl(int controleID) {
        List<ControleCase> list = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query(TABLE_CONTROLE_CASE, null,
                "controleID = ?", new String[]{String.valueOf(controleID)}, null, null, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) do {
                    ControleCase cc = new ControleCase();
                    cc.setControleCaseID(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CONTROLE_CASE_ID)));
                    cc.setControleID(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CONTROLE_ID)));
                    cc.setProductID(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)));
                    cc.setExpectedQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EXPECTED_QUANTITY)));
                    cc.setActualQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ACTUAL_QUANTITY)));
                    list.add(cc);
                } while (cursor.moveToNext());
            } finally { cursor.close(); }
        }
        return list;
    }

    public List<ControleCase> getControleCasesForStock(String productID) {
        List<ControleCase> list = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query(TABLE_CONTROLE_CASE, null,
                "productID = ?", new String[]{productID}, null, null, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) do {
                    ControleCase cc = new ControleCase();
                    cc.setControleID(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CONTROLE_ID)));
                    cc.setProductID(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)));
                    cc.setExpectedQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EXPECTED_QUANTITY)));
                    cc.setActualQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ACTUAL_QUANTITY)));
                    list.add(cc);
                } while (cursor.moveToNext());
            } finally { cursor.close(); }
        }
        return list;
    }

    public List<ControleCase> getLastControlDataForProduct(String productID) {
        List<ControleCase> list = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT * FROM tbl_controle_case WHERE productID = ? ORDER BY controleID DESC LIMIT 1",
                new String[]{productID});
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) do {
                    ControleCase cc = new ControleCase();
                    cc.setControleID(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CONTROLE_ID)));
                    cc.setProductID(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)));
                    cc.setExpectedQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EXPECTED_QUANTITY)));
                    cc.setActualQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ACTUAL_QUANTITY)));
                    list.add(cc);
                } while (cursor.moveToNext());
            } finally { cursor.close(); }
        }
        return list;
    }

    // =========================================================================
    // PRODUCT INFO / CART CALCULATIONS
    // =========================================================================
    public ProductInfo getProductInfoByInstance(String instanceID) {
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT P.productID, P.productName, P.productManufacture, P.product_photo_name,"
                        + " S.stockManDate, S.stockExpDate, S.stockID, S.stockQuantity, PP.price, PP.pricecaseID"
                        + " FROM tbl_product_instance PI"
                        + " JOIN tbl_stock S ON PI.stockID = S.stockID"
                        + " JOIN tbl_product P ON S.productID = P.productID"
                        + " JOIN tbl_product_price PP ON P.productID = PP.productID"
                        + " WHERE PI.instanceID = ? AND PP.pricecaseState = 'active'",
                new String[]{instanceID});
        if (cursor == null) return null;
        try {
            if (!cursor.moveToFirst()) return null;
            return new ProductInfo(
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STOCK_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STOCK_QUANTITY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STOCK_MAN_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STOCK_EXP_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_MANUFACTURE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_PHOTO_NAME)),
                    1, cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRICE_CASE_ID)));
        } finally { cursor.close(); }
    }

    public Product getProductObjectDetailsById(String productID) {
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT P.productID, P.productName, P.product_photo_name, PP.price, PP.pricecaseID"
                        + " FROM tbl_product P JOIN tbl_product_price PP ON P.productID = PP.productID"
                        + " WHERE P.productID = ? AND PP.pricecaseState = 'active'", new String[]{productID});
        if (cursor == null) return null;
        try {
            if (!cursor.moveToFirst()) return null;
            Product p = new Product();
            p.setProductID(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)));
            p.setProductName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME)));
            p.setProductPhotoName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_PHOTO_NAME)));
            p.setProductPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)));
            p.setPricecaseID(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRICE_CASE_ID)));
            return p;
        } finally { cursor.close(); }
    }

    public PhysicalControle getMostRecentPhysicalControl(String productID) {
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT * FROM tbl_physical_controle WHERE controleID IN"
                        + " (SELECT controleID FROM tbl_controle_case WHERE productID = ?)"
                        + " ORDER BY controleDateTime DESC LIMIT 1", new String[]{productID});
        if (cursor == null) return null;
        try {
            if (!cursor.moveToFirst()) return null;
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CONTROLE_ID));
            return new PhysicalControle(id,
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTROLE_DATETIME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADMIN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_ID)),
                    getControleCasesForControl(id));
        } finally { cursor.close(); }
    }

    public int getExpectedQuantityFromControl(String productID) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT SUM(c.expectedQuantity) as v FROM tbl_controle_case c"
                        + " JOIN tbl_physical_controle pc ON c.controleID = pc.controleID"
                        + " WHERE c.productID = ? ORDER BY pc.controleDateTime DESC LIMIT 1",
                new String[]{productID});
        try { return c.moveToFirst() ? c.getInt(0) : 0; } finally { c.close(); }
    }

    public int getActualQuantityFromControl(String productID) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT SUM(c.actualQuantity) as v FROM tbl_controle_case c"
                        + " JOIN tbl_physical_controle pc ON c.controleID = pc.controleID"
                        + " WHERE c.productID = ? ORDER BY pc.controleDateTime DESC LIMIT 1",
                new String[]{productID});
        try { return c.moveToFirst() ? c.getInt(0) : 0; } finally { c.close(); }
    }

    public double calculateTotalAmount(List<CartItem> items) {
        double total = 0;
        for (CartItem item : items) total += item.getUnitPrice() * item.getQuantity();
        return total;
    }

    public boolean deleteSale(String cartID) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            Cursor cursor = db.query(TABLE_CART_ITEMS_WITH_INSTANCE,
                    new String[]{COLUMN_INSTANCE_ID}, "cartID = ?", new String[]{cartID},
                    null, null, null);
            if (cursor != null) {
                try {
                    while (cursor.moveToNext()) {
                        String iid = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INSTANCE_ID));
                        if (iid != null) updateInstanceState(iid, CommonCssConstants.ACTIVE);
                    }
                } finally { cursor.close(); }
            }
            db.delete(TABLE_PAYMENT, "cartID = ?", new String[]{cartID});
            db.delete(TABLE_CART_ITEMS_WITH_INSTANCE, "cartID = ?", new String[]{cartID});
            db.delete(TABLE_CART_ITEMS_WITHOUT_INSTANCE, "cartID = ?", new String[]{cartID});
            db.delete(TABLE_CART, "cartID = ?", new String[]{cartID});
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace(); return false;
        } finally { db.endTransaction(); db.close(); }
    }

    public boolean deleteStock(String stockID) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(TABLE_STOCK, "stockID = ?", new String[]{stockID});
        db.close();
        return rows > 0;
    }

    public List<CartItem> getSalesSummary(String date) {
        List<CartItem> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c1 = db.rawQuery(
                "SELECT ciwoi.cartItemwithoutinstanceID, p.productName, ciwoi.quantityCart, pp.price"
                        + " FROM tbl_cart_itemswithoutinstance ciwoi"
                        + " JOIN tbl_product p ON ciwoi.productID = p.productID"
                        + " JOIN tbl_product_price pp ON ciwoi.pricecaseID = pp.pricecaseID"
                        + " JOIN tbl_cart c ON ciwoi.cartID = c.cartID WHERE DATE(c.cartDate) = ?",
                new String[]{date});
        try {
            if (c1.moveToFirst()) do {
                list.add(new CartItem(c1.getString(c1.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME)),
                        c1.getInt(c1.getColumnIndexOrThrow(COLUMN_QUANTITY_CART)),
                        c1.getDouble(c1.getColumnIndexOrThrow(COLUMN_PRICE)),
                        c1.getString(c1.getColumnIndexOrThrow(COLUMN_CART_ITEM_WITHOUT_INSTANCE_ID))));
            } while (c1.moveToNext());
        } finally { c1.close(); }
        Cursor c2 = db.rawQuery(
                "SELECT ciwi.cartItemID, p.productName, 1 AS quantityCart, pp.price"
                        + " FROM tbl_cart_itemswithinstance ciwi"
                        + " JOIN tbl_product_instance pi ON ciwi.instanceID = pi.instanceID"
                        + " JOIN tbl_stock s ON pi.stockID = s.stockID"
                        + " JOIN tbl_product p ON s.productID = p.productID"
                        + " JOIN tbl_product_price pp ON ciwi.pricecaseID = pp.pricecaseID"
                        + " JOIN tbl_cart c ON ciwi.cartID = c.cartID WHERE DATE(c.cartDate) = ?",
                new String[]{date});
        try {
            if (c2.moveToFirst()) do {
                list.add(new CartItem(c2.getString(c2.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME)),
                        c2.getInt(c2.getColumnIndexOrThrow(COLUMN_QUANTITY_CART)),
                        c2.getDouble(c2.getColumnIndexOrThrow(COLUMN_PRICE)),
                        c2.getString(c2.getColumnIndexOrThrow(COLUMN_CART_ITEM_ID))));
            } while (c2.moveToNext());
        } finally { c2.close(); db.close(); }
        return list;
    }

    // =========================================================================
    // FINANCIAL AGGREGATIONS
    // =========================================================================
    public double getTotalSales(String date) {
        SQLiteDatabase db = getReadableDatabase();
        double total = 0;
        Cursor c1 = db.rawQuery(
                "SELECT SUM(pp.price * ciw.quantityCart) FROM tbl_cart_itemswithoutinstance ciw"
                        + " JOIN tbl_product_price pp ON ciw.pricecaseID = pp.pricecaseID"
                        + " JOIN tbl_cart c ON ciw.cartID = c.cartID WHERE DATE(c.cartDate) = ?",
                new String[]{date});
        if (c1 != null) { try { if (c1.moveToFirst()) total += c1.getDouble(0); } finally { c1.close(); } }
        Cursor c2 = db.rawQuery(
                "SELECT SUM(pp.price) FROM tbl_cart_itemswithinstance ci"
                        + " JOIN tbl_product_price pp ON ci.pricecaseID = pp.pricecaseID"
                        + " JOIN tbl_cart c ON ci.cartID = c.cartID WHERE DATE(c.cartDate) = ?",
                new String[]{date});
        if (c2 != null) { try { if (c2.moveToFirst()) total += c2.getDouble(0); } finally { c2.close(); } }
        db.close();
        return total;
    }

    public double getTotalSalePrice(String date) { return getTotalSales(date); }

    public double getTotalSalesT() {
        SQLiteDatabase db = getReadableDatabase();
        double total = 0;
        Cursor c1 = db.rawQuery(
                "SELECT SUM(ciwoi.quantityCart * pp.price) FROM tbl_cart_itemswithoutinstance ciwoi"
                        + " JOIN tbl_cart c ON ciwoi.cartID = c.cartID"
                        + " JOIN tbl_product_price pp ON ciwoi.pricecaseID = pp.pricecaseID", null);
        if (c1 != null) { try { if (c1.moveToFirst()) total += c1.getDouble(0); } finally { c1.close(); } }
        Cursor c2 = db.rawQuery(
                "SELECT SUM(pp.price) FROM tbl_cart_itemswithinstance ciwi"
                        + " JOIN tbl_cart c ON ciwi.cartID = c.cartID"
                        + " JOIN tbl_product_price pp ON ciwi.pricecaseID = pp.pricecaseID", null);
        if (c2 != null) { try { if (c2.moveToFirst()) total += c2.getDouble(0); } finally { c2.close(); } }
        db.close();
        return total;
    }

    public double getTotalSalesSince(String date) {
        SQLiteDatabase db = getReadableDatabase();
        double total = 0;
        Cursor c1 = db.rawQuery(
                "SELECT SUM(ciwoi.quantityCart * pp.price) FROM tbl_cart_itemswithoutinstance ciwoi"
                        + " JOIN tbl_cart c ON ciwoi.cartID = c.cartID"
                        + " JOIN tbl_product_price pp ON ciwoi.pricecaseID = pp.pricecaseID WHERE c.cartDate >= ?",
                new String[]{date});
        if (c1 != null) { try { if (c1.moveToFirst()) total += c1.getDouble(0); } finally { c1.close(); } }
        Cursor c2 = db.rawQuery(
                "SELECT SUM(pp.price) FROM tbl_cart_itemswithinstance ciwi"
                        + " JOIN tbl_cart c ON ciwi.cartID = c.cartID"
                        + " JOIN tbl_product_price pp ON ciwi.pricecaseID = pp.pricecaseID WHERE c.cartDate >= ?",
                new String[]{date});
        if (c2 != null) { try { if (c2.moveToFirst()) total += c2.getDouble(0); } finally { c2.close(); } }
        db.close();
        return total;
    }

    public double getTotalPurchases() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(totalAmountUsed) FROM tbl_stock", null);
        try { return (c != null && c.moveToFirst()) ? c.getDouble(0) : 0; }
        finally { if (c != null) c.close(); db.close(); }
    }

    public double getTotalPurchasePrice(String date) {
        SQLiteDatabase db = getReadableDatabase();
        double total = purchaseWithInstances(db, date) + purchaseWithoutInstancesFIFO(db, date);
        db.close();
        return total;
    }

    public double getTotalPurchasesSince(String date) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT SUM(totalAmountUsed) FROM tbl_stock WHERE stockDateTime >= ? AND statusID = 3",
                new String[]{date});
        try { return (c != null && c.moveToFirst()) ? c.getDouble(0) : 0; }
        finally { if (c != null) c.close(); db.close(); }
    }

    public double getTotalExpenses() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(amount) FROM tbl_dispenses", null);
        try { return (c != null && c.moveToFirst()) ? c.getDouble(0) : 0; }
        finally { if (c != null) c.close(); db.close(); }
    }

    public double getTotalExpenses(String date) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(amount) FROM tbl_dispenses WHERE DATE(dispenseDate) = ?",
                new String[]{date});
        try { return (c != null && c.moveToFirst()) ? c.getDouble(0) : 0; }
        finally { if (c != null) c.close(); db.close(); }
    }

    public double getTotalExpensesSince(String date) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(amount) FROM tbl_dispenses WHERE dispenseDate >= ?",
                new String[]{date});
        try { return (c != null && c.moveToFirst()) ? c.getDouble(0) : 0; }
        finally { if (c != null) c.close(); db.close(); }
    }

    public double getAmountInExpenses(String date) { return getTotalExpenses(date); }

    public double getTotalPreviousDeposits() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(versedAmount) FROM tbl_versement", null);
        try { return (c != null && c.moveToFirst()) ? c.getDouble(0) : 0; }
        finally { if (c != null) c.close(); db.close(); }
    }

    public double getVersementDeposit(String date) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT SUM(versedAmount) FROM tbl_versement WHERE DATE(versementDateTime) = ?",
                new String[]{date});
        if (c == null) return 0;
        try { return c.moveToFirst() ? c.getDouble(0) : 0; } finally { c.close(); }
    }

    public double getTotalDeposits(String date) { return getVersementDeposit(date); }

    public double getTotalDepositsSince(String date) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(versedAmount) FROM tbl_versement WHERE versementDateTime >= ?",
                new String[]{date});
        try { return (c != null && c.moveToFirst()) ? c.getDouble(0) : 0; }
        finally { if (c != null) c.close(); db.close(); }
    }

    public double getAmountInStock(String date) {
        return (getTotalSalePrice(date) - getAmountInExpenses(date)) - getVersementDeposit(date);
    }

    public int getTotalStocksMade(String date) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT COUNT(*) FROM tbl_stock WHERE DATE(stockDateTime) = ?", new String[]{date});
        if (c == null) return 0;
        try { return c.moveToFirst() ? c.getInt(0) : 0; } finally { c.close(); }
    }

    public double calculateAmountAvailable() {
        return getTotalSalesT() - getTotalPurchases() - getTotalExpenses() - getTotalPreviousDeposits();
    }

    public double calculateAmountAvailableSinceLastControl() {
        String last = getLastControlDate();
        if (last == null) return calculateAmountAvailable();
        return getTotalSalesSince(last) - getTotalExpensesSince(last)
                - getTotalDepositsSince(last) - getTotalPurchasesSince(last);
    }

    public double calculateAmountInStockPerDay(String date) {
        return getTotalSalePrice(date) - getTotalPurchasePrice(date)
                - getTotalExpenses(date) + getTotalDeposits(date);
    }

    public Map<String, Double> getSalesByPaymentType(String date) {
        Map<String, Double> map = new HashMap<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT tp.paymentMethod, SUM(cartSub.cartTotal / pc.paymentCount) AS amount"
                        + " FROM (SELECT c.cartID, COALESCE(sw.totalWithout,0)+COALESCE(si.totalWith,0) AS cartTotal"
                        + "   FROM tbl_cart c"
                        + "   LEFT JOIN (SELECT c1.cartID, SUM(ci.quantityCart*pp.price) AS totalWithout"
                        + "       FROM tbl_cart c1 JOIN tbl_cart_itemswithoutinstance ci ON c1.cartID=ci.cartID"
                        + "       JOIN tbl_product_price pp ON ci.pricecaseID=pp.pricecaseID"
                        + "       WHERE DATE(c1.cartDate)=? GROUP BY c1.cartID) sw ON c.cartID=sw.cartID"
                        + "   LEFT JOIN (SELECT c2.cartID, SUM(pp.price) AS totalWith"
                        + "       FROM tbl_cart c2 JOIN tbl_cart_itemswithinstance ci2 ON c2.cartID=ci2.cartID"
                        + "       JOIN tbl_product_price pp ON ci2.pricecaseID=pp.pricecaseID"
                        + "       WHERE DATE(c2.cartDate)=? GROUP BY c2.cartID) si ON c.cartID=si.cartID"
                        + "   WHERE DATE(c.cartDate)=?) cartSub"
                        + " JOIN tbl_payment p ON cartSub.cartID=p.cartID"
                        + " JOIN (SELECT cartID, COUNT(*) AS paymentCount FROM tbl_payment GROUP BY cartID) pc ON p.cartID=pc.cartID"
                        + " JOIN tbl_typepayment tp ON p.paymentTypeID=tp.paymentTypeID GROUP BY tp.paymentMethod",
                new String[]{date, date, date});
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) do {
                    map.put(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PAYMENT_METHOD)),
                            cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT)));
                } while (cursor.moveToNext());
            } finally { cursor.close(); }
        }
        db.close();
        return map;
    }

    private double purchaseWithInstances(SQLiteDatabase db, String date) {
        Cursor c = db.rawQuery(
                "SELECT s.totalAmountUsed, s.stockQuantity FROM tbl_cart_itemswithinstance ciwi"
                        + " JOIN tbl_cart c ON ciwi.cartID=c.cartID"
                        + " JOIN tbl_product_instance pi ON ciwi.instanceID=pi.instanceID"
                        + " JOIN tbl_stock s ON pi.stockID=s.stockID WHERE DATE(c.cartDate)=?",
                new String[]{date});
        double total = 0;
        if (c != null) {
            try { if (c.moveToFirst()) do {
                total += c.getDouble(0) / c.getInt(1);
            } while (c.moveToNext()); } finally { c.close(); }
        }
        return total;
    }

    private double purchaseWithoutInstancesFIFO(SQLiteDatabase db, String date) {
        Cursor c = db.rawQuery(
                "SELECT ciwoi.productID, SUM(ciwoi.quantityCart) AS totalQty"
                        + " FROM tbl_cart_itemswithoutinstance ciwoi JOIN tbl_cart c ON ciwoi.cartID=c.cartID"
                        + " WHERE DATE(c.cartDate)=? GROUP BY ciwoi.productID", new String[]{date});
        double total = 0;
        if (c != null) {
            try {
                if (c.moveToFirst()) do {
                    String pid = c.getString(0);
                    int remaining = c.getInt(1);
                    Cursor c2 = db.rawQuery(
                            "SELECT stockQuantity, totalAmountUsed FROM tbl_stock"
                                    + " WHERE productID=? ORDER BY stockDateTime ASC", new String[]{pid});
                    if (c2 != null) {
                        try {
                            if (c2.moveToFirst()) do {
                                int qty = c2.getInt(0);
                                double unit = c2.getDouble(1) / qty;
                                int take = Math.min(qty, remaining);
                                total += take * unit;
                                remaining -= take;
                                if (remaining <= 0) break;
                            } while (c2.moveToNext());
                        } finally { c2.close(); }
                    }
                } while (c.moveToNext());
            } finally { c.close(); }
        }
        return total;
    }

    // =========================================================================
    // QUANTITY / INSTANCE COUNTERS
    // =========================================================================
    public double getRemainingQuantityWithoutInstances(String productID) {
        SQLiteDatabase db = getReadableDatabase();
        double stock = 0, sold = 0, det = 0;
        Cursor c1 = db.rawQuery("SELECT IFNULL(SUM(stockQuantity),0) FROM tbl_stock WHERE productID=? AND statusID=3", new String[]{productID});
        try { if (c1.moveToFirst()) stock = c1.getDouble(0); } finally { c1.close(); }
        Cursor c2 = db.rawQuery("SELECT IFNULL(SUM(quantityCart),0) FROM tbl_cart_itemswithoutinstance WHERE productID=?", new String[]{productID});
        try { if (c2.moveToFirst()) sold = c2.getDouble(0); } finally { c2.close(); }
        Cursor c3 = db.rawQuery("SELECT IFNULL(SUM(quantity),0) FROM tbl_deteriorated_product_without_instance WHERE productID=? AND actionTaken=1", new String[]{productID});
        try { if (c3.moveToFirst()) det = c3.getDouble(0); } finally { c3.close(); db.close(); }
        return stock - sold - det;
    }

    public double getRemainingQuantityForProduct(String productID) {
        return getRemainingQuantityWithoutInstances(productID);
    }

    public double getRemainingQuantityForStockWithoutInstances(String productID, String stockID) {
        SQLiteDatabase db = getReadableDatabase();
        LinkedHashMap<String, Double> stockMap = new LinkedHashMap<>();
        Cursor c1 = db.rawQuery(
                "SELECT stockID, stockQuantity FROM tbl_stock WHERE productID=? ORDER BY stockDateTime ASC",
                new String[]{productID});
        try {
            if (c1 != null && c1.moveToFirst()) do {
                stockMap.put(c1.getString(0), c1.getDouble(1));
            } while (c1.moveToNext());
        } finally { if (c1 != null) c1.close(); }
        Cursor c2 = db.rawQuery(
                "SELECT quantityCart FROM tbl_cart_itemswithoutinstance WHERE productID=? ORDER BY cartItemwithoutinstanceID",
                new String[]{productID});
        try {
            if (c2 != null && c2.moveToFirst()) do {
                double sold = c2.getDouble(0);
                List<String> keys = new ArrayList<>(stockMap.keySet());
                for (String key : keys) {
                    if (sold <= 0) break;
                    double qty = stockMap.get(key);
                    double deduct = Math.min(qty, sold);
                    stockMap.put(key, qty - deduct);
                    sold -= deduct;
                }
            } while (c2.moveToNext());
        } finally { if (c2 != null) c2.close(); }
        db.close();
        return stockMap.getOrDefault(stockID, 0.0);
    }

    public double getRemainingQuantityForStock(String productID, String stockID) {
        return getRemainingQuantityForStockWithoutInstances(productID, stockID);
    }

    public int getRemainingInstancesForProduct(String productID) {
        if (productID == null) return 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT COUNT(*) FROM tbl_product_instance pi JOIN tbl_stock s ON pi.stockID=s.stockID"
                        + " WHERE s.productID=? AND pi.instanceState=?",
                new String[]{productID, CommonCssConstants.ACTIVE});
        try { return c != null && c.moveToFirst() ? c.getInt(0) : 0; }
        finally { if (c != null) c.close(); db.close(); }
    }

    public int getRemainingInstancesForStock(String stockID) {
        if (stockID == null) return 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT COUNT(*) FROM tbl_product_instance WHERE stockID=? AND instanceState=?",
                new String[]{stockID, CommonCssConstants.ACTIVE});
        try { return (c != null && c.moveToFirst()) ? c.getInt(0) : 0; }
        finally { if (c != null) c.close(); db.close(); }
    }

    public int getTotalInstancesForStock(String stockID) {
        if (stockID == null) return 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT COUNT(*) FROM tbl_product_instance WHERE stockID=?", new String[]{stockID});
        try { return c != null && c.moveToFirst() ? c.getInt(0) : 0; }
        finally { if (c != null) c.close(); db.close(); }
    }

    public double getTotalSoldWithoutInstances(String productID) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT IFNULL(SUM(quantityCart),0) FROM tbl_cart_itemswithoutinstance WHERE productID=?",
                new String[]{productID});
        try { return c.moveToFirst() ? c.getDouble(0) : 0; } finally { c.close(); }
    }

    public double getTotalDeterioratedWithoutInstances(String productID) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT IFNULL(SUM(quantity),0) FROM tbl_deteriorated_product_without_instance"
                        + " WHERE productID=? AND actionTaken=1", new String[]{productID});
        try { return c.moveToFirst() ? c.getDouble(0) : 0; } finally { c.close(); }
    }

    // =========================================================================
    // CLOSURE HELPERS
    // =========================================================================
    public ClosureData prepareClosureData(String date) {
        return new ClosureData(UUID.randomUUID().toString(), date,
                getTotalSalePrice(date), getAmountInStock(date), 1,
                getLoggedInEmployeeID(), getTotalStocksMade(date),
                getAmountInExpenses(date), getVersementDeposit(date));
    }

    public boolean isClosureDataExistsForToday() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM tbl_closure WHERE DATE(closureDate) = ?",
                new String[]{new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date())});
        try { return c.moveToFirst() && c.getInt(0) > 0; }
        finally { c.close(); db.close(); }
    }

    public boolean isClosureDataForToday() {
        Cursor c = null;
        try {
            c = getReadableDatabase().rawQuery(
                    "SELECT 1 FROM tbl_closure WHERE DATE(closureDate) = DATE('now') LIMIT 1", null);
            return c.moveToFirst();
        } catch (Exception e) { return false; }
        finally { if (c != null) c.close(); }
    }

    public boolean isDateInClosureTable(String date) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT COUNT(*) FROM tbl_closure WHERE DATE(closureDate) = ?", new String[]{date});
        try { return c.moveToFirst() && c.getInt(0) > 0; } finally { c.close(); }
    }

    // =========================================================================
    // DATE / CONTROL DATE HELPERS
    // =========================================================================
    public String getLastControlDate() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT MAX(controleDateTime) FROM tbl_physical_controle", null);
        try { return c.moveToFirst() ? c.getString(0) : null; }
        finally { c.close(); db.close(); }
    }

    public List<String> getDateRangeFromLastControlToToday() {
        List<String> dates = new ArrayList<>();
        String lastControl = getLastControlDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        if (lastControl == null || lastControl.isEmpty()) {
            dates.add(sdf.format(cal.getTime())); return dates;
        }
        try {
            cal.setTime(sdf.parse(lastControl));
            while (!cal.getTime().after(new Date())) {
                dates.add(sdf.format(cal.getTime()));
                cal.add(Calendar.DAY_OF_YEAR, 1);
            }
        } catch (ParseException e) { e.printStackTrace(); }
        return dates;
    }

    // =========================================================================
    // LOW STOCK
    // =========================================================================
    public Cursor getLowStockProducts() {
        return getReadableDatabase().rawQuery(
                "SELECT p.productID, p.productName, p.product_seuil_stock,"
                        + " IFNULL(SUM(s.stockQuantity),0)"
                        + " - (IFNULL(SUM(cwi.quantityCart),0)"
                        + "   + IFNULL(COUNT(ciwi.cartItemID),0)"
                        + "   + IFNULL(SUM(dpi.quantity),0)"
                        + "   + IFNULL(SUM(dpwi.quantity),0)) AS availableStock"
                        + " FROM tbl_product p"
                        + " LEFT JOIN tbl_stock s ON p.productID=s.productID"
                        + " LEFT JOIN tbl_cart_itemswithoutinstance cwi ON p.productID=cwi.productID"
                        + " LEFT JOIN tbl_cart_itemswithinstance ciwi ON ciwi.instanceID IN"
                        + "   (SELECT pi.instanceID FROM tbl_product_instance pi WHERE pi.stockID=s.stockID)"
                        + " LEFT JOIN tbl_deteriorated_product_with_instance dpi ON dpi.instanceID=ciwi.instanceID AND dpi.actionTaken=1"
                        + " LEFT JOIN tbl_deteriorated_product_without_instance dpwi ON dpwi.productID=p.productID AND dpwi.actionTaken=1"
                        + " GROUP BY p.productID HAVING availableStock < p.product_seuil_stock"
                        + " ORDER BY p.productName COLLATE NOCASE", null);
    }

    // =========================================================================
    // PRIVATE CURSOR-TO-OBJECT BUILDERS
    // =========================================================================
    private Product buildProductFromCursor(Cursor cursor) {
        Product p = new Product();
        p.setProductID(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)));
        p.setProductName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME)));
        p.setProductManufacture(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_MANUFACTURE)));
        p.setManufactureAddress(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MANUFACTURE_ADDRESS)));
        p.setProductPhotoName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_PHOTO_NAME)));
        p.setProductAddDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ADD_DATE)));
        p.setProductSeuilStock(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_SEUIL_STOCK)));
        p.setAdminID(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADMIN_ID)));
        p.setTypeProductID(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TYPE_PRODUCT_ID)));
        p.setSubSubAccountID(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUB_SUB_ACCOUNT_ID)));
        p.setUniteID(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UNITE_ID)));
        p.setIsActiveToInstances(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_ACTIVE_TO_INSTANCES)));
        p.setIsActiveToDecimalQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_ACTIVE_TO_DECIMAL_QUANTITY)));
        return p;
    }

    private Stock buildStockFromCursor(Cursor cursor) {
        Stock s = new Stock();
        s.setStockID(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STOCK_ID)));
        s.setStockDateTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STOCK_DATE_TIME)));
        s.setStockQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STOCK_QUANTITY)));
        s.setTotalAmountUsed(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_AMOUNT_USED)));
        s.setProductID(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)));
        s.setStockManDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STOCK_MAN_DATE)));
        s.setStockExpDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STOCK_EXP_DATE)));
        s.setSupplierName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUPPLIER_NAME)));
        s.setSupplierContact(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUPPLIER_CONTACT)));
        s.setFactureNumber(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FACTURE_NUMBER)));
        s.setFactureImageName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FACTURE_IMAGE_NAME)));
        s.setPaymentTypeID(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PAYMENT_TYPE_ID)));
        s.setEmployeeID(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_ID)));
        s.setStatusID(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STATUS_ID)));
        s.setUploadStatus(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_UPLOAD_STATUS)));
        return s;
    }

    private Dispense buildDispenseFromCursor(Cursor cursor) {
        return new Dispense(
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DISPENSE_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DISPENSE_DATE)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TYPE_DISPENSE_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_ID)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STATUS_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PICTURE_NAME)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PAYMENT_TYPE_ID)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_UPLOAD_STATUS)));
    }

    /** Deteriorated WITH instance from cursor */
    private DeterioratedProductWithInstance buildDWI(Cursor cursor) {
        return new DeterioratedProductWithInstance(
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DETERIORATED_PRODUCT_WITH_INSTANCE_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INSTANCE_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DETERIORATION_DATE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REASON)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DETECTED_BY_EMPLOYEE_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PICTURE_NAME)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ACTION_TAKEN)) == 1,
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACTION_DATE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUBMISSION_DATE)),
                null,
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_UPLOAD_STATUS)));
    }

    /** Deteriorated WITHOUT instance from cursor */
    private DeterioratedProductWithoutInstance buildDWOI(Cursor cursor) {
        return new DeterioratedProductWithoutInstance(
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DETERIORATED_PRODUCT_WITHOUT_INSTANCE_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DETERIORATION_DATE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REASON)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DETECTED_BY_EMPLOYEE_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PICTURE_NAME)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ACTION_TAKEN)) > 0,
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACTION_DATE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUBMISSION_DATE)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_UPLOAD_STATUS)));
    }
}
