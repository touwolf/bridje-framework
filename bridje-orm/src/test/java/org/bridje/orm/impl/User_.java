
package org.bridje.orm.impl;

import javax.annotation.Generated;
import org.bridje.orm.Table;
import org.bridje.orm.Column;

@Generated(value = "bridje-orm")
public final class User_ extends Table<User>
{
    public static final User_ table = new User_();

    public static final Column<User, Long> id = new Column<>(User_.table, "id", Long.class);

    public static final Column<User, String> name = new Column<>(User_.table, "name", String.class);

    public static final Column<User, Character> clasif = new Column<>(User_.table, "clasif", Character.class);

    public static final Column<User, Boolean> enable = new Column<>(User_.table, "enable", Boolean.class);

    public static final Column<User, Byte> counts = new Column<>(User_.table, "counts", Byte.class);

    public static final Column<User, Short> age = new Column<>(User_.table, "age", Short.class);

    public static final Column<User, Integer> mins = new Column<>(User_.table, "mins", Integer.class);

    public static final Column<User, Long> year = new Column<>(User_.table, "year", Long.class);

    public static final Column<User, Float> credit = new Column<>(User_.table, "credit", Float.class);

    public static final Column<User, Double> money = new Column<>(User_.table, "money", Double.class);

    public static final Column<User, java.util.Date> brithday = new Column<>(User_.table, "brithday", java.util.Date.class);

    public static final Column<User, java.sql.Date> updated = new Column<>(User_.table, "updated", java.sql.Date.class);

    public static final Column<User, java.sql.Timestamp> created = new Column<>(User_.table, "created", java.sql.Timestamp.class);

    public static final Column<User, java.sql.Time> hour = new Column<>(User_.table, "hour", java.sql.Time.class);

    public static final Column<User, org.bridje.orm.impl.Group> group = new Column<>(User_.table, "group", org.bridje.orm.impl.Group.class);

    private User_()
    {
        super(User.class);
    }

}
