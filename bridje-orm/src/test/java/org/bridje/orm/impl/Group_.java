
package org.bridje.orm.impl;

import javax.annotation.Generated;
import org.bridje.orm.Table;
import org.bridje.orm.Column;

@Generated(value = "bridje-orm")
public final class Group_ extends Table<Group>
{
    public static final Group_ table = new Group_();

    public static final Column<Group, Long> id = new Column<>(Group_.table, "id", Long.class);

    public static final Column<Group, String> name = new Column<>(Group_.table, "name", String.class);

    private Group_()
    {
        super(Group.class);
    }

}
