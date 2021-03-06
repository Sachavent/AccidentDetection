package android.mission.accidentdetection.Helper;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Annick on 05/02/2017.
 */

public class ActionEmergencyContactHelper {

    private Context context;

    public ActionEmergencyContactHelper(Context context) {
        this.context = context;
    }

    /**Saving Emergency Contact in the SQLite Database */
    public Uri insertEmergencyContact (String name, String telephone) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelper.COL_2, name);
        contentValues.put(DBHelper.COL_3, telephone);

        Uri result = context.getApplicationContext().getContentResolver().insert(android.mission.accidentdetection.Provider.ContentProvider.CONTENT_URL, contentValues);
        return result;
    }

    /**Getting all emergency contacts */
    public HashMap<String,String> getAllEmergencyContacts() {
        HashMap<String, String> contactsAllreadyAdded = new HashMap<>();
        Cursor cursor = context.getApplicationContext().getContentResolver().query(Uri.parse("content://android.mission.accidentdetection/elements/"), null, null, null, null);
        while (cursor.moveToNext()) {
            contactsAllreadyAdded.put(cursor.getString(1), cursor.getString(2));
        }
        return contactsAllreadyAdded;
    }

    /** Deleting an emergency contact */
    public void deletingEmergencyContact(String name) {
        Log.d("delete", "delete: "+name);

        ContentResolver contentResolver = context.getApplicationContext().getContentResolver();

        String where = DBHelper.COL_2+"=?";
        String[] whereParam = {name};

        contentResolver.delete(Uri.parse("content://android.mission.accidentdetection/elements/"),where,whereParam);
    }
}
