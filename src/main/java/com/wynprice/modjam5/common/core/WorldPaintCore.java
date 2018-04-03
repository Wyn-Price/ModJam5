package com.wynprice.modjam5.common.core;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.Name(value = "World Paint Core")
@IFMLLoadingPlugin.MCVersion(value = "1.12.2")
@IFMLLoadingPlugin.TransformerExclusions({"com.wynprice.modjam5.common.core"})
@IFMLLoadingPlugin.SortingIndex(1001)
public class WorldPaintCore implements IFMLLoadingPlugin {

    public static boolean isDebofEnabled = false;

    @Override
    public String[] getASMTransformerClass() {
        return new String[] {"com.wynprice.modjam5.common.core.WorldPaintTransformer"};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        isDebofEnabled = (boolean) data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

}