
package org.bridje.orm.impl;

import javax.annotation.Generated;
import org.bridje.orm.*;

@Generated(value = "bridje-orm")
public final class User_ extends Table<User>
{
    public static final User_ table = new User_();

    public static final NumberColumn<User, Long> id = new NumberColumn<>(User_.table, "id", Long.class);

    public static final StringColumn<User> name = new StringColumn<>(User_.table, "name");

    public static final Column<User, Character> clasif = new Column<>(User_.table, "clasif", Character.class);

    public static final Column<User, Boolean> enable = new Column<>(User_.table, "enable", Boolean.class);

    public static final NumberColumn<User, Byte> counts = new NumberColumn<>(User_.table, "counts", Byte.class);

    public static final NumberColumn<User, Short> age = new NumberColumn<>(User_.table, "age", Short.class);

    public static final NumberColumn<User, Integer> mins = new NumberColumn<>(User_.table, "mins", Integer.class);

    public static final NumberColumn<User, Long> year = new NumberColumn<>(User_.table, "year", Long.class);

    public static final NumberColumn<User, Float> credit = new NumberColumn<>(User_.table, "credit", Float.class);

    public static final NumberColumn<User, Double> money = new NumberColumn<>(User_.table, "money", Double.class);

    public static final Column<User, java.util.Date> brithday = new Column<>(User_.table, "brithday", java.util.Date.class);

    public static final Column<User, java.sql.Date> updated = new Column<>(User_.table, "updated", java.sql.Date.class);

    public static final Column<User, java.sql.Timestamp> created = new Column<>(User_.table, "created", java.sql.Timestamp.class);

    public static final Column<User, java.sql.Time> hour = new Column<>(User_.table, "hour", java.sql.Time.class);

    public static final RelationColumn<User, org.bridje.orm.impl.Group> group = new RelationColumn<>(User_.table, Group_.table, "group");

    private User_()
    {
        super(User.class);
    }

}
