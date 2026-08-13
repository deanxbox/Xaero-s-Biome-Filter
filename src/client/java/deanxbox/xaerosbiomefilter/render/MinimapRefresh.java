package deanxbox.xaerosbiomefilter.render;

import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.module.MinimapSession;

public final class MinimapRefresh {
    private MinimapRefresh() {
    }

    public static void request() {
        MinimapSession session = BuiltInHudModules.MINIMAP.getCurrentSession();
        if (session != null) {
            session.getProcessor().setToResetImage(true);
        }
    }
}
