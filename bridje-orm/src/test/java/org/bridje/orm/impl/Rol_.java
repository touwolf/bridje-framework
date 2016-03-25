
package org.bridje.orm.impl;

import javax.annotation.Generated;
import org.bridje.orm.*;

@Generated(value = "bridje-orm")
public final class Rol_ extends Table<Rol>
{
    public static final Rol_ table = new Rol_();

    public static final NumberColumn<Rol, Long> id = new NumberColumn<>(Rol_.table, "id", Long.class);

    public static final StringColumn<Rol> name = new StringColumn<>(Rol_.table, "name");

    private Rol_()
    {
        super(Rol.class);
    }

}
