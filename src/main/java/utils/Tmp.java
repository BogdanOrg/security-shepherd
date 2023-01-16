package utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Tmp extends MainActivity
        implements OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    TabHost th;
    Button Login;
    EditText username;
    TextView loginTitle;
    EditText password;
    EditText key;
    String dbPass = "37e44d547f20a9f3ca9ac7d625486b7b";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        super.onCreate(savedInstanceState);
        setContentView(R.layout.csi_layout);
        th = (TabHost) findViewById(R.id.tabhost);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(
                        this,
                        drawer,
                        toolbar,
                        R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        populateTable(this, "dbPass");
        generateKey(this, "dbPass");
        referenceXML();
        th.setup();

        TabSpec specs = th.newTabSpec("tag1");
        specs.setContent(R.id.tab1);
        specs.setIndicator("Login");
        th.addTab(specs);

        specs = th.newTabSpec("tag2");
        specs.setContent(R.id.tab2);
        specs.setIndicator("Key");
        th.addTab(specs);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void referenceXML() {
        // TODO Auto-generated method stub
        Login = (Button) findViewById(R.id.bLogin);
        // Login.setFilterTouchesWhenObscured(true);
        username = (EditText) findViewById(R.id.etName);
        password = (EditText) findViewById(R.id.etPass);
        key = (EditText) findViewById(R.id.etKey);
        Login.setOnClickListener(this);
        loginTitle = (TextView) findViewById(R.id.tvTitle);

        th = (TabHost) findViewById(R.id.tabhost);
    }

    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case (R.id.bLogin):
                String CheckName = username.getText().toString();
                String CheckPass = password.getText().toString();

                try {
                    if (login(CheckName, CheckPass) == true) {
                        outputKey(this, dbPass);
                        Toast login = Toast.makeText(com.mobshep.mobileshepherd.CSInjection.this, "Logged in!", Toast.LENGTH_LONG);
                        login.show();
                    }
                } catch (IOException e1) {
                    Toast error = Toast.makeText(com.mobshep.mobileshepherd.CSInjection.this, "An error occurred!", Toast.LENGTH_LONG);
                    error.show();
                }

                try {
                    if (login(CheckName, CheckPass) == false) {
                        Toast invalid =
                                Toast.makeText(com.mobshep.mobileshepherd.CSInjection.this, "Invalid Credentials!", Toast.LENGTH_SHORT);
                        invalid.show();
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (CheckName.contentEquals("")
                        || CheckPass.contentEquals("")
                        || CheckPass.contentEquals("A3B922DF010PQSI827")) {
                    Toast empty =
                            Toast.makeText(com.mobshep.mobileshepherd.CSInjection.this, "Empty Fields Detected.", Toast.LENGTH_SHORT);
                    empty.show();
                }
        }
    }

    private boolean login(String username, String password) throws IOException {
        try {
            try {
                String dbPath = this.getDatabasePath("encrypted.db").getPath();

                SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbPath, dbPass, null);

                String query =
                        ("SELECT * FROM ENCRYPTED WHERE memName='"
                                + username
                                + "' AND memPass = '"
                                + password
                                + "';");

                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null) {
                    if (cursor.getCount() <= 0) {
                        cursor.close();
                        return false;
                    }
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                Toast error = Toast.makeText(com.mobshep.mobileshepherd.CSInjection.this, "An error occurred.", Toast.LENGTH_LONG);
                error.show();
                key.getText().clear();
                key.setHint("The key is only shown to authenticated users.");
                return false;
            }

        } catch (SQLiteException e) {
            Toast error =
                    Toast.makeText(com.mobshep.mobileshepherd.CSInjection.this, "An database error occurred.", Toast.LENGTH_LONG);
            error.show();
        }

        return true;
    }

    public void populateTable(Context context, String password) {
        try {
            try {
                SQLiteDatabase.loadLibs(context);

                String dbPath = context.getDatabasePath("encrypted.db").getPath();

                File dbPathFile = new File(dbPath);
                if (!dbPathFile.exists()) {
                    dbPathFile.getParentFile().mkdirs();
                }

                SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbPath, dbPass, null);

                db.execSQL("DROP TABLE IF EXISTS encrypted");
                db.execSQL(
                        "CREATE TABLE encrypted(memID INTEGER PRIMARY KEY AUTOINCREMENT, memName TEXT, memAge"
                                + " INTEGER, memPass VARCHAR)");

                db.execSQL("INSERT INTO encrypted VALUES( 1,'Admin',20,'A3B922DF010PQSI827')");

            } catch (Exception e) {
                // TODO Auto-generated catch block
                Toast error = Toast.makeText(com.mobshep.mobileshepherd.CSInjection.this, "An error occurred.", Toast.LENGTH_LONG);
                error.show();
            }

        } catch (SQLiteException e) {
            Toast error =
                    Toast.makeText(com.mobshep.mobileshepherd.CSInjection.this, "An database error occurred.", Toast.LENGTH_LONG);
            error.show();
        }
    }

    public void outputKey(Context context, String password) {
        SQLiteDatabase.loadLibs(context);

        String dbPath = context.getDatabasePath("key0.db").getPath();

        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbPath, dbPass, null);

        String query = ("SELECT * FROM key0;");

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {

            try {
                if (cursor.moveToFirst()) {
                    key.setText(cursor.getString(0));
                }
            } finally {
                cursor.close();
            }
        }
    }

    public void generateKey(Context context, String password) {
        try {
            try {
                SQLiteDatabase.loadLibs(context);

                String dbPath = context.getDatabasePath("key0.db").getPath();

                File dbPathFile = new File(dbPath);
                if (!dbPathFile.exists()) {
                    dbPathFile.getParentFile().mkdirs();
                }

                SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbPath, dbPass, null);

                db.execSQL("DROP TABLE IF EXISTS key0");
                db.execSQL("CREATE TABLE key0(key VARCHAR)");

                db.execSQL("INSERT INTO key0 VALUES('The Key is VolcanicEruptionsAbruptInterruptions.')");

            } catch (Exception e) {
                // TODO Auto-generated catch block
                Toast error = Toast.makeText(com.mobshep.mobileshepherd.CSInjection.this, "An error occurred.", Toast.LENGTH_LONG);
                error.show();
            }

        } catch (SQLiteException e) {
            Toast error =
                    Toast.makeText(com.mobshep.mobileshepherd.CSInjection.this, "An database error occurred.", Toast.LENGTH_LONG);
            error.show();
        }
    }
}
