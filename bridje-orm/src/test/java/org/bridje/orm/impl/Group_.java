
package org.bridje.orm.impl;

import javax.annotation.Generated;
import org.bridje.orm.*;

@Generated(value = "bridje-orm")
public final class Group_ extends Table<Group>
{
    public static final Group_ table = new Group_();

    public static final NumberColumn<Group, Long> id = new NumberColumn<>(Group_.table, "id", Long.class);

    public static final StringColumn<Group> name = new StringColumn<>(Group_.table, "name");

    private Group_()
    {
        super(Group.class);
    }

}
