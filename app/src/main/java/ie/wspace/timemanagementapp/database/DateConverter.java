package ie.wspace.timemanagementapp.database;

import androidx.room.TypeConverter;

import java.util.Date;

/*
 * DateConverter
 * Converts Dates to Longs or Longs to Dates
 */
public class DateConverter {
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
