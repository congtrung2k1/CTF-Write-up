// 
// Decompiled by Procyon v0.5.36
// 

package androidx.appcompat.view.menu;

import android.view.SubMenu;
import androidx.core.internal.view.SupportSubMenu;
import android.os.Build$VERSION;
import android.view.MenuItem;
import androidx.core.internal.view.SupportMenuItem;
import android.view.Menu;
import androidx.core.internal.view.SupportMenu;
import android.content.Context;

public final class MenuWrapperFactory
{
    private MenuWrapperFactory() {
    }
    
    public static Menu wrapSupportMenu(final Context context, final SupportMenu supportMenu) {
        return (Menu)new MenuWrapperICS(context, supportMenu);
    }
    
    public static MenuItem wrapSupportMenuItem(final Context context, final SupportMenuItem supportMenuItem) {
        if (Build$VERSION.SDK_INT >= 16) {
            return (MenuItem)new MenuItemWrapperJB(context, supportMenuItem);
        }
        return (MenuItem)new MenuItemWrapperICS(context, supportMenuItem);
    }
    
    public static SubMenu wrapSupportSubMenu(final Context context, final SupportSubMenu supportSubMenu) {
        return (SubMenu)new SubMenuWrapperICS(context, supportSubMenu);
    }
}
